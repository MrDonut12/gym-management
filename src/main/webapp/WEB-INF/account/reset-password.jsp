<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    ADMIN sets a new password for another account (CN-10, HL-09). Servlet: AccountResetPasswordServlet (/admin/accounts/reset-password).
    Request attributes: account (target Account), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Reset password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Reset password</h1>
<%-- TODO: Show the target account's username, full name and status (c:out); the lock status is not changed by this action. --%>
<form method="post" action="${pageContext.request.contextPath}/admin/accounts/reset-password" onsubmit="return validatePasswordForm(this);">
    <input type="hidden" name="id" value="">
    <label for="newPassword">New password (8-64 characters)</label>
    <input type="password" id="newPassword" name="newPassword" minlength="8" maxlength="64">
    <label for="confirmPassword">Confirm new password</label>
    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64">
    <%-- TODO: Show fieldErrors. --%>
    <button type="submit">Reset</button>
    <a href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
