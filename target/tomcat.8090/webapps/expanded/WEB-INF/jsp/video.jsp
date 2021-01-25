<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: paulolabisi
  Date: 1/22/21
  Time: 3:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Renting ${video.title}</title>

    <meta charset="utf-8">
    <meta name = "format-detection" content = "telephone=no" />
    <link rel="icon" type="image/png" href="<c:url value="/resources/images/favico.ico"/>">
    <link rel="shortcut icon" href="<c:url value="/resources/images/favico.ico"/>" />
    <link rel="stylesheet" href="<c:url value="/resources/css/contact-form.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>">
    <script src="<c:url value="/resources/js/jquery.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery-migrate-1.1.1.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery.easing.1.3.js"/>"></script>
    <script src="<c:url value="/resources/js/script.js"/>"></script>
    <script src="<c:url value="/resources/js/superfish.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery.equalheights.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery.mobilemenu.js"/>"></script>
    <script src="<c:url value="/resources/js/tmStickUp.js"/>"></script>
    <script src="<c:url value="/resources/js/jquery.ui.totop.js"/>"></script>
    <script src="<c:url value="/resources/js/TMForm.js"/>"></script>
    <script src="<c:url value="/resources/js/modal.js"/>"></script>
    <script>
        $(window).load(function(){
            $().UItoTop({ easingType: 'easeOutQuart' });
        });
    </script>
</head>
<body>

<div class="main">
    <header>
        <div class="container_12">
            <div class="grid_12">
                <h1 class="logo">
                    <a href="#">${user}'s <span>Video Rental</span>
                    </a>
                </h1>
            </div>
            <br>
        </div>
    </header>
    <!--=====================
              Content
    ======================-->
    <section id="content"><div class="ic">More Website Templates @ TemplateMonster.com - October 06, 2014!</div>
        <div class="container_12">
            <div class="grid_12">
                <h2 class="inset__1">${video.title}</h2>
                <div class="map">
                    <figure class="">
                        <img src="${video.imgUrl}" alt="">
                    </figure>
                </div>
            </div>
            <div class="grid_5">
                <h4 class="color1 inset__1">Rental Information</h4>
                <p><strong>NUMBER OF DAYS: </strong> ${rentalDays}</p>
                <p><strong>PRICE: </strong> ${price}</p>
            </div>

            <div class="grid_6 prefix_1">
                <h4 class="color1 inset__1">Payment Information</h4>
                <p>Please pay to the following account details:</p>
                <p><strong>ACCOUNT NUMBER: </strong>1234567890</p>
                <p><strong>BANK: </strong>PAGATECH LIMITED</p>
            </div>
            <div class="clear"></div>
        </div>
    </section>
    <!--==============================
                  footer_top
    =================================-->
    <div class="footer-top">
        <div class="container_12">
            <div class="grid_12">
                <div class="sep-1"></div>
            </div>
            <div class="grid_4">
                <address class="address-1">
                    <div class="fa fa-home"></div>176, Herbert Macaulay Way, 3rd Floor, Yaba, Lagos,<br>LG 100001
                </address>
            </div>
            <div class="grid_3">
                <a href="#" class="mail-1"><span class="fa fa-envelope"></span>service@mypaga.com</a>
            </div>
            <div class="grid_4 fright">
                <div class="socials">
                    <a href="http://www.facebook.com/mypaga">facebook</a>
                    <a href="http://twitter.com/@mypaga">twitter</a>
                    <a href="http://instagram.com/my_paga">instagram</a>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
    <!--==============================
                  footer
    =================================-->
</div>
<a href="#" id="toTop" class="fa fa-angle-up"></a>

</body>
</html>
