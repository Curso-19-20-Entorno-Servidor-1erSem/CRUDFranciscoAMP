package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import es.albarregas.connections.Conexion;
import es.albarregas.utils.MyLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


@WebServlet(name = "Concluir", urlPatterns = {"/conclusion"})
public class Concluir extends HttpServlet {

    DataSource dataSource = null;
    
    private final static Logger LOG = Logger.getLogger(Concluir.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        
        dataSource = Conexion.getDataSource();

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder url = null;
        String sql = null;
        StringBuilder clausulaWhere = new StringBuilder();
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = dataSource.getConnection();
            if (request.getParameter("cancelar") != null) {
                // En el caso de haber pulsado Cancelar nos dirigimos al menú principal
                url = new StringBuilder("index.html");
            } else if (request.getParameter("actualizar") != null) {
                // Venimos de la página donde se introducen los datos que queremos actualizar (actualizar.jsp)
                sql = "select * from pajaros where anilla = ?";
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
//                    LOG.trace("el valor de la variable indice es " + indice);
                    
                }

                if (clausulaWhere.length() != 0) {
                    // Si se han realizado cambios en el registro
                    sql = "update pajaros" + clausulaWhere.toString() + " where anilla='" + request.getParameter("anilla") + "'";
                    // Actualizamos el registro en la base de datos
                    sentencia = conexion.createStatement();
                    try {

                        sentencia.executeUpdate(sql);
                        url = new StringBuilder("update/finActualizar.jsp");
                        // Añadimos al log de información la operación que se ha realizado

                        
                        request.setAttribute("registro", request.getParameter("anilla"));
                    } catch(SQLException e) {
                        // En el caso de que se haya producido algún error se notificará en el fichero de log
//                        e.printStackTrace();
                        MyLogger.doLog(e, this.getClass(), "error");
                        url = new StringBuilder("error500.jsp");
                    }

                } else {
                    // Visualizaremos que no se han realizado cambios
                    url = new StringBuilder("update/finActualizar.jsp");
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
                sql = "delete from pajaros " + clausulaWhere.toString();

                sentencia = conexion.createStatement();

                try {
                    sentencia.executeUpdate(sql);
                    // Todo correcto, visualizaremos el número de registros eliminados
                    url = new StringBuilder("delete/finEliminar.jsp");
                    request.setAttribute("numero", (Integer) listado.length);
                    

                } catch (SQLException e){

                    // En el caso de que se haya producido algún error se notificará 
                    //  en el fichero de log
//                    e.printStackTrace();
                    MyLogger.doLog(e, this.getClass(), "error");
                    url = new StringBuilder("error500.jsp");
                }

            } else {
//                // Vamos a crear un nuevo registro y los datos se han introducido en el formulario de la página insertar.jsp
                HttpSession sesion = request.getSession();
                ave = new Ave();
                ave = (Ave)sesion.getAttribute("pajaro");
//                // Utilizamos la clase BeanUtills para pasar del formulario a los atributos del bean correspondiente
//                try {
//                    BeanUtils.populate(ave, request.getParameterMap());
//                } catch (IllegalAccessException | InvocationTargetException ex) {
//
//                    MyLogger.doLog(ex, this.getClass(), "error");
//                        url = new StringBuilder("error500.jsp");
//                }
                
                try {
                    sql = "insert into pajaros values(?,?,?,?)";

                    preparada = conexion.prepareStatement(sql);
                    preparada.setString(1, ave.getAnilla());
                    preparada.setString(2, ave.getEspecie());
                    preparada.setString(3, ave.getLugar());
                    preparada.setDate(4, ave.getFecha());

//                    request.setAttribute("pajaro", ave);
                    preparada.executeUpdate();
                    url = new StringBuilder("create/finInsertar.jsp");
                    

                } catch (SQLException e) {

                    if (e.getErrorCode() == 1062) {
                        /*
                        En el caso de claves duplicadas volvemos a la página insertar.jsp donde se notificará el error y
                        mantendrán los datos anteriormente introducidos salvo la anilla
                        */
                        MyLogger.doLog(e, this.getClass(), "error");
                        
                        request.setAttribute("error", "Se ha intentado duplicar la clave primaria");
                        url = new StringBuilder("create/inicioInsertar.jsp");

                    } else {
//                        e.printStackTrace();
                        MyLogger.doLog(e, this.getClass(), "error");
                        url = new StringBuilder("error500.jsp");

                    }

                }
            }
        } catch (SQLException e) {
//            e.printStackTrace();
            MyLogger.doLog(e, this.getClass(), "error");
            url = new StringBuilder("error500.jsp");
        } finally {

            Conexion.closeConexion(conexion, resultado);
        }
        if(url.indexOf("index") == -1) {
            url = url.insert(0, "/JSP/");
        }

        request.getRequestDispatcher(url.toString()).forward(request, response);

    }

}
