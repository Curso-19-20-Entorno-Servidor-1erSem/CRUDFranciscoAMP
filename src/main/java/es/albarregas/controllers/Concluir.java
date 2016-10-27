package es.albarregas.controllers;

import es.albarregas.beans.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
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

@WebServlet(name = "Concluir", urlPatterns = {"/conclusion"})
public class Concluir extends HttpServlet {

    DataSource datasource;

    @Override
    public void init(ServletConfig config)
            throws ServletException {
        try {
            Context contextoInicial = new InitialContext();
            datasource = (DataSource) contextoInicial.lookup("java:comp/env/jdbc/CRUDPool");
        } catch (NamingException ex) {
            System.out.println("Problemas en el acceso a la BD");
            ex.printStackTrace();
        }

    }

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
            conexion = datasource.getConnection();
            if(request.getParameter("cancelar") != null){
                url = "index.html";
            } else if(request.getParameter("actualizar") != null){
                
                sql = "select * from aves where anilla = ?";
                preparada = conexion.prepareStatement(sql);
                preparada.setString(1, request.getParameter("anilla"));
                resultado = preparada.executeQuery();
                resultado.next();
                Enumeration<String> parametros = request.getParameterNames();
                int indice = 1;
                boolean primeraVez = true;
                while(parametros.hasMoreElements()){
                    String nombre = parametros.nextElement();

                    if(!nombre.equals("anilla") && !nombre.equals("actualizar")){
                        if(!request.getParameter(nombre).equals(resultado.getString(indice))){
                            if (primeraVez){
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
                if(clausulaWhere.length() != 0){
                    sql = "update aves" + clausulaWhere.toString() + " where anilla='" + request.getParameter("anilla") + "'";
                    System.out.println("sentencia sql = " + sql);
                    sentencia = conexion.createStatement();
                    if(sentencia.executeUpdate(sql) != 0){
                        url = "finActualizar.jsp";
//                        request.setAttribute("registro", request.getParameter("anilla"));
                    } else {
                        url = "error.jsp";
                        request.setAttribute("error", "ERROR. Ocurrió un error al actualizar la base de datos para la anilla " +request.getParameter("anilla"));
                    }
                
                } else {
                    url = "finActualizar.jsp";
                    request.setAttribute("sincambios", (Boolean)true);
                    
                }
            } else {
                String[] listado = request.getParameterValues("anilla");
                if (listado.length == 0){
                    url = "index.html";
                } else {
                    
                    clausulaWhere = new StringBuilder(" where anilla in (");
                    for(String anilla : listado){
                        clausulaWhere.append("'");
                        clausulaWhere.append(anilla);
                        clausulaWhere.append("',");
                    }
                    clausulaWhere.replace(clausulaWhere.length()-1, clausulaWhere.length(), ")");
                    sql = "delete from aves " + clausulaWhere.toString();
                    System.out.println("sentencia sql = " + sql);
                    sentencia = conexion.createStatement();
                    if(sentencia.executeUpdate(sql) != 0){
                        url = "finEliminar.jsp";
                        request.setAttribute("numero", (Integer)listado.length);
                    } else {
                        url = "error.jsp";
                        request.setAttribute("error", "ERROR. Ocurrió un error al actualizar la base de datos para la anilla " +request.getParameter("anilla"));
                    }
                    
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

                
        
        request.getRequestDispatcher(url).forward(request, response);

    }

}
