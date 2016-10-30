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
    
    <h2>Actualizar los datos de un ave</h2>
    <table>
        <form action="conclusion" method="post">
    
    
    <tr>
        <td>Anilla</td>
        <td><%=request.getAttribute("anilla")%></td>
        <input type="hidden" name="anilla" value="<%=request.getAttribute("anilla")%>" />
        
    </tr>
    <tr>
        <td>Especie</td>
        <td><input type="text" name="especie" value="<%=request.getAttribute("especie")%>" /></td>
    </tr>
    <tr>
        <td>Lugar</td>
        <td><input type="text" name="lugar" value="<%=request.getAttribute("lugar")%>" /></td>
    </tr>
    <tr>
        <td>Fecha</td>
        <td><input type="text" name="fecha" value="<%=request.getAttribute("fecha")%>" /></td>
    </tr>
    <tr>
        <td><input type="submit" name="cancelar" value="Cancelar" /></td>
        <td><input type="submit" name="actualizar" value="Actualizar" /></td>
    </tr>
    
        </form>
    </table>
    
        

<br />

</body>
</html>