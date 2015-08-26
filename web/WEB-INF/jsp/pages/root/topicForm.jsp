<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration Page</title>
    </head>
    <body>
        <form action="/Forum/home/" method="POST">
            Készítő: <input type="text" name="nickname" />
            <br />
            Title <input type="text" name="title" />
            <br />
            Description <input type="text" name="descripton" />
            <br />
            <input type="submit" value="Submit" />
            <br />
        </form>
    </body>
</html>
