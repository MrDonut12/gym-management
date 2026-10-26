<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Package list (CN-18). Servlet: PackageListServlet (/staff/packages). Roles: MANAGER, RECEPTIONIST.
    Request attributes: packages (List<GymPackage>), active (filter value: "", "true", "false").
--%>
<c:set var="pageTitle" value="Packages" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Packages</h1>
<%-- TODO: MANAGER only: "Create package" link (/manager/packages/create) and the on-sale filter below.
     RECEPTIONIST sees on-sale packages only and no action buttons. --%>
<a href="${pageContext.request.contextPath}/manager/packages/create">Create package</a>

<form method="get" action="${pageContext.request.contextPath}/staff/packages">
    <select name="active">
        <option value="">All</option>
        <option value="true">On sale</option>
        <option value="false">Not on sale</option>
    </select>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead><tr><th>Name</th><th>Duration (months)</th><th>Price (VND)</th><th>Sale status</th><th></th></tr></thead>
    <tbody>
    <%-- TODO: c:forEach over packages; c:out; fmt:formatNumber for the price.
         MANAGER actions: Edit (/manager/packages/edit?id=), POST form to /manager/packages/status
         (action=deactivate when on sale, activate otherwise). No delete button (QT-16). --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
