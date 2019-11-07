package es.albarregas.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* A este servlet llegan las peticiones realizas desde:
*   - /JSP/create/finInsertar.jsp       donde se han visualizados los datos añadidos a la base de datos
*   - /JSP/update/finActualizar.jsp     donde se ha informado de la actualización de los campos de la base datos
*   - /JSP/delete/finEliminar.jsp       donde se ha informado de los registros eliminados de la base de datos
*   - /Realizar.java                    cuando hemos cancelado el proceso en el primer paso de añadir, actualizar o eliminar:
*       + /JSP/create/inicioInsertar.jsp    
*       + /JSP/update/leerActualizar.jsp    
*       + /JSP/delete/leerEliminar.jsp 
*   - /Concluir.java                    cuando hemos cancelado el proceso en el último paso de actualizar o eliminar:
*       + /JSP/update/actualizar.jsp
*       + /JSP/delete/eliminar.jsp
*/
@WebServlet(name = "Volver", urlPatterns = {"/volver"})
public class Volver extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
        * Dirigimos el flujo hacia el menú pricipal de la aplicación
        */
        request.getRequestDispatcher("/index.html").forward(request, response);
    }


}
