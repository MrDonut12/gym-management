<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Registration/transaction list (FR-24, BR-13). Servlet: RegistrationListServlet (/staff/registrations).
    Roles: MANAGER, RECEPTIONIST.
    Request attributes: page (Page<RegistrationView>), filter (RegistrationFilter, dates resolved to the range used),
                        packages (List<GymPackage>), fieldErrors, error.
    PENDING rows have Confirm (FR-25 review step) and Cancel with a reason (FR-23); PAID/CANCELLED rows have no action.
--%>
<c:set var="pageTitle" value="Registrations" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="result" value="${requestScope.page}"/>
<c:set var="filter" value="${requestScope.filter}"/>
<c:set var="isManager" value="${sessionScope.sessionUser.role == 'MANAGER'}"/>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
    <h1 class="h3 mb-0">Registrations</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/registrations/create">New registration</a>
</div>

<form class="row g-3 align-items-end mb-2" method="get" action="${pageContext.request.contextPath}/staff/registrations">
    <div class="col-6 col-md-2">
        <label class="form-label" for="status">Status</label>
        <select class="form-select" id="status" name="status">
            <option value="">All statuses</option>
            <option value="PENDING" ${filter.status == 'PENDING' ? 'selected' : ''}>Pending</option>
            <option value="PAID" ${filter.status == 'PAID' ? 'selected' : ''}>Paid</option>
            <option value="CANCELLED" ${filter.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
        </select>
    </div>
    <div class="col-6 col-md-3">
        <label class="form-label" for="packageId">Package</label>
        <select class="form-select" id="packageId" name="packageId">
            <option value="">All packages</option>
            <c:forEach var="p" items="${packages}">
                <option value="${p.id}" ${filter.packageId == p.id ? 'selected' : ''}>
                    <c:out value="${p.name}"/><c:if test="${not p.active}"> (not on sale)</c:if>
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="col-12 col-md-4">
        <label class="form-label" for="memberKeyword">Member</label>
        <input class="form-control" type="text" id="memberKeyword" name="memberKeyword" maxlength="100"
               placeholder="Phone or full name" value="<c:out value='${filter.memberKeyword}'/>">
        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.memberKeyword}"/></div>
    </div>
    <div class="col-12 col-md-3">
        <label class="form-label" for="axis">Date range on</label>
        <select class="form-select" id="axis" name="axis">
            <option value="CREATED_AT" ${filter.axis != 'PAID_AT' ? 'selected' : ''}>Created date (every status)</option>
            <option value="PAID_AT" ${filter.axis == 'PAID_AT' ? 'selected' : ''}>Payment date (PAID only)</option>
        </select>
    </div>
    <div class="col-6 col-md-3">
        <label class="form-label" for="fromDate">From</label>
        <input class="form-control" type="date" id="fromDate" name="fromDate" value="<c:out value='${filter.fromDate}'/>">
    </div>
    <div class="col-6 col-md-3">
        <label class="form-label" for="toDate">To</label>
        <input class="form-control" type="date" id="toDate" name="toDate" value="<c:out value='${filter.toDate}'/>">
        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.toDate}"/></div>
    </div>
    <div class="col-12 col-md-2 d-grid">
        <button class="btn btn-outline-primary" type="submit">Filter</button>
    </div>
