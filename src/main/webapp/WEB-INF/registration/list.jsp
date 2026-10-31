<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<%--
    Registration/transaction list (CN-24, QT-13). Servlet: RegistrationListServlet (/staff/registrations).
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

<form class="row g-2 align-items-end mb-3" method="get" action="${pageContext.request.contextPath}/staff/registrations">
    <div class="col-auto">
        <label class="form-label" for="status">Status</label>
        <select class="form-select" id="status" name="status">
            <option value="">All statuses</option>
            <c:forEach var="s" items="${['PENDING', 'PAID', 'CANCELLED']}">
                <option value="${s}" ${filter.status == s ? 'selected' : ''}>${s}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-auto">
        <label class="form-label" for="packageId">Package</label>
        <select class="form-select" id="packageId" name="packageId">
            <option value="">All packages</option>
            <c:forEach var="p" items="${requestScope.packages}">
                <option value="${p.id}" ${filter.packageId == p.id ? 'selected' : ''}><c:out value="${p.name}"/></option>
            </c:forEach>
        </select>
    </div>
    <div class="col-auto">
        <label class="form-label" for="memberKeyword">Member</label>
        <input class="form-control" type="text" id="memberKeyword" name="memberKeyword" maxlength="100"
               placeholder="Member code or phone" value="<c:out value="${filter.memberKeyword}"/>">
    </div>
    <div class="col-auto">
        <label class="form-label" for="axis">Date axis</label>
        <select class="form-select" id="axis" name="axis">
            <option value="CREATED_AT" ${filter.axis != 'PAID_AT' ? 'selected' : ''}>By created date</option>
            <option value="PAID_AT" ${filter.axis == 'PAID_AT' ? 'selected' : ''}>By payment date (PAID only)</option>
        </select>
    </div>
    <div class="col-auto">
        <label class="form-label" for="fromDate">From</label>
        <input class="form-control" type="date" id="fromDate" name="fromDate" value="${filter.fromDate}">
    </div>
    <div class="col-auto">
        <label class="form-label" for="toDate">To</label>
        <input class="form-control" type="date" id="toDate" name="toDate" value="${filter.toDate}">
    </div>
    <div class="col-auto">
        <button class="btn btn-outline-primary" type="submit">Filter</button>
    </div>
    <div class="hint">Leave the dates empty to show the current month.</div>
</form>

<div class="table-responsive">
<table class="table table-striped align-middle">
    <thead>
    <tr><th>Code</th><th>Member</th><th>Package (snapshot)</th><th class="text-end">Amount (VND)</th><th>Status</th><th>Method</th>
        <th>Created at</th><th>Paid at</th><th>Start - End</th><th></th></tr>
    </thead>
    <tbody>
    <c:forEach var="v" items="${result.items}">
        <c:set var="r" value="${v.registration}"/>
        <tr>
            <td><c:out value="${r.code}"/></td>
            <td>
                <c:out value="${v.memberCode} - ${v.memberName}"/>
                <c:if test="${v.memberDeleted}"><span class="badge badge-none">Soft-deleted</span></c:if>
                <div class="hint"><c:out value="${v.memberPhone}"/></div>
            </td>
            <td><c:out value="${r.packageNameSnapshot}"/> (<c:out value="${r.durationMonthsSnapshot}"/> mo)</td>
            <td class="text-end"><fmt:formatNumber value="${r.priceSnapshot}" groupingUsed="true"/></td>
            <td>
                <c:choose>
                    <c:when test="${r.status == 'PENDING'}"><span class="badge badge-pending">PENDING</span></c:when>
                    <c:when test="${r.status == 'PAID'}"><span class="badge badge-paid">PAID</span></c:when>
                    <c:otherwise><span class="badge badge-cancelled">CANCELLED</span></c:otherwise>
                </c:choose>
            </td>
            <td><c:out value="${r.paymentMethod}"/></td>
            <%-- LocalDateTime is not supported by fmt:formatDate: print "yyyy-MM-dd HH:mm" from its ISO text. --%>
            <td><c:if test="${not empty r.createdAt}"><c:out value="${fn:substring(fn:replace(r.createdAt, 'T', ' '), 0, 16)}"/></c:if></td>
            <td><c:if test="${not empty r.paidAt}"><c:out value="${fn:substring(fn:replace(r.paidAt, 'T', ' '), 0, 16)}"/></c:if></td>
            <td>
                <c:if test="${not empty r.startDate}"><c:out value="${r.startDate}"/> - <c:out value="${r.endDate}"/></c:if>
            </td>
            <td>
                <c:if test="${r.status == 'PENDING'}">
                    <div class="d-flex flex-wrap gap-2">
                        <a class="btn btn-sm btn-success"
                           href="${pageContext.request.contextPath}/staff/payments/confirm?id=${r.id}">Confirm</a>
                        <form class="d-flex gap-1" method="post" novalidate
                              action="${pageContext.request.contextPath}/staff/registrations/cancel"
                              onsubmit="return validateCancelForm(this);">
                            <input type="hidden" name="id" value="${r.id}">
                            <input class="form-control form-control-sm" type="text" name="reason" required
                                   minlength="3" maxlength="255" placeholder="Cancel reason" aria-label="Cancel reason">
                            <button class="btn btn-sm btn-outline-danger" type="submit">Cancel</button>
                        </form>
                    </div>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty result.items}">
        <tr><td colspan="10" class="text-center text-secondary">No registrations found.</td></tr>
    </c:if>
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
