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
    <input type="text" name="keyword" maxlength="100" placeholder="Username or full name">
    <select name="role">
        <option value="">All roles</option>
        <option value="ADMIN">ADMIN</option>
        <option value="MANAGER">MANAGER</option>
        <option value="RECEPTIONIST">RECEPTIONIST</option>
    </select>
    <select name="status">
        <option value="">All statuses</option>
        <option value="ACTIVE">ACTIVE</option>
        <option value="LOCKED">LOCKED</option>
    </select>
    <%-- TODO: Re-select the current filter values. --%>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead>
    <tr><th>Username</th><th>Full name</th><th>Phone</th><th>Role</th><th>Status</th><th>Created at</th><th>Actions</th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over accounts; c:out for every value; never show the password hash.
         Actions: Edit (/admin/accounts/edit?id=), Reset password (/admin/accounts/reset-password?id=),
         POST form to /admin/accounts/lock (id, action=lock|unlock) with a confirmation dialog.
         Hide Lock and Reset password on the current user's own row (QT-14; the backend refuses anyway). --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
