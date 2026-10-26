<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Expiring-soon list (CN-17, QT-08). Servlet: ExpiringMemberListServlet (/staff/members/expiring). Roles: MANAGER, RECEPTIONIST.
    Display only; no notification is sent.
    Request attributes: members (List<ExpiringMember>), sorted by end date ascending.
--%>
<c:set var="pageTitle" value="Expiring soon" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Expiring soon</h1>
<p class="hint">Members with an active package whose last valid day is within the next 7 days (today included) and no follow-on package.</p>

<table class="table">
    <thead>
    <tr><th>Code</th><th>Full name</th><th>Phone</th><th>Package</th><th>Expiry date</th><th>Days left</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over members; c:out; Detail link (/staff/members/detail?id=). No rows -> a message row. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
