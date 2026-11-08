<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Package list (CN-18). Servlet: PackageListServlet (/staff/packages). Roles: MANAGER, RECEPTIONIST.
    Request attributes: packages (List<GymPackage>), active (filter value: "", "true", "false").
--%>
<c:set var="pageTitle" value="Packages" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="isManager" value="${sessionScope.sessionUser.role == 'MANAGER'}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
    <h1 class="h3 mb-0">Packages</h1>
    <c:if test="${isManager}">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/manager/packages/create">Create package</a>
    </c:if>
</div>

<c:if test="${isManager}">
    <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/staff/packages">
        <div class="col-auto">
            <label class="visually-hidden" for="active">Sale status</label>
            <select class="form-select" id="active" name="active">
                <option value="" ${requestScope.active == '' ? 'selected' : ''}>All</option>
                <option value="true" ${requestScope.active == 'true' ? 'selected' : ''}>On sale</option>
                <option value="false" ${requestScope.active == 'false' ? 'selected' : ''}>Not on sale</option>
            </select>
        </div>
        <div class="col-auto">
            <button class="btn btn-outline-primary" type="submit">Filter</button>
        </div>
    </form>
</c:if>

<table class="table table-striped align-middle">
    <thead>
    <tr><th>Name</th><th>Duration (months)</th><th class="text-end">Price (VND)</th><th>Sale status</th>
        <c:if test="${isManager}"><th></th></c:if></tr>
    </thead>
    <tbody>
        <%-- TODO: c:forEach over packages; c:out; fmt:formatNumber for the price.
             MANAGER actions: Edit (/manager/packages/edit?id=), POST form to /manager/packages/status
             (action=deactivate when on sale, activate otherwise). No delete button (QT-16). --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
