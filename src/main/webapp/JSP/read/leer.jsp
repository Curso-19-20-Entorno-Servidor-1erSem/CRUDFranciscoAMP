<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listado</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        <div id="principal">
            <h2>Listado de todas las aves</h2>

            <form action="realiza" method="post">
                <%
                        List<Ave> listado = new ArrayList<>();
                        
                        listado = (ArrayList<Ave>) request.getAttribute("lista");
                        
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
                    %>

                    <input type="hidden" name="op" value="read" />

                    <tr><td colspan="4">&nbsp;</td></tr>
                    <tr>
                        <td class="boton" colspan="4" style="text-align: center;"><input type="submit" name="inicio" value="Inicio" class="enlace"></td>



                    </tr>
                </table>
            </form>




        </div>

    </body>
</html>