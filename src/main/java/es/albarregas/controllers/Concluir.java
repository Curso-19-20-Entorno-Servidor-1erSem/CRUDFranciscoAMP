package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import es.albarregas.conexion.Conexion;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

@WebServlet(name = "Concluir", urlPatterns = {"/conclusion"})
public class Concluir extends HttpServlet {


    // logger general para toda la aplicación
    final static Logger LOGGER = Logger.getRootLogger();
    // logger destinado a llevar un registro de las diferentes operaciones exitosas
    final static Logger DESC = Logger.getLogger(Concluir.class);



    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = null;
        String sql = null;
        StringBuilder clausulaWhere = new StringBuilder();
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = Conexion.getDataSource().getConnection();
            if (request.getParameter("cancelar") != null) {
                // En el caso de haber pulsado Cancelar nos dirigimos al menú principal
                url = "index.html";
            } else if (request.getParameter("actualizar") != null) {
                // Venimos de la página donde se introducen los datos que queremos actualizar (actualizar.jsp)
                sql = "select * from aves where anilla = ?";
                preparada = conexion.prepareStatement(sql);
                preparada.setString(1, request.getParameter("anilla"));
                // Leemos los datos de la base de datos para comprobar los que han cambiado
                resultado = preparada.executeQuery();
                resultado.next();
                Enumeration<String> parametros = request.getParameterNames();
                int indice = 1;
                boolean primeraVez = true;
                /* 
                Construimos la parte de los cambios de la forma set campo1=valor1,campo2=valor2 ... 
                o nada en el caso de no haber cambiado ningún valor
                 */
                while (parametros.hasMoreElements()) {
                    String nombre = parametros.nextElement();

                    if (!nombre.equals("anilla") && !nombre.equals("actualizar")) {
                        if (!request.getParameter(nombre).equals(resultado.getString(indice))) {
                            if (primeraVez) {
                                clausulaWhere.append(" set ");
                                primeraVez = false;
                            } else {
                                clausulaWhere.append(",");
                            }

                            clausulaWhere.append(nombre);
                            clausulaWhere.append("='");
                            clausulaWhere.append(request.getParameter(nombre));
                            clausulaWhere.append("'");
                        }

                    }
                    indice++;
                }

                if (clausulaWhere.length() != 0) {
                    // Si se han realizado cambios en el registro
                    sql = "update aves" + clausulaWhere.toString() + " where anilla='" + request.getParameter("anilla") + "'";
                    // Actualizamos el registro en la base de datos
                    sentencia = conexion.createStatement();
                    if (sentencia.executeUpdate(sql) != 0) {
                        url = "/JSP/finActualizar.jsp";
                        // Añadimos al log de información la operación que se ha realizado
                        DESC.info("ACTUALIZAR. Se ha actualizado el registro de anilla " + request.getParameter("anilla"));
                        request.setAttribute("registro", request.getParameter("anilla"));
                    } else {
                        // En el caso de que se haya producido algún error se notificará en la página correspondiente
                        url = "/JSP/error.jsp";
                        request.setAttribute("error", "ERROR. Ocurrió un error al actualizar la base de datos para la anilla " + request.getParameter("anilla"));
                    }

                } else {
                    // Visualizaremos que no se han realizado cambios
                    url = "/JSP/finActualizar.jsp";
                    request.setAttribute("sincambios", (Boolean) true);

                }
            } else if (request.getParameter("eliminar") != null) {
                /*
                Venimos de la página donde se han mostrado los registros que se pretenden eliminar.
                Hemos pasado en unos campos de texto ocultos las diferentes anillas que se quieren eliminar
                 */
                String[] listado = request.getParameterValues("anilla");
                // Construimos la clausula where de la forma where anilla in ('a1','a2')
                clausulaWhere = new StringBuilder(" where anilla in (");
                for (String anilla : listado) {
                    clausulaWhere.append("'");
                    clausulaWhere.append(anilla);

                    clausulaWhere.append("',");

                }
                clausulaWhere.replace(clausulaWhere.length() - 1, clausulaWhere.length(), ")");
                sql = "delete from aves " + clausulaWhere.toString();

                sentencia = conexion.createStatement();
                if (sentencia.executeUpdate(sql) != 0) {
                    // Todo correcto, visualizaremos el número de registros eliminado
                    url = "/JSP/finEliminar.jsp";
                    request.setAttribute("numero", (Integer) listado.length);
                    // Añadimos al log de información la operación que se ha realizado
                    DESC.info("ELIMINAR. Se ha eliminado el registro de anilla "
                            + clausulaWhere.substring(clausulaWhere.indexOf("(") + 1, clausulaWhere.length() - 2).replaceAll("'", ""));
                } else {
                    // En el caso de que se haya producido algún error se notificará en la página correspondiente
                    url = "/JSP/error.jsp";
                    request.setAttribute("error", "ERROR. Ocurrió un error al actualizar la base de datos para la anilla " + request.getParameter("anilla"));
                }

            } else {
                // Vamos a crear un nuevo registro y los datos se han introducido en el formulario de la página insertar.jsp
                ave = new Ave();
                // Utilizamos la clase BeanUtills para pasar del formulario a los atributos del bean correspondiente
                try {
                    BeanUtils.populate(ave, request.getParameterMap());
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    LOGGER.fatal("Problema con el enlace del formulario con el bean", ex);
                }
                try {
                    sql = "insert into aves values(?,?,?,?)";

                    preparada = conexion.prepareStatement(sql);
                    preparada.setString(1, ave.getAnilla());
                    preparada.setString(2, ave.getEspecie());
                    preparada.setString(3, ave.getLugar());
                    preparada.setString(4, ave.getFecha());

                    request.setAttribute("pajaro", ave);
                    preparada.executeUpdate();
                    url = "/JSP/finInsertar.jsp";
                    // Añadimos al log de información la operación que se ha realizado
                    DESC.info("ALTAS. Se ha añadido el ave con la anilla " + ave.getAnilla());

                } catch (SQLException ex) {

                    if (ex.getErrorCode() == 1062) {
                        /*
                        En el caso de claves duplicadas volvemos a la página insertar.jsp donde se notificará el error y
                        mantendrán los datos anteriormente introducidos salvo la anilla
                        */
                        request.setAttribute("error", "ERROR. Se ha intentado duplicar la clave primaria");
                        url = "/JSP/insertar.jsp";

                    } else {
                        // Añadiremos al log de errores y mostraremos una pantalla de error
                        LOGGER.fatal("Problema con la instrucción de SQL", ex);

                    }

                }
            }
        } catch (SQLException ex) {
            LOGGER.fatal("Problema con la conexion a la base de datos", ex);
        } finally {

            Conexion.closeConexion(conexion, resultado);
        }

        request.getRequestDispatcher(url).forward(request, response);

    }

}
