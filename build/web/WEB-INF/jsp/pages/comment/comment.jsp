<%@page import="forum.entity.Comment"%>
<%@page import="forum.entity.Topic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>lol</title>
    </head>
    <body>
        <%
            Topic t = (Topic)request.getAttribute("topic");
            if (t.getComments().size() < 1) {%>
                <h1>Nincsenek még tárolt hozzászólások!</h1>
           <%}
            else {
                for (Comment c : t.getComments()) {
                    out.println(c.toString());
                    out.println("<br>");
                }
            }
        %>
        <br>
        <h1>Szólj hozzá te is!</h1>
        <br>
        <br>
        <jsp:include page="commentForm.jsp"/>
    </body>
</html>
