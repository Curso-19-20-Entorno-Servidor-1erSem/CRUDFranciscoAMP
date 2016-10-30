<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="es.albarregas.beans.Ave"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resultados</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/CSS/estilo.css" />
</head>
<body>
    
    <h2>Listado de todas las aves de la base de datos</h2>
    
        <form action="realiza" method="post">
            <table id="listado">
     <%
    List<Ave> listado = null;
    listado = new ArrayList();
    listado = (ArrayList<Ave>)request.getAttribute("lista");
    for(Ave pajaro : listado){
    %>
    
    <tr>
        <%
        if(request.getParameter("op").equals("elimina")){
        %>       
        <td><input type="checkbox" name="registro" value="<%=pajaro.getAnilla()%>"/></td>
        <%
            } else if (request.getParameter("op").equals("actualiza")){
        %>
        <td><input type="radio" name="registro" value="<%=pajaro.getAnilla()%>"/></td>
        <%
            }
        %>
        <td><%=pajaro.getEspecie()%></td>
        <%
        if(request.getParameter("op").equals("lee")){
        %>       
        <td><%=pajaro.getLugar()%></td>
        <td><%=pajaro.getFecha()%></td>
        <%
            } 
        %>
        
    </tr>
    <%
    }
    %>
    <input type="hidden" name="op" value="<%=request.getParameter("op")%>" />
    <%
        String valor = "Realizar";
        if(request.getParameter("op").equals("lee")){
            valor = "Menú";
        }
    %>
    
    </table>
    <p class="boton"><input type="submit" name="realizar" value="<%=valor%>" /></p>
        </form>
    
    
        

<br />

</body>
</html>