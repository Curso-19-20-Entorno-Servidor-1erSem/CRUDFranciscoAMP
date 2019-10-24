<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="es.albarregas.beans.Ave, java.util.Date" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevos</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        <div id="principal">
            <h2>A&ntilde;adir nuevo p&aacute;jaro</h2>
            <table>
                <form action="realiza" method="post">


                    <tr>
                        <td>Anilla</td>
                        <%
                            
                            Ave ave = (Ave) request.getAttribute("pajaro");
                            String valor = (ave != null && ave.getAnilla() != null) ? ave.getAnilla() : "";
                        %>
                            
                        <td><input type="text" name="anilla" value="<%=valor%>" maxlength="6" /></td>

                    </tr>
                    <tr>
                        <td>Especie</td>
                        <%
                            
                            valor = (ave != null && ave.getEspecie() != null) ? ave.getEspecie() : "";


                        %> 
                        <td><input type="text" name="especie" value="<%=valor%>" maxlength="20"/></td>
                    </tr>
                    <tr>
                        <td>Lugar</td>
                        <%
                            valor = (ave != null && ave.getLugar() != null) ? ave.getLugar() : "";
                        %> 
                        <td><input type="text" name="lugar" value="<%=valor%>" maxlength="30" /></td>
                    </tr>
                    <tr>
                        <td>Fecha</td>
                        <%

                            valor = (ave != null && ave.getFecha() != null) ? ave.getFecha().toString() : "";
                        %> 
                        <td><input type="text" name="fecha" value ="<%=valor%>" /></td>
                    </tr>

                    <tr>
                        <%
                            valor = (request.getAttribute("error") != null) ? (String) request.getAttribute("error") : " ";
                        %> 
                        <td colspan="2"><p class="error"><%=(request.getAttribute("error") != null) ? (String) request.getAttribute("error") : " "%></p></td>
                    </tr>
                    <input type="hidden" name="op" value="create" />
                    <tr>
                        <td><input type="submit" name="cancelar" value="Cancelar" class="enlace" /></td>
                        <td><input type="submit" name="crear" value="A&ntilde;adir" class="enlace" /></td>
                    </tr>

                </form>
            </table>
        </div>


       

    </body>
</html>