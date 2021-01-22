<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: paulolabisi
  Date: 1/22/21
  Time: 1:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Videos</title>

    <meta charset="utf-8">
    <meta name = "format-detection" content = "telephone=no" />
    <link rel="icon" type="image/png" href="<c:url value="/resources/images/favico.ico"/>">
    <link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico"/>" />
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
    <script>
        $(window).load(function(){
            $().UItoTop({ easingType: 'easeOutQuart' });
        });
    </script>
</head>
<body>


    <!--==============================
              header
=================================-->
    <div class="main">
        <header>
            <div class="container_12">
                <div class="grid_12">
                    <h1 class="logo">
                        <a href="#">${user}'s <span>Video Rental</span>
                        </a>
                    </h1>
                </div>
                <div class="clear"></div>
            </div>

<%--            <section id="stuck_container">--%>
<%--                <!--==============================--%>
<%--                            Stuck menu--%>
<%--                =================================-->--%>
<%--                <div class="container_12">--%>
<%--                    <div class="grid_12">--%>
<%--                        <div class="navigation ">--%>
<%--                            <nav>--%>
<%--                                <ul class="sf-menu">--%>
<%--                                    <li><a href="index.html">Home</a></li>--%>
<%--                                    <li><a href="about.html">About</a></li>--%>
<%--                                    <li><a href="classes.html">Classes</a></li>--%>
<%--                                    <li class="current"><a href="staff.html">Staff</a></li>--%>
<%--                                    <li><a href="contacts.html">Contacts</a></li>--%>
<%--                                </ul>--%>
<%--                            </nav>--%>
<%--                            <div class="clear"></div>--%>
<%--                        </div>--%>
<%--                        <div class="clear"></div>--%>
<%--                    </div>--%>
<%--                    <div class="clear"></div>--%>
<%--                </div>--%>
<%--            </section>--%>

        </header>
        <!--=====================
                  Content
        ======================-->
        <section id="content" class="inset__1"><div class="ic">More Website Templates @ TemplateMonster.com - October 06, 2014!</div>
            <div class="container_12">
                <div class="grid_6">
                    <div class="block-2">
                        <img src="images/page4_img1.jpg" alt="">
                        <div class="extra_wrapper">
                            <h4 class="color1"><a href="/video/${user}/1">Julia Herzigova</a></h4>
                            <p class="color1">Amus at magna non nunc tristique rhoncus. Aliquam nibh antegestas id dictum a, commodo luctus libero. </p>
                            Praesent faucibus malesuucibus. Donec laoreet metus id laoreet
                        </div>
                    </div>
                </div>
                <div class="grid_6">
                    <div class="block-2">
                        <img src="images/page4_img2.jpg" alt="">
                        <div class="extra_wrapper">
                            <h4 class="color1"><a href="#">David Grey</a></h4>
                            <p class="color1">Nomus at magna non nunc tique rhoncus. Aliquam nibh tegestas id dictum a, commodo luctus liberomer. </p>
                            Oraesent faucibus malesuada faucibusnec laoreet metus ider
                        </div>
                    </div>
                </div>
                <div class="clear"></div>
                <div class="grid_6">
                    <div class="block-2">
                        <img src="images/page4_img3.jpg" alt="">
                        <div class="extra_wrapper">
                            <h4 class="color1"><a href="#">Dayle Peters</a></h4>
                            <p class="color1">Memus at magna non nunc tristique rhoncus. Aliquam nibh antegestas id dictum acommodo luctus libero. </p>
                            Praesent faucibus malesuada us. Donec laoreet metus id laoreeto
                        </div>
                    </div>
                </div>
                <div class="grid_6">
                    <div class="block-2">
                        <img src="images/page4_img4.jpg" alt="">
                        <div class="extra_wrapper">
                            <h4 class="color1"><a href="#">Linda Wood</a></h4>
                            <p class="color1">Gemus at magna non nunc tristique rhoncus. Aliquam nibh antegestas id dictum ammodo luctus liberober. </p>
                            Mraesent faucibus malesuadam faucibusonec laoreet metus ide
                        </div>
                    </div>
                </div>
                <div class="clear"></div>
                <div class="grid_6">
                    <div class="block-2">
                        <img src="images/page4_img5.jpg" alt="">
                        <div class="extra_wrapper">
                            <h4 class="color1"><a href="#">Eva Savits</a></h4>
                            <p class="color1">Pomus at magna non nunc tristique rhoncus. Aliquam nibh antegestas id dictum aommodo luctus libero. </p>
                            Meraesent faucibus malesuada faucibusonec laoreet metus ide
                        </div>
                    </div>
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
    <footer id="footer">
        <div class="container_12">
            <div class="grid_12">
                <div class="sub-copy">MOVE &copy; <span id="copyright-year"></span> | <a href="#">Privacy Policy</a> <br> Website designed by <a href="http://www.templatemonster.com/" rel="nofollow">TemplateMonster.com</a></div>
            </div>
            <div class="clear"></div>
        </div>
    </footer>
    <a href="#" id="toTop" class="fa fa-angle-up"></a>

</body>
</html>
