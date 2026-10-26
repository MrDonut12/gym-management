<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Revenue per package (CN-27, QT-12). Servlet: PackageRevenueReportServlet (/manager/reports/revenue-by-package). MANAGER only.
    Grouped by package ID and labelled with the CURRENT package name; the total equals CN-26 for the same range (KT-24).
    Request attributes: range (DateRange), rows (List<RevenueRow>), total (RevenueRow), error.
--%>
<c:set var="pageTitle" value="Revenue by package" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Revenue by package</h1>
<a href="${pageContext.request.contextPath}/manager/reports/revenue">By period</a>

<form method="get" action="${pageContext.request.contextPath}/manager/reports/revenue-by-package">
    <label>From <input type="date" name="fromDate"></label>
    <label>To <input type="date" name="toDate"></label>
    <%-- TODO: Re-fill the current values (default range: current month). --%>
    <button type="submit">View</button>
</form>

<table class="table">
    <thead><tr><th>Package</th><th>PAID registrations</th><th>Revenue (VND)</th><th>Share (%)</th></tr></thead>
    <tbody><%-- TODO: c:forEach over rows (label, transactionCount, revenue, sharePercent with fmt:formatNumber). --%></tbody>
    <tfoot><%-- TODO: Total row from total. --%></tfoot>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
