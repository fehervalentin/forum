<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="Forum/forum/registration" method="POST">
            Nickname: <input type="text" name="nickname">
            <br />
            Password: <input type="password" name="password" />
            <br />
            Email: <input type="text" name="email" />
            <br />
            <input type="submit" value="Submit" />
            <br />
        </form>
    </body>
</html>
