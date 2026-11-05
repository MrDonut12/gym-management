<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Change own password (CN-09, HL-09). Servlet: ChangePasswordServlet (/change-password). Every role.
    Request attributes: fieldErrors (Map), error.
--%>
<c:set var="pageTitle" value="Change password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="row justify-content-center">
<div class="col-12 col-md-8 col-lg-6">
<div class="card">
<div class="card-body p-4">
<h1 class="h3 mb-4">Change password</h1>
<form class="d-grid gap-2" method="post" action="${pageContext.request.contextPath}/change-password" onsubmit="return validatePasswordForm(this);">
    <label class="form-label mt-2 mb-0" for="currentPassword">Current password</label>
    <input class="form-control" type="password" id="currentPassword" name="currentPassword" autocomplete="current-password" maxlength="64" required>
    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.currentPassword}"/></div>

    <label class="form-label mt-2 mb-0" for="newPassword">New password (8-64 characters)</label>
    <input class="form-control" type="password" id="newPassword" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.newPassword}"/></div>

    <label class="form-label mt-2 mb-0" for="confirmPassword">Confirm new password</label>
    <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>

    <button class="btn btn-primary mt-3" type="submit">Save</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/home">Cancel</a>
</form>

</div>
</div>
</div>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
