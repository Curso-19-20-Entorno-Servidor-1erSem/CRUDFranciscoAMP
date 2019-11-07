<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="es.albarregas.beans.Ave" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevos</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>

        <%
            // HttpSession sesion = request.getSession();
            Ave ave = (Ave) request.getAttribute("pajaro");
        %>
        <div id="principal">
            <h2>Informaci&oacute;n de nuevas altas</h2>
            <h3>Los datos introducidos por el usuario son:</h3>
            <table>



                <tr>
                    <td>Anilla</td>

                    <td><%=ave.getAnilla()%>  <%=(request.getAttribute("error") != null) ? request.getAttribute("error") : ""%></td>

                </tr>
                <tr>
                    <td>Especie</td>
                    <td><%=ave.getEspecie()%></td>
                </tr>
                <tr>
                    <td>Lugar</td>
                    <td><%=ave.getLugar()%></td>
                </tr>
                <tr>
                    <td>Fecha</td>
                    <td><%=ave.getFecha()%></td>
                </tr>
                <tr>

                    <td></td>
                </tr>
                <form action="conclusion" method="post">


                    <%
                        if (request.getAttribute("error") == null) {
                    %>
                    <td><input type="submit" name="almacenar" value="Almacenar" class="enlace"></td>
                        <%
                            }
                        %>
                    <td><input type="submit" name="cancelar" value="Cancelar" class="enlace"></td>

                </form>   
            </table>


        </div>    

        <br />

    </body>
</html>