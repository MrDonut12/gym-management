<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    ADMIN sets a new password for another account (FR-10, VR-09). Servlet: AccountResetPasswordServlet (/admin/accounts/reset-password).
    Request attributes: account (target Account), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Reset password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
        <div class="card">
            <div class="card-body p-4">
                <h1 class="h3 mb-4">Reset password</h1>
                <p><c:out value="${account.username}"/> — <c:out value="${account.fullName}"/> (<c:out value="${account.status}"/>). Status will not change.</p>
                <form class="d-grid gap-2" method="post" action="${pageContext.request.contextPath}/admin/accounts/reset-password" onsubmit="return validatePasswordForm(this);">
                    <input type="hidden" name="id" value="<c:out value='${account.id}'/>">
                    <label class="form-label mt-2 mb-0" for="newPassword">New password (8-64 characters)</label>
                    <input class="form-control" type="password" id="newPassword" name="newPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.newPassword}"/></div>
                    <label class="form-label mt-2 mb-0" for="confirmPassword">Confirm new password</label>
                    <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" autocomplete="new-password" minlength="8" maxlength="64" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>
                    <button class="btn btn-primary mt-3" type="submit">Reset</button>
                    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
                </form>

            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
