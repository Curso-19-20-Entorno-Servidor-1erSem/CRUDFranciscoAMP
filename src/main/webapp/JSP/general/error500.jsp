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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/error500.css" />
    </head>
    <body>
        
        <div class="error">
            <a href="<%=request.getContextPath()%>"><img src="<%=request.getContextPath()%>/image/error.png"/></a>
    
            <div class=container>
                <h2>Error interno del servidor.</h2>
                <h3>¡Vaya!Lo sentimos, algo salió mal.</h3>
                <p>Trata de volver a cargar esta página o no dudes en contactar con nosotros si el problema persiste.</p>
                <p>En caso de continuar es que no he sabido como solcionarlo. :( </p>
            </div>
        </div>
    </body>
</html>
