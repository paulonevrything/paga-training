<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: uduke
  Date: 2017/03/06
  Time: 11:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Video Rental</title>
</head>
<body>
    <h1>Welcome to ${owner} Video Rental</h1>

    <img src="<c:url value="/resources/img/logo.jpg"/>">

    <h1>${user} is logged in currently</h1>
</body>
</html>
