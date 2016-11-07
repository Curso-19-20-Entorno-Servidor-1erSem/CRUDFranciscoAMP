package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import es.albarregas.conexion.Conexion;
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

import org.apache.log4j.Logger;

@WebServlet(name = "Realizar", urlPatterns = {"/realiza"})
public class Realizar extends HttpServlet {

    DataSource datasource = null;
    final static Logger LOGGER = Logger.getRootLogger();

    public void init(ServletConfig config) throws ServletException {
        
        datasource = Conexion.getDataSource();
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = null;
        String sql = null;
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Connection conexion = null;
        

        if (request.getParameter("op").equals("lee") || request.getParameter("cancelar") != null) {
            // Ya hemos visto el listado de registros o hemos pulsado Cancelar y volvemos al menú inicial
                url = "index.html";
               
        } else if (request.getParameter("op").equals("actualiza")) {
                try {
                    if (request.getParameter("registro") != null) {
                        /* 
                        Hemos elegido actualizar algún registro para lo cual primero leemos el registro
                        que queremos actualizar para mostrarselo al usuario
                        */
                        conexion = datasource.getConnection();
                        sql = "select * from aves where anilla = ?";
                        preparada = conexion.prepareStatement(sql);
                        preparada.setString(1, request.getParameter("registro"));
                        resultado = preparada.executeQuery();
                        resultado.next();
                        url = "actualizar.jsp";
                        request.setAttribute("anilla", resultado.getString("anilla"));
                        request.setAttribute("especie", resultado.getString("especie"));
                        request.setAttribute("lugar", resultado.getString("lugar"));
                        request.setAttribute("fecha", resultado.getString("fecha"));
                    } else {
                        // No hemos elegido ningún registro que actualizar y nos vamos a la página final de actualización
                        url = "finActualizar.jsp";
                    }
                } catch (SQLException ex) {
                    LOGGER.fatal("Problemas en la conexión a la base de datos o en SQL", ex);
                }
        } else { // Hemos elegido eliminar
            
                try {
                    conexion = Conexion.getDataSource().getConnection();
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
                        sql = "select * from aves" + clausulaWhere.toString();
                        
                        sentencia = conexion.createStatement();
                        resultado = sentencia.executeQuery(sql);
                        aves = new ArrayList();
                        while (resultado.next()) {
                            ave = new Ave();
                            ave.setAnilla(resultado.getString("anilla"));
                            ave.setEspecie(resultado.getString("especie"));
                            ave.setLugar(resultado.getString("lugar"));
                            ave.setFecha(resultado.getString("fecha"));
                            aves.add(ave);
                        }
                        // Guardamos los registros en un ArrayList y lo lanzamos a un atributo de la petición
                        request.setAttribute("lista", aves);
                        url = "eliminar.jsp";
                    } else {
                        // No hemos seleccionado ningún registro que eliminar
                        url = "finEliminar.jsp";
                    }
                } catch (SQLException ex) {
                    LOGGER.fatal("Problemas en la conexión a la base de datos o SQL", ex);
                } finally {

                    Conexion.closeConexion(conexion, resultado);
                }

                
        }
        // En el caso de que no sea visualización los documentos de las diferentes vistas se encuentran en /JSP
        if(!request.getParameter("op").equals("lee") && request.getParameter("cancelar") == null){
            url = "/JSP/" + url;
        }
        request.getRequestDispatcher(url).forward(request, response);

    }

}
