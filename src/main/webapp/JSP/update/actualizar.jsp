<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="es.albarregas.beans.Ave" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Actualizar</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        <div id="principal">
            <h2>Actualizar los datos de un ave</h2>
            <%
            Ave ave = (Ave) request.getAttribute("pajaro");
            %>
            <table>
                <form action="conclusion" method="post">


                    <tr>
                        <td>Anilla</td>
                        <td><%=ave.getAnilla()%></td>
                    <input type="hidden" name="anilla" value="<%=ave.getAnilla()%>" />

                    </tr>
                    <tr>
                        <td>Especie</td>
                        <td><input type="text" name="especie" value="<%=ave.getEspecie()%>" /></td>
                    </tr>
                    <tr>
                        <td>Lugar</td>
                        <td><input type="text" name="lugar" value="<%=ave.getLugar()%>" /></td>
                    </tr>
                    <tr>
                        <td>Fecha</td>
                        <td><input type="text" name="fecha" value="<%=(ave.getFecha()!=null)?ave.getFecha():""%>" /></td>
                    </tr>
                    <tr><td colspan="4"><p class="error"><%=(request.getAttribute("error")!=null)?request.getAttribute("error"):""%></p></td></tr>
                    <tr>
                        <td colspan="2"><input type="submit" name="cancelar" value="Cancelar" class="enlace"></td>
                        <td colspan="2"><input type="submit" name="actualizar" value="Actualizar" class="enlace"></td>
                    </tr>

                </form>
            </table>

        </div>    

        <br />

    </body>
</html>