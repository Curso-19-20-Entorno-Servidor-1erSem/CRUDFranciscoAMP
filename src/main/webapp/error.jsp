<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ERROR</title>
<!--<link rel="stylesheet" type="text/css" href="../CSS/estilo.css" />-->
</head>
<body>
    <h2>Página de error</h2>
    <p><%=request.getAttribute("error") %></p>



<br />
<p><a href="<%= request.getContextPath()%>">Men&uacute; inicial</a></p>
</body>
</html>