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
<p><c:out value="${account.username}"/> — <c:out value="${account.fullName}"/> (<c:out value="${account.status}"/>). Status will not change.</p>
<form method="post" action="${pageContext.request.contextPath}/admin/accounts/reset-password" onsubmit="return validatePasswordForm(this);">
    <input type="hidden" name="id" value="<c:out value='${account.id}'/>">
    <label for="newPassword">New password (8-64 characters)</label>
    <input type="password" id="newPassword" name="newPassword" minlength="8" maxlength="64" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.newPassword}"/></div>
    <label for="confirmPassword">Confirm new password</label>
    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>
    <button type="submit">Reset</button>
    <a href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
