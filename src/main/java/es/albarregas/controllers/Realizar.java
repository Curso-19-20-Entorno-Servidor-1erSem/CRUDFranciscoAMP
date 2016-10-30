package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

@WebServlet(name = "Realizar", urlPatterns = {"/realiza"})
public class Realizar extends HttpServlet {

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

        switch (request.getParameter("op")) {
            case "lee": // Ya hemos visto el listado de registros y volvemos al menú inicial
                url = "index.html";
                break;
            case "actualiza":
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
                break;
            case "elimina":
                try {
                    conexion = datasource.getConnection();
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
        // En el caso de que no sea visualización los documentos de las diferentes vistas se encuentran en /JSP
        if(!request.getParameter("op").equals("lee")){
            url = "/JSP/" + url;
        }
        request.getRequestDispatcher(url).forward(request, response);

    }

}
