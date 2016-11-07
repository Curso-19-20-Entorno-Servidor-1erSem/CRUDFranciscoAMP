<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Actualizar</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
</head>
<body>
    <div id="principal">
    <h2>Informaci&oacute;n de la actualizaci&oacute;n</h2>
    <%
        String estilo = null;
        String mensaje = null;
        if(request.getParameter("anilla") == null){
            mensaje = "No se ha seleccionado ninguna anilla que actualizar";
            estilo = "error";
        } else if(request.getAttribute("sincambios") != null && (Boolean)request.getAttribute("sincambios")){
            estilo = "aviso";
            mensaje = "No se han realizado cambios sobre el registro";
        } else {
            estilo = "normal";
            mensaje = "Se actualizó el ave de anilla " + request.getParameter("anilla");
        }
    %>
    <h3 class="<%=estilo%>"><%=mensaje%></h3>
    
<br />
<p id="volver"><a href="<%= request.getContextPath()%>">Men&uacute; inicial</a></p>
</div>
</body>
</html>