</form>
<p class="hint mb-4">Both days of the range are included. Empty dates use the current month.</p>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Registrations matching the filters</caption>
        <thead class="table-light">
            <tr><th scope="col">Code</th><th scope="col">Member</th><th scope="col">Package (snapshot)</th>
                <th scope="col" class="text-end">Amount (VND)</th><th scope="col">Status</th><th scope="col">Method</th>
                <th scope="col">Created at</th><th scope="col">Paid at</th><th scope="col">Start - End</th>
                <th scope="col">Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${result.items}">
                <c:set var="reg" value="${item.registration}"/>
                <tr>
                    <td class="fw-semibold text-nowrap"><c:out value="${reg.code}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${not item.memberDeleted or isManager}">
                                <a href="${pageContext.request.contextPath}/staff/members/detail?id=${reg.memberId}"><c:out value="${item.memberCode}"/></a>
                            </c:when>
                            <c:otherwise><c:out value="${item.memberCode}"/></c:otherwise>
                        </c:choose>
                        - <c:out value="${item.memberName}"/>
                        <c:if test="${item.memberDeleted}"><span class="badge text-bg-danger ms-1">Soft-deleted</span></c:if>
                        <div class="hint"><c:out value="${item.memberPhone}"/></div>
                    </td>
                    <td><c:out value="${reg.packageNameSnapshot}"/>
                        <div class="hint">${reg.durationMonthsSnapshot} ${reg.durationMonthsSnapshot == 1 ? 'month' : 'months'}</div></td>
                    <td class="text-end"><fmt:formatNumber value="${reg.priceSnapshot}" type="number" groupingUsed="true"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${reg.status == 'PAID'}"><span class="badge badge-paid">Paid</span></c:when>
                            <c:when test="${reg.status == 'PENDING'}"><span class="badge badge-pending">Pending</span></c:when>
                            <c:otherwise><span class="badge badge-cancelled" title="<c:out value='${reg.cancelReason}'/>">Cancelled</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${reg.paymentMethod == 'CASH'}">Cash</c:when>
                            <c:when test="${reg.paymentMethod == 'TRANSFER'}">Bank transfer</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-nowrap"><c:out value="${DateTimeUtil.format(reg.createdAt)}"/></td>
                    <td class="text-nowrap"><c:if test="${not empty reg.paidAt}"><c:out value="${DateTimeUtil.format(reg.paidAt)}"/></c:if></td>
                    <td class="text-nowrap">
                        <c:if test="${not empty reg.startDate}">
                            <c:out value="${DateTimeUtil.format(reg.startDate)}"/> - <c:out value="${DateTimeUtil.format(reg.endDate)}"/>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${reg.status == 'PENDING'}">
                            <div class="d-flex flex-column gap-2" style="min-width: 14rem;">
                                <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/staff/payments/confirm?id=${reg.id}">Confirm</a>
                                <form class="d-flex gap-1 m-0" method="post" action="${pageContext.request.contextPath}/staff/registrations/cancel"
                                      onsubmit="return validateCancelForm(this) &amp;&amp; confirm('Cancel this registration?');">
                                    <input type="hidden" name="id" value="${reg.id}">
                                    <label class="visually-hidden" for="reason-${reg.id}">Cancel reason</label>
                                    <input class="form-control form-control-sm" type="text" id="reason-${reg.id}" name="reason"
                                           placeholder="Cancel reason" minlength="3" maxlength="255" required>
                                    <button class="btn btn-sm btn-outline-danger" type="submit">Cancel</button>
                                </form>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty result.items}">
                <tr><td colspan="10" class="text-center text-body-secondary py-5">No registrations found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<%-- Pagination: every link keeps all filter values (dates included). --%>
<c:if test="${not empty result and result.totalPages > 1}">
    <nav class="mt-3" aria-label="Registration pages">
        <ul class="pagination flex-wrap mb-0">
            <c:forEach var="number" begin="1" end="${result.totalPages}">
                <c:url var="pageUrl" value="/staff/registrations">
                    <c:param name="status" value="${filter.status}"/>
                    <c:param name="packageId" value="${filter.packageId}"/>
                    <c:param name="memberKeyword" value="${filter.memberKeyword}"/>
                    <c:param name="axis" value="${filter.axis}"/>
                    <c:param name="fromDate" value="${filter.fromDate}"/>
                    <c:param name="toDate" value="${filter.toDate}"/>
                    <c:param name="page" value="${number}"/>
                </c:url>
                <li class="page-item ${number == result.page ? 'active' : ''}">
                    <a class="page-link" href="${pageUrl}" ${number == result.page ? 'aria-current="page"' : ''}>${number}</a>
                </li>
            </c:forEach>
        </ul>
    </nav>
    <p class="hint mt-2">Page ${result.page} of ${result.totalPages} (${result.totalItems} registrations).</p>
</c:if>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
