package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import es.albarregas.connections.Conexion;
import es.albarregas.utils.MyLogger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;


/*
* A este servlet llegan las peticiones realizas desde:
*   - /JSP/update/actualizar.jsp        cuando se han cambiado los datos de los campos de la base de datos
*   - /JSP/delete/eliminar.jsp          cuando se ha informado de los registros que se eliminarán de la base de datos
*/
@WebServlet(name = "Concluir", urlPatterns = {"/conclusion"})
public class Concluir extends HttpServlet {

    DataSource dataSource = null;

    

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
//        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = dataSource.getConnection();
            if (request.getParameter("cancelar") != null) {
                /*
                * Cuando pulsamos cancelar en cualquier página que llame a este Servlet dirigimos el flujo hacia menú principal
                */
                url = new StringBuilder("volver");
            } else if (request.getParameter("actualizar") != null) {
                /*
                * Venimos de la página donde se introducen los datos que queremos actualizar (actualizar.jsp)
                * Primero vamos a comprobar los datos de entrada para validarlos
                */ 
                ave = new Ave();
                try {
                    BeanUtils.populate(ave, request.getParameterMap());

                    /* 
                    * Comprobamos que todos los campos estén rellenos
                    */
                    boolean error = false;
                    Enumeration<String> parametros = request.getParameterNames();
                    while (parametros.hasMoreElements() && !error) {
                        String nombre = parametros.nextElement();
                        if (request.getParameter(nombre).length() == 0) {
                            error = true;
                        }
                    }
                    if (error) {
                        /*
                        * En el caso de que exista error se realizan las siguientes funciones:
                        *   - Cargamos un atributo de petición explicando el error cometido
                        *   - Dirigimos el flujo hacia el formulario de entrada de datos para actualizar
                        */
                        url = new StringBuilder("/JSP/update/actualizar.jsp");
                        request.setAttribute("error", "Todos los campos son obligatorios");
                        request.setAttribute("pajaro", ave);
                    } else {
                        /*
                        * Leemos el registro de la base de datos para poder comprobar los campos que han cambiado
                        */
                        sql = "select * from aves where anilla = ?";
                        preparada = conexion.prepareStatement(sql);
                        preparada.setString(1, request.getParameter("anilla"));
                        
                        resultado = preparada.executeQuery();
                        resultado.next();
                        parametros = request.getParameterNames();
                        int indice = 1;
                        boolean primeraVez = true;
                        /* 
                        * Construimos la parte de los cambios de la forma set campo1=valor1,campo2=valor2 ... 
                        * o nada en el caso de no haber cambiado ningún valor
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

                                    clausulaWhere.append(nombre).append("='").append(request.getParameter(nombre)).append("'");

                                }

                            }
                            indice++;


                        }

                        if (clausulaWhere.length() != 0) {
                            /* 
                            * Se han realizado cambios en el registro y por lo tanto actualizamos la base de datos y 
                            * en la página final de la actualización se informa que se ha cambiado el registro
                            */
                            sql = "update aves" + clausulaWhere.toString() + " where anilla=?";
                            
                            preparada = conexion.prepareStatement(sql);
                            preparada.setString(1, request.getParameter("anilla"));
                            try {

                                preparada.executeUpdate();

                                
                                request.setAttribute("registro", request.getParameter("anilla"));
                            } catch (SQLException e) {
                                /*
                                * Existe un error al ejecutar la sentencia update. Escribimos el logger y se visualiza error500.jsp
                                */
                                MyLogger.doLog(e, this.getClass(), "error");

                            }

                        } else {
                            /*
                            * No se han realizado cambios en ninguno de los campos y en la última página de actualizar informaremos de ello
                            */

                            request.setAttribute("sincambios", (Boolean) true);

                        }
                        /*
                        * Dirigimos el flujo a la última página jsp de la rama de actualización
                        */
                        url = new StringBuilder("/JSP/update/finActualizar.jsp");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    /*
                    * Existe un error al utilizar la clase BeanUtils. Escribimos el logger y se visualiza error500.jsp
                    */
                    MyLogger.doLog(e, this.getClass(), "error");
                } catch (ConversionException e) {
                    /*
                    * Se ha producido un error en el formato de la fecha de entrada. Es un catch producido por BeanUtils.populate y realizamos:
                    *   - Cargamos un atributo de petición con el objeto ave para luego mostrar los datos junto con el error
                    *   - Cargamos un atributo de petición explicando el error cometido
                    *   - Dirigimos el flujo hacia el formulario de entrada de datos para actualizar
                    */
                    url = new StringBuilder("/JSP/update/actualizar.jsp");
                    request.setAttribute("pajaro", ave);
                    request.setAttribute("error", "El formato de la fecha no es correcto");
                }

            } else if (request.getParameter("eliminar") != null) {
                /*
                * Venimos de la página donde se han mostrado los registros que se pretenden eliminar (eliminar.jsp).
                * Hemos pasado en unos campos ocultos las diferentes anillas que se quieren eliminar 
                * para construir la cláusula where de la forma: where anilla in ('a1','a2') 
                */
                String[] listado = request.getParameterValues("anilla");
                
                clausulaWhere = new StringBuilder(" where anilla in (");
                for (String anilla : listado) {
                    clausulaWhere.append("'");
                    clausulaWhere.append(anilla);

                    clausulaWhere.append("',");

                }
                clausulaWhere.replace(clausulaWhere.length() - 1, clausulaWhere.length(), ")");
                /*
                * Hacemos efectivo el borrado de las aves seleccionadas
                */
                sql = "delete from aves " + clausulaWhere.toString();

                sentencia = conexion.createStatement();

                try {
                    /*
                    * Todo correcto se digrige el flujo a la última página de la rama eliminar dode se informará de los registros eliminados
                    */
                    sentencia.executeUpdate(sql);
                    
                    url = new StringBuilder("/JSP/delete/finEliminar.jsp");
                    request.setAttribute("numero", (Integer) listado.length);

                } catch (SQLException e) {
                    /*
                    * Existe un error al ejecutar la sentencia delete. Escribimos el logger y se visualiza error500.jsp
                    */
                    MyLogger.doLog(e, this.getClass(), "error");

                }

            } 
        } catch (SQLException e) {
            /*
            * Existe un error al intentar abrir la conexión. Escribimos el logger y se visualiza error500.jsp
            */
            MyLogger.doLog(e, this.getClass(), "error");

        } finally {
            /*
            * Liberamos recursos
            */
            Conexion.closeConexion(conexion);
        }
        /*
        * Hacemos efectivo el flujo de la aplicación
        */
        request.getRequestDispatcher(url.toString()).forward(request, response);

    }

}
