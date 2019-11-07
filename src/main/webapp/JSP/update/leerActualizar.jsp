<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
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
            <h2>Elige un p&aacute;jaro para cambiar sus datos</h2>

            <form action="realiza" method="post">
                <table id="listado">
                    <%
                        /*
                        * Se obtienen los datos que vienen en el atributo lista, es decir, el contenido de la tabla aves
                        */
                        List<Ave> listado = new ArrayList<>();
                        
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

                        <td style="text-align: center;"><input type="radio" name="registro" value="<%=pajaro.getAnilla()%>"/></td>

                        <td style="padding-left: 20%;"><%=pajaro.getEspecie()%></td>


                    </tr>
                    <%
                        }
                    %>
                    <input type="hidden" name="op" value="update" />
                    <tr>
                    <td colspan="2"><p class="error"><%=(request.getAttribute("errorUpdate") != null) ? (String) request.getAttribute("errorUpdate") : " "%></p></td>
                    </tr>
                    <tr>
                        <td class="boton"><input type="submit" name="realizar" value="Realizar" class="enlace"></td>
                        <td class="boton" style="text-align: right;"><input type="submit" name="cancelar" value="Cancelar" class="enlace"></td>

                    </tr>
                </table>
            </form>

        </div>  


        

    </body>
</html>