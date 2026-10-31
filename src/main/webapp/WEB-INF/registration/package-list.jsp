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
    <c:forEach var="p" items="${requestScope.packages}">
        <tr>
            <td><c:out value="${p.name}"/></td>
            <td><c:out value="${p.durationMonths}"/></td>
            <td class="text-end"><fmt:formatNumber value="${p.price}" groupingUsed="true"/></td>
            <td>
                <c:choose>
                    <c:when test="${p.active}"><span class="badge badge-active">On sale</span></c:when>
                    <c:otherwise><span class="badge badge-none">Not on sale</span></c:otherwise>
                </c:choose>
            </td>
            <c:if test="${isManager}">
                <td class="text-end">
                    <%-- No delete button: packages are only deactivated (QT-16). --%>
                    <div class="d-flex justify-content-end gap-2">
                        <a class="btn btn-sm btn-outline-primary"
                           href="${pageContext.request.contextPath}/manager/packages/edit?id=${p.id}">Edit</a>
                        <form method="post" action="${pageContext.request.contextPath}/manager/packages/status">
                            <input type="hidden" name="id" value="${p.id}">
                            <c:choose>
                                <c:when test="${p.active}">
                                    <input type="hidden" name="action" value="deactivate">
                                    <button class="btn btn-sm btn-outline-danger" type="submit">Stop selling</button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="action" value="activate">
                                    <button class="btn btn-sm btn-outline-success" type="submit">Resume selling</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>
                </td>
            </c:if>
        </tr>
    </c:forEach>
    <c:if test="${empty requestScope.packages}">
        <tr><td colspan="${isManager ? 5 : 4}" class="text-center text-secondary">No packages found.</td></tr>
    </c:if>
    </tbody>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
