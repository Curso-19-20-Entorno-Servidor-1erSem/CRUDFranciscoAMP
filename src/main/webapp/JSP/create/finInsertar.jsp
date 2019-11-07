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

            Ave ave = (Ave) request.getAttribute("pajaro");
        %>
        <div id="principal">
            <h2>Informaci&oacute;n de nuevas aves</h2>
            <h3>Los datos introducidos en la base de datos son</h3>
            <table>



                <tr>
                    <td>Anilla</td>

                    <td><%=ave.getAnilla()%></td>

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

                <tr>
                    <td colspan="2">
                        <form action="volver" method="post">
                            <input type="submit" name="menu" value="Inicio" class="enlace">
                        </form>
                    </td>
                </tr>
            </table>



        </div>    



    </body>
</html>