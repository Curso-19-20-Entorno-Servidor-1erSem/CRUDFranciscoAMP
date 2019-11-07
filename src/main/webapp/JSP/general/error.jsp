<%-- 
    Document   : error500
    Created on : 30-oct-2016, 11:31:07
    Author     : Jesus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
        <%@include file="/INC/metas.inc"%>
    </head>
    <body>
        
        <div class="error">
            <p><%=request.getAttribute("error")%></p>
            <form action="volver" method="post">
                <input type="submit" name="menu" value="Inicio" class="enlace">
            </form>
        </div>
    </body>
</html>
