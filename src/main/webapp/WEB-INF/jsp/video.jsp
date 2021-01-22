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
    <title>Title ${VideoTitle}</title>

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
    </header>
    <!--=====================
              Content
    ======================-->
    <section id="content"><div class="ic">More Website Templates @ TemplateMonster.com - October 06, 2014!</div>
        <div class="container_12">
            <div class="grid_12">
                <h2 class="inset__1">Movie Title</h2>
                <div class="map">
                    <figure class="">
                        <iframe src="https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d24214.807650104907!2d-73.94846048422478!3d40.65521573400813!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e0!3m2!1sen!2sus!4v1395650655094" style="border:0"></iframe>
                    </figure>
                </div>
            </div>
            <div class="grid_5">
                <h4 class="color1 inset__1">Contact Info</h4>
                <p>Lorem ipsum dolor sit amet, consecteturpiscinger elit um dolor sit amet, consecteturpiscing. </p>
                <p>24/7 support is available for all <a class="color1" href="http://www.templatemonster.com/ rel="nofollow">premium templates</a>.</p>
                <p><a class="color1" href="http://www.templatetuning.com/" rel="nofollow">TemplateTuning</a> will assist with customization of the chosen templates.</p>
                The Company Name Inc. <br>
                9870 St Vincent Place, <br>
                Glasgow, DC 45 Fr 45. <br>
                Telephone: +1 800 603 6035 <br>
                FAX: +1 800 889 9898 <br>
                E-mail: <a href="mailto:mail@demolink.org">mail@demolink.org</a>
            </div>
            <div class="grid_6 prefix_1">
                <h4 class="color1 inset__1">Contact Form</h4>
                <form id="contact-form">
                    <div class="contact-form-loader"></div>
                    <fieldset>
                        <label class="name">
                            <input type="text" name="name" placeholder="Name:" value="" data-constraints="@Required @JustLetters"  />
                            <span class="empty-message">*This field is required.</span>
                            <span class="error-message">*This is not a valid name.</span>
                        </label>
                        <label class="email">
                            <input type="text" name="email" placeholder="E-mail:" value="" data-constraints="@Required @Email" />
                            <span class="empty-message">*This field is required.</span>
                            <span class="error-message">*This is not a valid email.</span>
                        </label>
                        <label class="phone">
                            <input type="text" name="phone" placeholder="Phone:" value="" data-constraints="@Required @JustNumbers" />
                            <span class="empty-message">*This field is required.</span>
                            <span class="error-message">*This is not a valid phone.</span>
                        </label>
                        <label class="message">
                            <textarea name="message" placeholder="Message:" data-constraints='@Required @Length(min=20,max=999999)'></textarea>
                            <span class="empty-message">*This field is required.</span>
                            <span class="error-message">*The message is too short.</span>
                        </label>
                        <div class="ta__right">
                            <a href="#" class="link-1" data-type="reset">clear</a>
                            <a href="#" class="link-1" data-type="submit">send</a>
                        </div>
                    </fieldset>
                    <div class="modal fade response-message">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title">Modal title</h4>
                                </div>
                                <div class="modal-body">
                                    You message has been sent! We will be in touch soon.
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
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
