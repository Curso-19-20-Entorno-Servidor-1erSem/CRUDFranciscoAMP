package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import es.albarregas.connections.Conexion;
import es.albarregas.utils.MyLogger;

import java.io.IOException;
import java.sql.Connection;
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

@WebServlet(name = "Operacion", urlPatterns = {"/operacion"})
public class Operacion extends HttpServlet {

    DataSource dataSource = null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        
        dataSource = Conexion.getDataSource();

    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder url = null;
        String sql = null;
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        ResultSet resultado = null;
        Connection conexion = null;

        String operacion = request.getParameter("op");
        if(operacion.equals("create")) {
            url = new StringBuilder("create/insertar.jsp");
        } else {
            // En caso contrario leemos todos los registros de la tabla para visualizarlos en listado.jsp

                try {
                    conexion = dataSource.getConnection();
                    sql = "select * from pajaros";
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

                        request.setAttribute("lista", aves);
                        
                        switch(operacion){
                            case "read":
                                url = new StringBuilder("read/leer.jsp");
                                break;
                            case "update":
                                url = new StringBuilder("update/leerActualizar.jsp");
                                break;
                            case "delete":
                                url = new StringBuilder("delete/leerEliminar.jsp");
                                
                        }

                    // En caso de error escribimos en el fichero de log y mostramos una pantalla de error
                    } catch (Exception e) {
                        MyLogger.doLog(e, this.getClass(), "fatal");
                        url = new StringBuilder("error500.jsp");
                        
            
                    }
                } catch (SQLException e) {
                    MyLogger.doLog(e, this.getClass(), "error");
                    url = new StringBuilder("error500.jsp");
                // Liberamos los recursos
                } finally {

                    Conexion.closeConexion(conexion, resultado);
                }

                
        }


        url = url.insert(0, "/JSP/");
        request.getRequestDispatcher(url.toString()).forward(request, response);
        

    }

}
