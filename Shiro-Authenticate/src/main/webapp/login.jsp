<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet"/>
    <title>login</title>
</head>
<body>
<h1>login page</h1>
<form action="shiro/login" method="post">
    <label>username: <input type="text" name="username"/></label>
    <br>
    <br>
    <label>password: <input type="password" name="password"/></label>
    <br>
    <br>
    <input type="submit" value="提交" />
</form>
</body>
</html>
