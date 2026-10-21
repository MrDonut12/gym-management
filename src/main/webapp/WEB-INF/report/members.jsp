<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Total members and new members (CN-28). Servlet: MemberReportServlet (/reports/members). Roles: Owner, Manager.
    Request attributes: fromDate, toDate, deletedFilter, statistics (MemberStatistics).
--%>
<c:set var="pageTitle" value="Member statistics" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Member statistics</h1>
<form method="get" action="${pageContext.request.contextPath}/reports/members">
    <label>From date <input type="date" name="fromDate"></label>
    <label>To date <input type="date" name="toDate"></label>
    <select name="deleted">
        <option value="">All profiles</option>
        <option value="false">Not soft-deleted</option>
        <option value="true">Soft-deleted</option>
    </select>
    <button type="submit">View</button>
</form>
<%-- TODO: Show totalMembers (including soft-deleted), activeProfiles, deletedProfiles, newMembers (by profile creation date). --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
