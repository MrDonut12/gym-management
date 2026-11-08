<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<%--
    Registration/transaction list (FR-24, BR-13). Servlet: RegistrationListServlet (/staff/registrations).
    Roles: MANAGER, RECEPTIONIST.
    Request attributes: page (Page<RegistrationView>), filter (RegistrationFilter), packages (List<GymPackage>), error.
--%>
<c:set var="pageTitle" value="Registrations" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="result" value="${requestScope.page}"/>
<c:set var="filter" value="${requestScope.filter}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
    <h1 class="h3 mb-0">Registrations</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/registrations/create">New registration</a>
</div>

<form method="get" action="${pageContext.request.contextPath}/staff/registrations">
    <select name="status">
        <option value="">All statuses</option>
        <option value="PENDING">PENDING</option>
        <option value="PAID">PAID</option>
        <option value="CANCELLED">CANCELLED</option>
    </select>
    <select name="packageId">
        <option value="">All packages</option>
        <%-- TODO: c:forEach over packages. --%>
    </select>
    <input type="text" name="memberKeyword" maxlength="100" placeholder="Member code (HV000123), phone or full name">
    <select name="axis">
        <option value="CREATED_AT">By created date</option>
        <option value="PAID_AT">By payment date (PAID only)</option>
    </select>
    <label>From <input type="date" name="fromDate"></label>
    <label>To <input type="date" name="toDate"></label>
        <%-- TODO: Re-select/re-fill the current filter values (default range: current month). --%>
    <button type="submit">Filter</button>
</form>

<div class="table-responsive">
<table class="table table-striped align-middle">
    <thead>
        <tr><th>Code</th><th>Member</th><th>Package (snapshot)</th><th>Amount</th><th>Status</th><th>Method</th>
            <th>Created at</th><th>Paid at</th><th>Start - End</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over page.items; c:out; fmt:formatNumber for amounts.
         PENDING rows: "Confirm" link (/staff/payments/confirm?id=) and a POST form to /staff/registrations/cancel
         (onsubmit="return validateCancelForm(this);") with hidden id and a required reason input (3-255 characters, VR-15).
         PAID/CANCELLED rows: no actions. --%>
    </tbody>
</table>
</div>

<%-- Pagination: every link keeps all filter values. --%>
<c:if test="${not empty result and result.totalPages > 1}">
    <c:url var="pageUrl" value="/staff/registrations">
        <c:param name="status" value="${filter.status}"/>
        <c:param name="packageId" value="${filter.packageId}"/>
        <c:param name="memberKeyword" value="${filter.memberKeyword}"/>
        <c:param name="axis" value="${filter.axis}"/>
        <c:param name="fromDate" value="${filter.fromDate}"/>
        <c:param name="toDate" value="${filter.toDate}"/>
    </c:url>
    <nav aria-label="Registration pages">
        <ul class="pagination">
            <li class="page-item ${result.hasPrevious ? '' : 'disabled'}">
                <a class="page-link" href="${pageUrl}&amp;page=${result.page - 1}">Previous</a>
            </li>
            <li class="page-item disabled">
                <span class="page-link">Page ${result.page} of ${result.totalPages} (${result.totalItems} registrations)</span>
            </li>
            <li class="page-item ${result.hasNext ? '' : 'disabled'}">
                <a class="page-link" href="${pageUrl}&amp;page=${result.page + 1}">Next</a>
            </li>
        </ul>
    </nav>
</c:if>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
