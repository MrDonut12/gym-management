<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member list and search (CN-11), the RECEPTIONIST home page. Servlet: MemberListServlet (/staff/members).
    Roles: MANAGER, RECEPTIONIST. Plain search, no AJAX (FUT-01).
    Request attributes: page (Page<MemberListItem>), keyword, scope (MemberScope).
--%>
<c:set var="pageTitle" value="Members" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Members</h1>
<a href="${pageContext.request.contextPath}/staff/members/create">Create member</a>
<a href="${pageContext.request.contextPath}/staff/members/expiring">Expiring soon</a>

<form method="get" action="${pageContext.request.contextPath}/staff/members">
    <input type="text" name="keyword" maxlength="100" placeholder="Code (HV000123 / 123), phone or full name">
    <%-- TODO: Show the scope select only for MANAGER (QT-11); RECEPTIONIST always gets ACTIVE. --%>
    <select name="scope">
        <option value="ACTIVE">Managed (not deleted)</option>
        <option value="DELETED">Soft-deleted</option>
        <option value="ALL">All</option>
    </select>
    <button type="submit">Search</button>
</form>

<table class="table">
    <thead>
    <tr><th>Code</th><th>Full name</th><th>Phone</th><th>Membership status</th><th>Expiry date</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over page.items; c:out for every value; status label (ACTIVE/UPCOMING/EXPIRED/NONE);
         "Soft-deleted" badge when member.deleted; Detail link (/staff/members/detail?id=). No results -> a message row. --%>
    </tbody>
</table>
<%-- TODO: Pagination (page.page / page.totalPages); links keep keyword and scope (KT-09). --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
