<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Staff account list (CN-05), the ADMIN home page. Servlet: AccountListServlet (/admin/accounts). ADMIN only.
    Request attributes: accounts (List<Account>), keyword, role, status.
--%>
<c:set var="pageTitle" value="Staff accounts" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Staff accounts</h1>
<a href="${pageContext.request.contextPath}/admin/accounts/create">Create account</a>

<form method="get" action="${pageContext.request.contextPath}/admin/accounts">
    <input type="text" name="keyword" aria-label="Username or full name" value="<c:out value='${keyword}'/>" maxlength="100" placeholder="Username or full name">
    <select name="role" aria-label="Role">
        <option value="">All roles</option>
        <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
        <option value="MANAGER" ${role == 'MANAGER' ? 'selected' : ''}>MANAGER</option>
        <option value="RECEPTIONIST" ${role == 'RECEPTIONIST' ? 'selected' : ''}>RECEPTIONIST</option>
    </select>
    <select name="status" aria-label="Status">
        <option value="">All statuses</option>
        <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
        <option value="LOCKED" ${status == 'LOCKED' ? 'selected' : ''}>LOCKED</option>
    </select>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.keyword}"/></div>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead>
    <tr><th>Username</th><th>Full name</th><th>Phone</th><th>Role</th><th>Status</th><th>Created at</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <c:forEach var="account" items="${accounts}">
        <tr>
            <td><c:out value="${account.username}"/></td>
            <td><c:out value="${account.fullName}"/></td>
            <td><c:out value="${account.phone}"/></td>
            <td><c:out value="${account.role}"/></td>
            <td><c:out value="${account.status}"/></td>
            <td><c:out value="${account.createdAt}"/></td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/accounts/edit?id=${account.id}">Edit</a>
                <c:if test="${account.id != sessionScope.sessionUser.id}">
                    <a href="${pageContext.request.contextPath}/admin/accounts/reset-password?id=${account.id}">Reset password</a>
                    <form method="post" action="${pageContext.request.contextPath}/admin/accounts/lock" onsubmit="return confirm('Change this account status?');">
                        <input type="hidden" name="id" value="${account.id}">
                        <input type="hidden" name="action" value="${account.status == 'ACTIVE' ? 'lock' : 'unlock'}">
                        <button type="submit">${account.status == 'ACTIVE' ? 'Lock' : 'Unlock'}</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty accounts}"><tr><td colspan="7">No accounts found.</td></tr></c:if>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
