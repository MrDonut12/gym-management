<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member overview (CN-28), the MANAGER home page. Servlet: MemberOverviewServlet (/manager/overview). MANAGER only.
    Request attributes: overview (MemberOverview), range (DateRange), today (LocalDate), error, fieldErrors.
--%>
<c:set var="pageTitle" value="Overview" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1 class="h3 mb-4">Member overview</h1>

<section class="mb-5">
    <h2 class="h5 mb-3">Today <span class="hint">(<c:out value="${DateTimeUtil.format(today)}"/>)</span></h2>
    <div class="row g-3">
        <div class="col-6 col-lg-3">
            <div class="card h-100"><div class="card-body">
                <div class="hint">Managed profiles</div>
                <div class="display-6 fw-semibold">${overview.activeProfiles}</div>
                <div class="hint">Not soft-deleted</div>
            </div></div>
        </div>
        <div class="col-6 col-lg-3">
            <div class="card h-100"><div class="card-body">
                <div class="hint">Soft-deleted profiles</div>
                <div class="display-6 fw-semibold">${overview.deletedProfiles}</div>
                <a class="small" href="${pageContext.request.contextPath}/staff/members?scope=DELETED">View list</a>
            </div></div>
        </div>
        <div class="col-6 col-lg-3">
            <div class="card h-100"><div class="card-body">
                <div class="hint">Members with an active package</div>
                <div class="display-6 fw-semibold">${overview.membersWithActivePackage}</div>
                <div class="hint">A PAID package covers today</div>
            </div></div>
        </div>
        <div class="col-6 col-lg-3">
            <div class="card h-100 ${overview.expiringSoon > 0 ? 'border-warning' : ''}"><div class="card-body">
                <div class="hint">Expiring soon</div>
                <div class="display-6 fw-semibold">${overview.expiringSoon}</div>
                <a class="small" href="${pageContext.request.contextPath}/staff/members/expiring">View list</a>
            </div></div>
        </div>
    </div>
</section>

<section>
    <h2 class="h5 mb-3">New members</h2>
    <form class="row g-3 align-items-start mb-3" method="get" action="${pageContext.request.contextPath}/manager/overview">
        <%@include file="range-fields.jspf"%>
        <div class="col-12 col-md-3 d-grid align-self-end">
            <button class="btn btn-primary" type="submit">View</button>
        </div>
    </form>
    <div class="card" style="max-width: 24rem;"><div class="card-body">
        <div class="hint">
            Created from <c:out value="${DateTimeUtil.format(range.from)}"/> to <c:out value="${DateTimeUtil.format(range.to)}"/>
        </div>
        <div class="display-6 fw-semibold">${overview.newMembers}</div>
        <div class="hint">Includes profiles soft-deleted later</div>
    </div></div>
</section>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
