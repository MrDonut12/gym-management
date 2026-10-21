<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Visit history lookup (CN-16). Servlet: VisitHistoryServlet (/visits). Roles: Owner, Manager, Receptionist.
    Request attributes: visits (List<VisitView>), filter (VisitFilter).
    No edit/cancel visit buttons (FUT-01).
--%>
<c:set var="pageTitle" value="Visit history" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Visit history</h1>
<form method="get" action="${pageContext.request.contextPath}/visits">
    <label>Member code <input type="number" name="memberId" min="1"></label>
    <label>From date <input type="date" name="fromDate"></label>
    <label>To date <input type="date" name="toDate"></label>
    <select name="status">
        <option value="">All statuses</option>
        <option value="OPEN">OPEN</option>
        <option value="COMPLETED">COMPLETED</option>
        <option value="AUTO_CLOSED">AUTO_CLOSED</option>
    </select>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead>
    <tr><th>Visit ID</th><th>Member</th><th>Plan</th><th>Date</th><th>Check-in</th><th>Check-out</th>
        <th>Status</th><th>Created by</th><th>Ended by</th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over visits; c:out; "Ended by" is "System" for AUTO_CLOSED. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
