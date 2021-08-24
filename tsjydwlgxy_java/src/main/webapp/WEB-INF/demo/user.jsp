<%@ page import="com.tsjy.dwlgxy.bean.*"%>
<%
UserAdmin user = (UserAdmin) request.getAttribute("user");
%>
<html>
<head>
    <title>Hello World - JSP</title>
</head>
<body>
    <h1>Hello <%= user.name %>!</h1>
    <p>School Name:
    <span style="color:red">
        <%= user.name %>
    </span>
    </p>
    <p>School Address:
    <span style="color:red">
        <%= user.account %>
    </span>
    </p>
</body>
</html>
