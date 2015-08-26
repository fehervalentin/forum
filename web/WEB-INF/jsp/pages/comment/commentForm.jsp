<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% out.println("<form action=\"/Forum/home/" + String.valueOf(request.getAttribute("id")) + "\" method=\"POST\">"); %>
    Tartalom: <input type="text" name="content" />
    <br />
    <input type="submit" value="Submit" />
    <br />
<%out.println("</form>");%>
