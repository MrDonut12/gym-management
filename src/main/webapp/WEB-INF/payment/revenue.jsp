<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Revenue per period (CN-26, QT-12, QT-13). Servlet: RevenueReportServlet (/manager/reports/revenue). MANAGER only.
    Request attributes: range (DateRange), granularity, rows (List<RevenueRow>), total (RevenueRow), error, fieldErrors.
--%>
<c:set var="pageTitle" value="Revenue" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">Revenue by period</h1>
    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/manager/reports/revenue-by-package">By package</a>
</div>

<form class="row g-3 align-items-start mb-2" method="get" action="${pageContext.request.contextPath}/manager/reports/revenue">
    <%@include file="range-fields.jspf"%>
    <div class="col-12 col-md-3">
        <label class="form-label" for="granularity">Group by</label>
        <select class="form-select" id="granularity" name="granularity">
            <option value="DAY" ${granularity == 'DAY' ? 'selected' : ''}>Per day</option>
            <option value="MONTH" ${granularity == 'MONTH' ? 'selected' : ''}>Per month</option>
        </select>
    </div>
    <div class="col-12 col-md-3 d-grid align-self-end">
        <button class="btn btn-primary" type="submit">View</button>
    </div>
</form>
<p class="hint mb-4">
    Showing <c:out value="${DateTimeUtil.format(range.from)}"/> to <c:out value="${DateTimeUtil.format(range.to)}"/> (both days included).
    Only PAID registrations are counted, by payment time.
</p>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Revenue of PAID registrations per ${granularity == 'MONTH' ? 'month' : 'day'}</caption>
        <thead class="table-light">
            <tr><th scope="col">${granularity == 'MONTH' ? 'Month' : 'Day'}</th>
                <th scope="col" class="text-end">PAID registrations</th><th scope="col" class="text-end">Revenue (VND)</th></tr>
        </thead>
        <tbody>
            <c:forEach var="row" items="${rows}">
                <tr>
                    <td><c:out value="${row.label}"/></td>
                    <td class="text-end">${row.transactionCount}</td>
                    <td class="text-end"><fmt:formatNumber value="${row.revenue}" type="number" groupingUsed="true"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty rows}">
                <tr><td colspan="3" class="text-center text-body-secondary py-5">No PAID registrations in this range.</td></tr>
            </c:if>
        </tbody>
        <tfoot class="table-light fw-semibold">
            <tr>
                <th scope="row"><c:out value="${total.label}"/></th>
                <td class="text-end">${total.transactionCount}</td>
                <td class="text-end"><fmt:formatNumber value="${total.revenue}" type="number" groupingUsed="true"/></td>
            </tr>
        </tfoot>
    </table>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
