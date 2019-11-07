<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <h2>Informaci&oacute;n sobre el borrado de registros</h2>
            <%
                String estilo = "normal";
                String mensaje = "Se eliminaron " + request.getAttribute("numero") + " registros";
                if (request.getAttribute("numero") == null) {
                    estilo = "error";
                    mensaje = "No se ha seleccionado ningún registro que eliminar";
                }
            %>
            <h3 class="<%=estilo%>"><%=mensaje%></h3>

            <br />
<!--            <p id="volver"><a href="<%= request.getContextPath()%>">Men&uacute; inicial</a></p>-->
            <form action="volver" method="post">
                <p id="volver">
                    <input type="submit" name="menu" value="Inicio" class="enlace">
                </p>
            </form>
        </div>
    </body>
</html>