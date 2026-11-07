<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Revenue per package (CN-27, QT-12). Servlet: PackageRevenueReportServlet (/manager/reports/revenue-by-package). MANAGER only.
    Grouped by package ID and labelled with the CURRENT package name; the total equals CN-26 for the same range (KT-24).
    Request attributes: range (DateRange), rows (List<RevenueRow>), total (RevenueRow), error, fieldErrors.
--%>
<c:set var="pageTitle" value="Revenue by package" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">Revenue by package</h1>
    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/manager/reports/revenue">By period</a>
</div>

<form class="row g-3 align-items-start mb-2" method="get" action="${pageContext.request.contextPath}/manager/reports/revenue-by-package">
    <%@include file="range-fields.jspf"%>
    <div class="col-12 col-md-3 d-grid align-self-end">
        <button class="btn btn-primary" type="submit">View</button>
    </div>
</form>
<p class="hint mb-4">
    Showing <c:out value="${DateTimeUtil.format(range.from)}"/> to <c:out value="${DateTimeUtil.format(range.to)}"/> (both days included).
    Only PAID registrations are counted, grouped by package with its current name.
</p>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Revenue of PAID registrations per package</caption>
        <thead class="table-light">
            <tr><th scope="col">Package</th><th scope="col" class="text-end">PAID registrations</th>
                <th scope="col" class="text-end">Revenue (VND)</th><th scope="col" class="text-end">Share (%)</th></tr>
        </thead>
        <tbody>
            <c:forEach var="row" items="${rows}">
                <tr>
                    <td><c:out value="${row.label}"/></td>
                    <td class="text-end">${row.transactionCount}</td>
                    <td class="text-end"><fmt:formatNumber value="${row.revenue}" type="number" groupingUsed="true"/></td>
                    <td class="text-end"><fmt:formatNumber value="${row.sharePercent}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty rows}">
                <tr><td colspan="4" class="text-center text-body-secondary py-5">No PAID registrations in this range.</td></tr>
            </c:if>
        </tbody>
        <tfoot class="table-light fw-semibold">
            <tr>
                <th scope="row"><c:out value="${total.label}"/></th>
                <td class="text-end">${total.transactionCount}</td>
                <td class="text-end"><fmt:formatNumber value="${total.revenue}" type="number" groupingUsed="true"/></td>
                <td class="text-end"><fmt:formatNumber value="${total.sharePercent}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
            </tr>
        </tfoot>
    </table>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
