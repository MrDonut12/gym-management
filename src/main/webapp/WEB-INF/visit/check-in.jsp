<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member check-in (CN-13, CN-14). Servlet: CheckInServlet (/visits/check-in). Roles: Manager, Receptionist.
    Request attributes: keyword, members (List<Member>, search results), message, error.
--%>
<c:set var="pageTitle" value="Check-in" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Check-in</h1>
<form method="get" action="${pageContext.request.contextPath}/visits/check-in">
    <input type="text" name="keyword" placeholder="Code, full name or phone">
    <button type="submit">Find member</button>
</form>

<table class="table">
    <thead><tr><th>Code</th><th>Full name</th><th>Phone</th><th>Profile status</th><th></th></tr></thead>
    <tbody>
    <%-- TODO: c:forEach over members; soft-deleted members get a label and no check-in button;
         each row has a POST form to /visits/check-in (hidden memberId) with a "Check in" button. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
