<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Administrator resets another account's password (CN-36). Servlet: AccountResetPasswordServlet (/accounts/reset-password).
    Request attributes: account (target Account), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Reset password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Reset password</h1>
<%-- TODO: Show the target account's username/full name (c:out). --%>
<form method="post" action="${pageContext.request.contextPath}/accounts/reset-password" onsubmit="return validatePasswordForm(this);">
    <input type="hidden" name="id" value="">
    <label for="newPassword">New password</label>
    <input type="password" id="newPassword" name="newPassword">
    <label for="confirmPassword">Confirm new password</label>
    <input type="password" id="confirmPassword" name="confirmPassword">
    <%-- TODO: Show fieldErrors. --%>
    <button type="submit">Reset</button>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
