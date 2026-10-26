<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member overview (CN-28), the MANAGER home page. Servlet: MemberOverviewServlet (/manager/overview). MANAGER only.
    Request attributes: overview (MemberOverview), range (DateRange), error.
--%>
<c:set var="pageTitle" value="Overview" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Member overview</h1>

<section>
    <h2>Today</h2>
    <%-- TODO: overview.activeProfiles (managed, not soft-deleted), overview.deletedProfiles,
         overview.membersWithActivePackage, overview.expiringSoon (link to /staff/members/expiring). --%>
</section>

<section>
    <h2>New members</h2>
    <form method="get" action="${pageContext.request.contextPath}/manager/overview">
        <label>From <input type="date" name="fromDate"></label>
        <label>To <input type="date" name="toDate"></label>
        <%-- TODO: Re-fill the current values (default range: current month). --%>
        <button type="submit">View</button>
    </form>
    <%-- TODO: overview.newMembers (by profile creation date, including profiles soft-deleted later). --%>
</section>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
