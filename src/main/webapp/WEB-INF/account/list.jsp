<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    List of accounts within the management scope (CN-04, CN-35). Servlet: AccountListServlet (/accounts).
    Roles: Administrator (all), Manager (Receptionist/Accountant).
    Request attributes: accounts (List<Account>), keyword, role, status, includeDeleted, roles (filter options).
--%>
<c:set var="pageTitle" value="Staff accounts" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Staff accounts</h1>
<a href="${pageContext.request.contextPath}/accounts/create">Create account</a>

<form method="get" action="${pageContext.request.contextPath}/accounts">
    <input type="text" name="keyword" placeholder="Username, full name, phone">
    <%-- TODO: role select (roles in scope), status select (ACTIVE/LOCKED), includeDeleted checkbox. --%>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead>
    <tr>
        <th>ID</th><th>Username</th><th>Full name</th><th>Phone</th><th>Email</th>
        <th>Role</th><th>Status</th><th>Soft-deleted</th><th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over accounts; render with c:out (never show the password or hash).
         Actions: Edit (/accounts/edit?id=), POST form to /accounts/lock (action=lock|unlock),
         Reset password (/accounts/reset-password?id=, Administrator only),
         POST form to /accounts/delete (Administrator only, not shown for the current user). --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
