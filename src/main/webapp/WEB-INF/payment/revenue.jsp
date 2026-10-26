<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Revenue per period (CN-26, QT-12, QT-13). Servlet: RevenueReportServlet (/manager/reports/revenue). MANAGER only.
    Request attributes: range (DateRange), granularity, rows (List<RevenueRow>), total (RevenueRow), error.
--%>
<c:set var="pageTitle" value="Revenue" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Revenue by period</h1>
<a href="${pageContext.request.contextPath}/manager/reports/revenue-by-package">By package</a>

<form method="get" action="${pageContext.request.contextPath}/manager/reports/revenue">
    <label>From <input type="date" name="fromDate"></label>
    <label>To <input type="date" name="toDate"></label>
    <select name="granularity">
        <option value="DAY">Per day</option>
        <option value="MONTH">Per month</option>
    </select>
    <%-- TODO: Re-fill the current values (default range: current month). --%>
    <button type="submit">View</button>
    <p class="hint">Only PAID registrations are counted, by payment time; both end days are included.</p>
</form>

<table class="table">
    <thead><tr><th>Period</th><th>PAID registrations</th><th>Revenue (VND)</th></tr></thead>
    <tbody><%-- TODO: c:forEach over rows (label, transactionCount, revenue with fmt:formatNumber). --%></tbody>
    <tfoot><%-- TODO: Total row from total. --%></tfoot>
</table>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
