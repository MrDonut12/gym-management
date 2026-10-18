<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Manual check-out (CN-15). Servlet: CheckOutServlet (/visits/check-out). Roles: Manager, Receptionist.
    Request attributes: openVisits (List<VisitView>, after the catch-up close step), message, error.
--%>
<c:set var="pageTitle" value="Check-out" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Check-out</h1>
<table class="table">
    <thead><tr><th>Visit ID</th><th>Member</th><th>Plan</th><th>Check-in time</th><th>Created by</th><th></th></tr></thead>
    <tbody>
    <%-- TODO: c:forEach over openVisits; each row has a POST form to /visits/check-out (hidden visitId) with a "Check out" button.
         No OPEN visits -> show a message row. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
