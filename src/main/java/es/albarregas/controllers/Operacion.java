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

/*
* A este servlet llegan las peticiones realizadas desde:
*   - index.html            al inicio de la aplicación o rama
*   - /Concluir.java        cuando en la lista de aves no se ha elegido ninguna que actiualizar o eliminar
*/
@WebServlet(name = "Operacion", urlPatterns = {"/operacion"})
public class Operacion extends HttpServlet {

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
        ResultSet resultado = null;
        Connection conexion = null;
        /*
        * Variable que se utiliza para saber de donde viene el flujo de la aplicación e incluso forzando a que tenga un comportamiento u otro.
        * Cuando el flujo viene desde que no se hayan elegido registros que actualizar o eliminar se fuerza a que siga por la rama adecuada
        * preguntando por la existencia del mensaje de error que viene en el atributo errorUpdate o errorDelete.
        */
        String operacion = null;

        if(request.getAttribute("errorUpdate") != null || request.getAttribute("errorDelete") != null) {
            /*
            * Depediendo de donde venga el flujo, actualización o borrado se carga la variable operación para que siga en esa rama
            */
            operacion = (request.getAttribute("errorUpdate") != null)?"(U)":"(D)";
            
        } else {
            /*
            * En el caso de que venga del index se carga con el valor del botón pulsado
            */
            operacion = request.getParameter("boton");
        }
        
        if (operacion.startsWith("(C)")) {
            /*
            * Hemos elegido insertar un ave y dirigimos el flujo a la primera página de añadir una nueva ave
            */
            url = new StringBuilder("create/inicioInsertar.jsp");
        } else {
            /* 
            * En caso contrario leemos todos los registros de la tabla para visualizarlos para las operaciones read, update y delete
            */
            try {
                conexion = dataSource.getConnection();
                sql = "select * from aves";
                sentencia = conexion.createStatement();
                try {
                    resultado = sentencia.executeQuery(sql);
                    /*
                    * Cargamos un ArrayList con todos los registros de la tabla aves
                    */
                    aves = new ArrayList();
                    while (resultado.next()) {
                        ave = new Ave();
                        ave.setAnilla(resultado.getString("anilla"));
                        ave.setEspecie(resultado.getString("especie"));
                        ave.setLugar(resultado.getString("lugar"));
                        ave.setFecha(resultado.getDate("fecha"));
                        aves.add(ave);
                    }
                    
                    if (!aves.isEmpty()) { 
                        /*
                        * En el caso de que existan registros en la tabla aves. 
                        *   - Creamos un atributo de petición con los datos de todas las aves
                        *   - Dirigimos el flujo  a donde corresponda dependiendo de la variable operacion
                        */
                        request.setAttribute("lista", aves);

                        if (operacion.startsWith("(R)")) {
                        
                            url = new StringBuilder("read/leer.jsp");
                        
                        } else if (operacion.startsWith("(U)")) {
                        
                            url = new StringBuilder("update/leerActualizar.jsp");
                        
                        } else {
                        
                            url = new StringBuilder("delete/leerEliminar.jsp");

                        }
                    } else { 
                        /*
                        * Cuando no existen registros en la tabla aves
                        * Cargamos un atributo de petición con el error producido y dirigimos el flujo a la página de error
                        */
                        request.setAttribute("error", "No existen datos guardados");
                        url = new StringBuilder("general/error.jsp");
                    }

                    
                } catch (SQLException e) {
                    /*
                    * Existe un error al tratar el resultado devuelto. Escribimos el logger y se visualiza error500.jsp
                    */
                    MyLogger.doLog(e, this.getClass(), "error");

                }
            } catch (SQLException e) {
                /*
                * Existe un error al intentar conectarnos a la base de datos. Escribimos el logger y visualiza error500.jsp
                */
                MyLogger.doLog(e, this.getClass(), "fatal");
      
            } finally {
                /*
                * Liberamos los recursos
                */
                Conexion.closeConexion(conexion);
            }

        }
        /*
        * Añadimos al principio del StringBuilder la cadena "/JSP/" para que se dirija a /JSP/...
        */
        url = url.insert(0, "/JSP/");
        request.getRequestDispatcher(url.toString()).forward(request, response);

    }

}
