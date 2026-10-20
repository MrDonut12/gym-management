<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Daily/monthly revenue (CN-26) and plan revenue (CN-27). Servlet: RevenueReportServlet (/reports/revenue).
    Roles: Owner, Manager, Accountant.
    Request attributes: fromDate, toDate, granularity, revenues (List<RevenueSummary>), totalRevenue (long),
                        planRevenues (List<PlanRevenueSummary>).
--%>
<c:set var="pageTitle" value="Revenue report" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Revenue report</h1>
<form method="get" action="${pageContext.request.contextPath}/reports/revenue">
    <label>From date <input type="date" name="fromDate"></label>
    <label>To date <input type="date" name="toDate"></label>
    <select name="granularity">
        <option value="DAY">Per day</option>
        <option value="MONTH">Per month</option>
    </select>
    <button type="submit">View</button>
    <p class="hint">Only PAID transactions are counted, by payment time; the whole last day is included.</p>
</form>

<%-- TODO: Total revenue (fmt:formatNumber totalRevenue). --%>

<h2>By period</h2>
<table class="table">
    <thead><tr><th>Period</th><th>Transactions</th><th>Revenue</th></tr></thead>
    <tbody><%-- TODO: c:forEach over revenues. --%></tbody>
</table>

<h2>By plan</h2>
<table class="table">
    <thead><tr><th>Plan (snapshot)</th><th>Transactions</th><th>Revenue</th></tr></thead>
    <tbody><%-- TODO: c:forEach over planRevenues. --%></tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
