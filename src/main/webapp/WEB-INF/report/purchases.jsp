<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    New registrations and renewals/repurchases (CN-29). Servlet: PurchaseReportServlet (/reports/purchases). Roles: Owner, Manager.
    Request attributes: fromDate, toDate, statistics (PurchaseStatistics).
--%>
<c:set var="pageTitle" value="Purchase statistics" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Purchase statistics</h1>
<form method="get" action="${pageContext.request.contextPath}/reports/purchases">
    <label>From date <input type="date" name="fromDate"></label>
    <label>To date <input type="date" name="toDate"></label>
    <button type="submit">View</button>
    <p class="hint">A member's first PAID purchase is a new registration; every later PAID purchase is a renewal/repurchase.</p>
</form>
<%-- TODO: Show newRegistrations and renewals. --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
