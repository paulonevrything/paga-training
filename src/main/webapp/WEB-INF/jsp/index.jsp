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


    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="<c:url value="/resources/images/icons/favicon.ico"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/bootstrap/css/bootstrap.min.css"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/animate/animate.css"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/css-hamburgers/hamburgers.min.css"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/vendor/select2/select2.min.css"/>">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/util.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css"/>">
</head>
<body>
<%--    <h1>Welcome to ${owner} Video Rental</h1>--%>

<%--    <img src="<c:url value="/resources/img/logo.jpg"/>">--%>

<%--    <h1>${user} is logged in currently</h1>--%>


    <div class="limiter">
        <div class="container-login100">
            <div class="wrap-login100">
                <div class="login100-pic js-tilt" data-tilt>
                    <img src="<c:url value="/resources/images/img-01.png"/>" alt="IMG">
                </div>

                <form id="videos" action="/videos" method="post" class="login100-form validate-form">
					<span class="login100-form-title">
						Member Login
					</span>

                    <div class="wrap-input100 validate-input" data-validate = "Valid name is required: John Doe">
                        <input class="input100" type="text" name="username" placeholder="Full Name">
                        <span class="focus-input100"></span>
                        <span class="symbol-input100">
							<i class="fa fa-envelope" aria-hidden="true"></i>
						</span>
                    </div>

<%--                    <div class="wrap-input100 validate-input" data-validate = "Password is required">--%>
<%--                        <input class="input100" type="password" name="pass" placeholder="Password">--%>
<%--                        <span class="focus-input100"></span>--%>
<%--                        <span class="symbol-input100">--%>
<%--							<i class="fa fa-lock" aria-hidden="true"></i>--%>
<%--						</span>--%>
<%--                    </div>--%>

                    <div class="container-login100-form-btn">
                        <button class="login100-form-btn">
                            Login
                        </button>
                    </div>

<%--                    <div class="text-center p-t-12">--%>
<%--						<span class="txt1">--%>
<%--							Forgot--%>
<%--						</span>--%>
<%--                        <a class="txt2" href="#">--%>
<%--                            Username / Password?--%>
<%--                        </a>--%>
<%--                    </div>--%>

<%--                    <div class="text-center p-t-136">--%>
<%--                        <a class="txt2" href="#">--%>
<%--                            Create your Account--%>
<%--                            <i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>--%>
<%--                        </a>--%>
<%--                    </div>--%>
                </form>
            </div>
        </div>
    </div>


    <!--===============================================================================================-->
    <script src="<c:url value="/resources/vendor/jquery/jquery-3.2.1.min.js"/>"></script>
    <!--===============================================================================================-->
    <script src="<c:url value="/resources/vendor/bootstrap/js/popper.js"/>"></script>
    <script src="<c:url value="/resources/vendor/bootstrap/js/bootstrap.min.js"/>"></script>
    <!--===============================================================================================-->
    <script src="<c:url value="/resources/vendor/select2/select2.min.js"/>"></script>
    <!--===============================================================================================-->
    <script src="<c:url value="/resources/vendor/tilt/tilt.jquery.min.js"/>"></script>
    <script >
        $('.js-tilt').tilt({
            scale: 1.1
        })
    </script>
    <!--===============================================================================================-->
    <script src="<c:url value="/resources/js/main.js"/>"></script>
</body>
</html>
