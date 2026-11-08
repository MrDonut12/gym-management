<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Staff account list (FR-05), the ADMIN home page. Servlet: AccountListServlet (/admin/accounts). ADMIN only.
    Request attributes: accounts (List<Account>), keyword, role, status.
--%>
<c:set var="pageTitle" value="Staff accounts" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">Staff accounts</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/accounts/create">Create account</a>
</div>

<form class="row g-3 align-items-end mb-4" method="get" action="${pageContext.request.contextPath}/admin/accounts">
    <div class="col-12 col-md-5">
        <label class="form-label" for="keyword">Search accounts</label>
        <input class="form-control" id="keyword" type="text" name="keyword" aria-label="Username, full name or phone" value="<c:out value='${keyword}'/>" maxlength="100" placeholder="Username, full name or phone">
        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.keyword}"/></div>
    </div>
    <div class="col-6 col-md-3">
        <label class="form-label" for="filterRole">Role</label>
        <select class="form-select" id="filterRole" name="role" aria-label="Role">
            <option value="">All roles</option>
            <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
            <option value="MANAGER" ${role == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
            <option value="RECEPTIONIST" ${role == 'RECEPTIONIST' ? 'selected' : ''}>RECEPTIONIST</option>
        </select>
    </div>
    <div class="col-6 col-md-2">
        <label class="form-label" for="filterStatus">Status</label>
        <select class="form-select" id="filterStatus" name="status" aria-label="Status">
            <option value="">All statuses</option>
            <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
            <option value="LOCKED" ${status == 'LOCKED' ? 'selected' : ''}>LOCKED</option>
        </select>
    </div>
    <div class="col-12 col-md-2 d-grid">
        <button class="btn btn-outline-primary" type="submit">Filter</button>
    </div>
</form>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Staff accounts matching the selected filters</caption>
        <thead class="table-light">
            <tr><th scope="col">Username</th><th scope="col">Full name</th><th scope="col">Phone</th><th scope="col">Role</th><th scope="col">Status</th><th scope="col">Created at</th><th scope="col">Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="account" items="${accounts}">
                <tr>
                    <td class="fw-semibold"><c:out value="${account.username}"/></td>
                    <td><c:out value="${account.fullName}"/></td>
                    <td><c:out value="${account.phone}"/></td>
                    <td><c:out value="${account.role}"/></td>
                    <td><span class="badge ${account.status == 'ACTIVE' ? 'text-bg-success' : 'text-bg-secondary'}"><c:out value="${account.status}"/></span></td>
                    <td class="text-nowrap"><c:if test="${not empty account.createdAt}"><c:out value="${DateTimeUtil.format(account.createdAt)}"/></c:if></td>
                    <td>
                        <div class="d-flex flex-wrap gap-2">
                            <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/accounts/edit?id=${account.id}">Edit</a>
                            <c:if test="${account.id != sessionScope.sessionUser.id}">
                                <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/admin/accounts/reset-password?id=${account.id}">Reset password</a>
                                <form class="m-0" method="post" action="${pageContext.request.contextPath}/admin/accounts/lock" onsubmit="return confirm('Change this account status?');">
                                    <input type="hidden" name="id" value="${account.id}">
                                    <input type="hidden" name="action" value="${account.status == 'ACTIVE' ? 'lock' : 'unlock'}">
                                    <button class="btn btn-sm ${account.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'}" type="submit">${account.status == 'ACTIVE' ? 'Lock' : 'Unlock'}</button>
                                </form>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty accounts}"><tr><td colspan="7" class="text-center text-body-secondary py-5">No accounts found.</td></tr></c:if>
            </tbody>
        </table>
    </div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
