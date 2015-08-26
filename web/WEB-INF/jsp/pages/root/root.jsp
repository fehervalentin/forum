<%@page import="java.util.List"%>
<%@page import="forum.entity.Topic"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forum</title>
    </head>
    <body>
        <%
            List<Topic> topics = (List<Topic>)request.getAttribute("topics");
            if (topics.size() < 1) {%>
                <h1>Nincsenek még tárolt topikok!</h1>
            <%}
            else {
                for (int i = 0; i < topics.size(); ++i) {
                    Topic topic = topics.get(i);
                    out.println(topic.toString());%>
                    <br>
                    <% out.println("<a href=\"/Forum/home/" + topics.get(i).getId() + "\">" + topic.getTitle() + "</a>"); %>
                    <br>
                    <br>
                <%}
            }
        %>
        <jsp:include page="topicForm.jsp"/>
    </body>
</html>
