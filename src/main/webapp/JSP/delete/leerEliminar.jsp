<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eliminar</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        <div id="principal">
            <h2>Elige uno o más aves para eliminar</h2>

            <form action="realiza" method="post">
                <table id="listado">
                    <%
                        List<Ave> listado = null;
                        listado = new ArrayList();
                        listado = (ArrayList<Ave>) request.getAttribute("lista");
                    %>
                    <tr>
                        <th style="width: 5%;">Elige</th>
                        <th>Especie</th>

                    </tr>
                    <%
                        for (Ave pajaro : listado) {
                    %>

                    <tr>

                        <td style="text-align: center;"><input type="checkbox" name="registro" value="<%=pajaro.getAnilla()%>"/></td>

                        <td style="padding-left: 20%;"><%=pajaro.getEspecie()%></td>


                    </tr>
                    <%
                        }
                    %>
                    <input type="hidden" name="op" value="delete" />

                    <tr><td colspan="2"><p class="error"><%=(request.getAttribute("errorDelete") != null) ? (String) request.getAttribute("errorDelete") : " "%></p></td></tr>
                    <tr>
                        <td class="boton"><input type="submit" name="realizar" value="Realizar" class="enlace"></td>

                        <td class="boton" style="text-align: right;"><input type="submit" name="cancelar" value="Cancelar" class="enlace"></td>

                    </tr>
                </table>
            </form>



        </div>
        <br />

    </body>
</html>