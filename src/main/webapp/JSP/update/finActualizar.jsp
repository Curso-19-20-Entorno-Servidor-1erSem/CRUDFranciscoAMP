<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <h2>Informaci&oacute;n de la actualizaci&oacute;n</h2>
            <%
                String estilo = null;
                String mensaje = null;
                if (request.getAttribute("sincambios") != null && (Boolean) request.getAttribute("sincambios")) {
                    estilo = "aviso";
                    mensaje = "No se han realizado cambios sobre el registro";
                } else {
                    estilo = "normal";
                    mensaje = "Se actualizó el ave de anilla " + request.getParameter("anilla");
                }
            %>
            <h3 class="<%=estilo%>"><%=mensaje%></h3>

            <br />
            <form action="volver" method="post">
                <p id="volver">
                    <input type="submit" name="menu" value="Inicio" class="enlace"></p>
            </form>
        </div>
    </body>
</html>