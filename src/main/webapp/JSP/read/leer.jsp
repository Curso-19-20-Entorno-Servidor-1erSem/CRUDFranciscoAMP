<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listado</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
    </head>
    <body>
        <div id="principal">
            <h2>Listado de todas las aves</h2>

            <form action="realiza" method="post">
                <%
                        List<Ave> listado = null;
                        listado = new ArrayList();
                        listado = (ArrayList<Ave>) request.getAttribute("lista");
                        if(!listado.isEmpty()) {
                %>
                <table id="listado">
                    <tr>
                        <th>Anilla</th>
                        <th>Especie</th>
                        <th>Lugar</th>
                        <th>Fecha</th>
                    </tr>
                    <%
                        for (Ave pajaro : listado) {
                    %>

                    <tr>
                        <td><%=pajaro.getAnilla()%></td>
                        <td><%=pajaro.getEspecie()%></td>

                        <td><%=pajaro.getLugar()%></td>
                        <td><%=pajaro.getFecha()%></td>


                    </tr>
                    <%
                        }
} else {
%>
<tr>
    <td>No existen aves que mostrar</td>
</tr>
<%
}
                    %>
                    <input type="hidden" name="op" value="<%=request.getParameter("op")%>" />

                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td class="boton" colspan="4"><input type="submit" name="cancelar" value="Menú incial" /></td>



                    </tr>
                </table>
            </form>




        </div>

    </body>
</html>