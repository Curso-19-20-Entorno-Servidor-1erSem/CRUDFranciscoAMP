<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Eliminar</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
</head>
<body>
    <div id="principal">
    <h2>Informaci&oacute;n sobre el borrado de registros</h2>
    <%
        String estilo = "normal";
        String mensaje = "Se eliminaron " + request.getAttribute("numero") + " registros";
        if(request.getAttribute("numero") == null){
            estilo = "error";
            mensaje = "No se ha seleccionado ningún registro que eliminar";
        }
    %>
    <h3 class="<%=estilo%>"><%=mensaje%></h3>
    
<br />
<p id="volver"><a href="<%= request.getContextPath()%>">Men&uacute; inicial</a></p>
</div>
</body>
</html>