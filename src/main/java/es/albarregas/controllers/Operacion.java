package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

@WebServlet(name = "Operacion", urlPatterns = {"/operacion"})
public class Operacion extends HttpServlet {

    DataSource datasource;
    final static Logger LOGGER = Logger.getRootLogger();

    @Override
    public void init(ServletConfig config)
            throws ServletException {
        try {
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/CRUDPool");
        } catch (NamingException ex) {
            LOGGER.fatal("Problemas en el acceso al pool de conexiones", ex);

        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = null;
        String sql = null;
        Ave ave = null;
        ArrayList<Ave> aves = null;
        Statement sentencia = null;
        ResultSet resultado = null;
        Connection conexion = null;
        switch (request.getParameter("op")) {
            case "crea": // Nos vamos a la página de crear un pajaro
                url = "insertar.jsp";
                break;
            // En caso contrario leemos tos los registros de la tabla para visualizarlos en listado.jsp
            case "actualiza":
            case "lee":
            case "elimina":
                try {
                    conexion = datasource.getConnection();
                    sql = "select * from aves";
                    sentencia = conexion.createStatement();
                    try {
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

                        request.setAttribute("lista", aves);
                        url = "listado.jsp";
                    // En caso de error escribimos en el fichero de log y mostramos una pantalla de error
                    } catch (SQLException e) {
                        LOGGER.fatal("Problema al ejecutar la instrucción SQL", e);
            
                    }
                } catch (SQLException e) {
                    LOGGER.fatal("Problemas en el acceso al pool de conexiones", e);
                // Liberamos los recursos
                } finally {
                    try {
                        if (conexion != null) {
                            conexion.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    try {
                        if (resultado != null) {
                            resultado.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                break;
        }

        url = "/JSP/" + url;
        request.getRequestDispatcher(url).forward(request, response);

    }

}
