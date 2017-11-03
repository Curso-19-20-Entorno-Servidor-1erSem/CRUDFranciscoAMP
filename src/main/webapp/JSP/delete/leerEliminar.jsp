<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eliminar</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
    </head>
    <body>
        <div>
            <h2>Elige uno o más aves para eliminar</h2>

            <form action="realiza" method="post">
                <table id="listado">
                    <%
                        List<Ave> listado = null;
                        listado = new ArrayList();
                        listado = (ArrayList<Ave>) request.getAttribute("lista");
                        for (Ave pajaro : listado) {
                    %>

                    <tr>

                        <td><input type="checkbox" name="registro" value="<%=pajaro.getAnilla()%>"/></td>

                        <td><%=pajaro.getEspecie()%></td>


                    </tr>
                    <%
                        }
                    %>
                    <input type="hidden" name="op" value="<%=request.getParameter("op")%>" />

                    <tr><td colspan="2">&nbsp;</td></tr>
                    <tr>
                        <td class="boton"><input type="submit" name="realizar" value="Realizar" /></td>

                        <td class="boton"><input type="submit" name="cancelar" value="Cancelar" /></td>

                    </tr>
                </table>
            </form>



        </div>
        <br />

    </body>
</html>