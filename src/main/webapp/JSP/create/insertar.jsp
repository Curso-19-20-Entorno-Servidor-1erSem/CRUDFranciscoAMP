<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="es.albarregas.beans.Ave" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Nuevos</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
</head>
<body>
    
    <%
        HttpSession sesion = request.getSession();
        Ave ave = (Ave)sesion.getAttribute("pajaro");
    %>
    <div id="principal">
    <h2>Informaci&oacute;n de nuevas altas</h2>
    <h3>Los datos introducidos por el usuario son:</h3>
    <table>
        
    
    
    <tr>
        <td>Anilla</td>
        
        <td><%=ave.getAnilla()%></td>
        
    </tr>
    <tr>
        <td>Especie</td>
        <td><%=ave.getEspecie()%></td>
    </tr>
    <tr>
        <td>Lugar</td>
        <td><%=ave.getLugar()%></td>
    </tr>
    <tr>
        <td>Fecha</td>
        <td><%=ave.getFecha()%></td>
    </tr>
    <tr>
        
        <td></td>
    </tr>
    <form action="conclusion" method="post">
          
    <tr>
        <td><input type="submit" name="almacenar" value="Almacenar"></td>
        <td><input type="submit" name="cancelar" value="Cancelar"></td>
    </tr>
    </form>   
    </table>
    
    
    </div>    

<br />

</body>
</html>