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
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        <div id="principal">
            <h2>Aves que se van a eliminar</h2>
            <table>
                <tr>

                    <th>Especie</th>
                    <th>Lugar</th>
                    <th>Fecha</th>
                </tr>
                <form action="conclusion" method="post">
                    <%
                        List<Ave> listado = new ArrayList<>();

                        listado = (ArrayList<Ave>) request.getAttribute("lista");
                        ArrayList<String> listaAnillas = new ArrayList<>();
                        for (Ave pajaro : listado) {
                            listaAnillas.add(pajaro.getAnilla());
                    %>

                    <tr>
                    <input type="hidden" name="anilla" value="<%=pajaro.getAnilla()%>" />

                    <td><%=pajaro.getEspecie()%></td>

                    <td><%=pajaro.getLugar()%></td>
                    <td><%=pajaro.getFecha()%></td>


                    </tr>
                    <tr><td colspan="2">&nbsp;</td></tr>
                    <%
                        }

                    %>

                    <td><input type="submit" name="cancelar" value="Cancelar" class="enlace"></td>
                    <td><input type="submit" name="eliminar" value="Eliminar" class="enlace"></td>
                </form>
            </table>

        </div>   

        <br />

    </body>
</html>