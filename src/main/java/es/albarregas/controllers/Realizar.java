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
import java.util.ArrayList;
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
*   - /JSP/create/inicioInsertar.jsp    cuando se introducen los datos de una nueva ave
*   - /JSP/read/leer.jsp                cuando se han visualizado las aves de la base de datos
*   - /JSP/update/leerActualizar.jsp    cuando se ha elegido el ave que se quiere actualizar
*   - /JSP/delete/leerEliminar.jsp      cuando se han elegidos las aves que se quieren eliminar
 */
@WebServlet(name = "Realizar", urlPatterns = {"/realiza"})
public class Realizar extends HttpServlet {

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
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = dataSource.getConnection();
            if (request.getParameter("op").equals("read") || request.getParameter("cancelar") != null) {
                /* 
                * Ya hemos visto el listado de registros o hemos pulsado Cancelar y volvemos al menú inicial
                 */
                url = new StringBuilder("volver");

            } else if (request.getParameter("op").equals("update")) {

                if (request.getParameter("registro") != null) {
                    /* 
                    * Hemos elegido actualizar algún registro para lo cual primero leemos el registro
                    * que queremos actualizar para mostrarselo al usuario
                     */

                    sql = "select * from aves where anilla = ?";
                    preparada = conexion.prepareStatement(sql);
                    preparada.setString(1, request.getParameter("registro"));
                    ave = new Ave();
                    try {
                        resultado = preparada.executeQuery();
                        resultado.next();
                        url = new StringBuilder("/JSP/update/actualizar.jsp");
                        ave.setAnilla(resultado.getString("anilla"));
                        ave.setEspecie(resultado.getString("especie"));
                        ave.setLugar(resultado.getString("lugar"));
                        ave.setFecha(resultado.getDate("fecha"));
                        request.setAttribute("pajaro", ave);
                    } catch (SQLException e) {
                        /*
                        * Existe un error al tratar el resultado devuelto. Escribimos el logger y se visualiza error500.jsp
                         */
                        MyLogger.doLog(e, this.getClass(), "error");

                    }
                } else {
                    /* 
                    * No hemos elegido ningún registro que actualizar y cargamos un atributo de petición con el error correspondiente
                     */
                    request.setAttribute("errorUpdate", "No se ha elegido ningún pájaro que cambiar");
                    /*
                    * Dirigimos el flujo hacia el controlador Operacion donde se volverán a leer los datos de la BD.
                    * Hacemos esto para priorizar el uso de la memoria en contra del acceso a disco
                     */
                    url = new StringBuilder("operacion");

                }

            } else if (request.getParameter("op").equals("delete")) { // Hemos elegido eliminar
                /*
                * Almacenamos las anillas de los registros seleccionados para eliminar en el array avesEliminar
                 */
                String[] avesEliminar = request.getParameterValues("registro");
                StringBuilder clausulaWhere = null;
                /*
                * Construimos la clausula where de la forma: where anilla in ('a1','a2')
                 */
                if (avesEliminar != null && avesEliminar.length != 0) {
                    clausulaWhere = new StringBuilder(" where anilla in (");
                    for (int i = 0; i < avesEliminar.length; i++) {
                        clausulaWhere.append("\'");
                        clausulaWhere.append(avesEliminar[i]);
                        clausulaWhere.append("\',");
                    }
                    clausulaWhere.replace(clausulaWhere.length() - 1, clausulaWhere.length(), ")");
                    /*
                    * Leemos los registros que queremos eliminar
                     */
                    sql = "select * from aves" + clausulaWhere.toString();

                    sentencia = conexion.createStatement();
                    try {
                        resultado = sentencia.executeQuery(sql);
                        aves = new ArrayList();
                        while (resultado.next()) {
                            ave = new Ave();
                            ave.setAnilla(resultado.getString("anilla"));
                            ave.setEspecie(resultado.getString("especie"));
                            ave.setLugar(resultado.getString("lugar"));
                            ave.setFecha(resultado.getDate("fecha"));
                            aves.add(ave);
                        }
                        /*
                        * Guardamos los registros en un ArrayList y lo cargamos en un atributo de la petición
                         */
                        request.setAttribute("lista", aves);
                        url = new StringBuilder("/JSP/delete/eliminar.jsp");
                    } catch (SQLException e) {
                        /*
                        * Existe un error al tratar el resultado devuelto. Escribimos el logger y se visualiza error500.jsp
                         */
                        MyLogger.doLog(e, this.getClass(), "error");

                    }
                } else {
                    /* 
                    * No hemos elegido ningún registro que eliminat y cargamos un atributo de petición con el error correspondiente
                     */
                    request.setAttribute("errorDelete", "No se ha elegido ningún pájaro que eliminar");
                    /*
                    * Dirigimos el flujo hacia el controlador Operacion donde se volverán a leer los datos de la BD.
                    * Hacemos esto para priorizar el uso de la memoria en contra del acceso a disco
                     */
                    url = new StringBuilder("operacion");
                }

            } else {
                /*
                * Procedemos a añadir una nueva ave a la base de datos
                 */
                ave = new Ave();
                /*
                * Cargamos un objeto ave con los datos introducidos por el usuario
                * Utilizamos la clase BeanUtills para pasar del formulario a los atributos del bean correspondiente
                 */
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
                        *   - Dirigimos el flujo hacia el formulario de entrada de datos para insertar
                         */
                        url = new StringBuilder("/JSP/create/inicioInsertar.jsp");
                        request.setAttribute("error", "Todos los campos son obligatorios");

                    } else {
                        
                        try {
                            sql = "insert into aves values (?,?,?,?)";
                            preparada = conexion.prepareStatement(sql);
                            preparada.setString(1, ave.getAnilla());
                            preparada.setString(2, ave.getEspecie());
                            preparada.setString(3, ave.getLugar());
                            preparada.setString(4, ave.getFecha().toString());
                            preparada.executeUpdate();
                            url = new StringBuilder("/JSP/create/finInsertar.jsp");
                        } catch (SQLException e) {
                            if (e.getErrorCode() == 1062) {
                                /*
                                * Si existe el registro quiere decir que se va a a intentar almacenar un registro con anilla duplicada
                                *   - Cargamos un atributo de petición explicando el error cometido
                                *   - Dirigimos el flujo hacia el formulario de entrada de datos para insertar
                                */
                                request.setAttribute("error", "Esta anilla ya existe en nuestra base de datos");
                                url = new StringBuilder("/JSP/create/inicioInsertar.jsp");
                            } else {
                                /*
                                * Existe un error al intentar insertar un registro. Escribimos el logger y se visualiza error500.jsp
                                */
                                MyLogger.doLog(e, this.getClass(), "error");
                            }
                        }

                    }
                    /*
                    * Cargamos un atributo de petición con los datos del ave que ha introducido el usuario para los siguientes casos:
                    *   - Mostrar los datos junto con el error cometido en caso de error
                    *   - Mostrar los datos que se han almacenado en la base de datos
                     */
                    request.setAttribute("pajaro", ave);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    /*
                    * Existe un error al utilizar la clase BeanUtils. Escribimos el logger y se visualiza error500.jsp
                     */
                    MyLogger.doLog(e, this.getClass(), "error");

                } catch (ConversionException e) {
                    /*
                    * Se ha producido un error en el formato de la fecha de entrada. Es un catch poroducido por BeanUtils.populate y realizamos:
                    *   - Cargamos un atributo de petición con el objeto ave para luego mostrar los datos junto con el error
                    *   - Cargamos un atributo de petición explicando el error cometido
                    *   - Dirigimos el flujo hacia el formulario de entrada de datos para insertar
                     */
                    url = new StringBuilder("/JSP/create/inicioInsertar.jsp");
                    request.setAttribute("pajaro", ave);
                    request.setAttribute("error", "El formato de la fecha no es correcto");
                }
            }

        } catch (SQLException e) {
            /*
            * Existe un error al intentar abrir la conexión. Escribimos el logger y se visualiza error500.jsp
             */
            MyLogger.doLog(e, this.getClass(), "fatal");

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
