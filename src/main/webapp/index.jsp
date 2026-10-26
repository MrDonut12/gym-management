<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%--
    Entry point: send the user to /home, which redirects to the home page of the user's role (section 2.4).
    AuthFilter redirects to /login first when there is no session.
--%>
<c:redirect url="/home"/>
