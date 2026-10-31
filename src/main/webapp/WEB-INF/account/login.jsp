<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Login screen (CN-01, CN-04, QT-17). Servlet: LoginServlet (/login). Public.
    Request attributes: username (pre-filled from the gym_username cookie or the failed attempt), rememberChecked (boolean), error.
    No reCAPTCHA (FUT-02).
--%>
<c:set var="pageTitle" value="Login" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Login</h1>
<form method="post" action="${pageContext.request.contextPath}/login" onsubmit="return validateLoginForm(this);">
    <label for="username">Username</label>
    <input type="text" id="username" name="username" value="<c:out value='${requestScope.username}'/>" autocomplete="username" maxlength="50" required>

    <label for="password">Password</label>
    <%-- The password is never pre-filled. --%>
    <input type="password" id="password" name="password" autocomplete="current-password" maxlength="64" required>

    <label>
        <input type="checkbox" name="rememberUsername" value="true" ${rememberChecked ? 'checked' : ''}> Remember username
    </label>

    <button type="submit">Login</button>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
