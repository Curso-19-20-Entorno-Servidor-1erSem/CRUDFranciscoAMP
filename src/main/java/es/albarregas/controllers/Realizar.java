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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;



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
                // Ya hemos visto el listado de registros o hemos pulsado Cancelar y volvemos al menú inicial
                url = new StringBuilder("index.html");

            } else if (request.getParameter("op").equals("update")) {

                if (request.getParameter("registro") != null) {
                    /* 
                        Hemos elegido actualizar algún registro para lo cual primero leemos el registro
                        que queremos actualizar para mostrarselo al usuario
                     */

                    sql = "select * from pajaros where anilla = ?";
                    preparada = conexion.prepareStatement(sql);
                    preparada.setString(1, request.getParameter("registro"));
                    try {
                        resultado = preparada.executeQuery();
                        resultado.next();
                        url = new StringBuilder("update/actualizar.jsp");
                        request.setAttribute("anilla", resultado.getString("anilla"));
                        request.setAttribute("especie", resultado.getString("especie"));
                        request.setAttribute("lugar", resultado.getString("lugar"));
                        request.setAttribute("fecha", resultado.getDate("fecha"));
                    } catch (SQLException e) {
                        MyLogger.doLog(e, this.getClass(), "fatal");
                        url = new StringBuilder("error500.jsp");
                    }
                } else {
                    // No hemos elegido ningún registro que actualizar y nos vamos a la página final de actualización
                    url = new StringBuilder("update/finActualizar.jsp");
                }

            } else { // Hemos elegido eliminar

                // Almacenamos las anillas de los registros seleccionados para eliminar en el array avesEliminar
                String[] avesEliminar = request.getParameterValues("registro");
                StringBuilder clausulaWhere = null;
                /*
                    Construimos la clausula where de la forma where anilla in ('a1','a2')
                 */
                if (avesEliminar != null && avesEliminar.length != 0) {
                    clausulaWhere = new StringBuilder(" where anilla in (");
                    for (int i = 0; i < avesEliminar.length; i++) {
                        clausulaWhere.append("\'");
                        clausulaWhere.append(avesEliminar[i]);
                        clausulaWhere.append("\',");
                    }
                    clausulaWhere.replace(clausulaWhere.length() - 1, clausulaWhere.length(), ")");
                    // Leemos los registros que queremos eliminar
                    sql = "select * from pajaros" + clausulaWhere.toString();

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
                        // Guardamos los registros en un ArrayList y lo lanzamos a un atributo de la petición
                        request.setAttribute("lista", aves);
                        url = new StringBuilder("delete/eliminar.jsp");
                    } catch (SQLException e) {
                        MyLogger.doLog(e, this.getClass(), "fatal");
                        url = new StringBuilder("error500.jsp");
                    }
                } else {
                    // No hemos seleccionado ningún registro que eliminar
                    url = new StringBuilder("delete/finEliminar.jsp");
                }

            }
            // En el caso de que no sea visualización los documentos de las diferentes vistas se encuentran en /JSP
            if (!request.getParameter("op").equals("read") && request.getParameter("cancelar") == null) {
                url = url.insert(0, "/JSP/");
            }
            request.getRequestDispatcher(url.toString()).forward(request, response);

        } catch (SQLException ex) {
            MyLogger.doLog(ex, this.getClass(), "fatal");
            url = new StringBuilder("error500.jsp");
        } finally {

            Conexion.closeConexion(conexion, resultado);
        }
    }
}
