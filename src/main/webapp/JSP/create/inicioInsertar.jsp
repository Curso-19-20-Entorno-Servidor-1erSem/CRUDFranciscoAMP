<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="es.albarregas.beans.Ave, java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevos</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
    </head>
    <body>
        <div id="principal">
            <h2>A&ntilde;adir nueva aves</h2>
            <table>
                <form action="conclusion" method="post">


                    <tr>
                        <td>Anilla</td>

                        <td><input type="text" name="anilla" value="" /></td>

                    </tr>
                    <tr>
                        <td>Especie</td>
                        <%
                            Ave ave = (Ave) request.getAttribute("pajaro");
                            String valor = (ave != null && ave.getEspecie() != null) ? ave.getEspecie() : "";


                        %> 
                        <td><input type="text" name="especie" value="<%=valor%>" /></td>
                    </tr>
                    <tr>
                        <td>Lugar</td>
                        <%
                            valor = (ave != null && ave.getLugar() != null) ? ave.getLugar() : "";
                        %> 
                        <td><input type="text" name="lugar" value="<%=valor%>" /></td>
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
                            valor = (ave != null) ? (String) request.getAttribute("error") : " ";
                        %> 
                        <td colspan="2"><p class="error"><%=valor%></p></td>
                    </tr>

                    <tr>
                        <td><input type="submit" name="cancelar" value="Cancelar" /></td>
                        <td><input type="submit" name="crear" value="A&ntilde;adir" /></td>
                    </tr>

                </form>
            </table>
        </div>


       

    </body>
</html>