<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Change own password (CN-09, HL-09). Servlet: ChangePasswordServlet (/change-password). Every role.
    Request attributes: fieldErrors (Map), error.
--%>
<c:set var="pageTitle" value="Change password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Change password</h1>
<form method="post" action="${pageContext.request.contextPath}/change-password" onsubmit="return validatePasswordForm(this);">
    <label for="currentPassword">Current password</label>
    <input type="password" id="currentPassword" name="currentPassword" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.currentPassword}"/></div>

    <label for="newPassword">New password (8-64 characters)</label>
    <input type="password" id="newPassword" name="newPassword" minlength="8" maxlength="64" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.newPassword}"/></div>

    <label for="confirmPassword">Confirm new password</label>
    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>

    <button type="submit">Save</button>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
