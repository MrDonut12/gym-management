<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Login screen (CN-01, CN-37). Servlet: LoginServlet (/login). Public.
    Request attributes: username (pre-filled from the gym_username cookie), rememberChecked (boolean), error.
    No reCAPTCHA widget (CN-40 is Should (B) and is not part of this baseline).
--%>
<c:set var="pageTitle" value="Login" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Login</h1>
<form method="post" action="${pageContext.request.contextPath}/login" onsubmit="return validateLoginForm(this);">
    <label for="username">Username</label>
    <%-- TODO: Pre-fill the value with c:out from requestScope.username. --%>
    <input type="text" id="username" name="username" maxlength="50">

    <label for="password">Password</label>
    <%-- The password must never be pre-filled. --%>
    <input type="password" id="password" name="password">

    <label>
        <%-- TODO: Mark as checked when requestScope.rememberChecked = true. --%>
        <input type="checkbox" name="rememberUsername" value="true"> Remember username
    </label>

    <button type="submit">Login</button>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
