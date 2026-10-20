<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    List/search members by code, name or phone (CN-07). Servlet: MemberListServlet (/members).
    Roles: Owner, Manager, Receptionist. Plain search (no AJAX; CN-38 is Should (B)).
    Request attributes: members (List<Member>), keyword, deletedFilter.
--%>
<c:set var="pageTitle" value="Members" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Members</h1>
<%-- TODO: Show the "Create member" button only for Manager/Receptionist. --%>
<a href="${pageContext.request.contextPath}/members/create">Create member</a>

<form method="get" action="${pageContext.request.contextPath}/members">
    <input type="text" name="keyword" placeholder="Code, full name or phone">
    <select name="deleted">
        <option value="">All profiles</option>
        <option value="false">Not soft-deleted</option>
        <option value="true">Soft-deleted</option>
    </select>
    <button type="submit">Search</button>
</form>

<table class="table">
    <thead>
    <tr><th>Code</th><th>Full name</th><th>Phone</th><th>Email</th><th>Created at</th><th>Profile status</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over members; c:out for every value; "Soft-deleted" label when deleted;
         Detail link (/members/detail?id=). No results -> show a message row. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
