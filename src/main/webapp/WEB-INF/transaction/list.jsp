<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Look up and filter transactions (CN-23, CN-24). Servlet: TransactionListServlet (/transactions).
    Roles: Owner, Manager, Receptionist, Accountant.
    Request attributes: transactions (List<TransactionView>), filter (TransactionFilter).
--%>
<c:set var="pageTitle" value="Transactions" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Transactions</h1>
<form method="get" action="${pageContext.request.contextPath}/transactions">
    <label>From date <input type="date" name="fromDate"></label>
    <label>To date <input type="date" name="toDate"></label>
    <label><input type="checkbox" name="status" value="PENDING"> PENDING</label>
    <label><input type="checkbox" name="status" value="PAID"> PAID</label>
    <label><input type="checkbox" name="status" value="CANCELLED"> CANCELLED</label>
    <button type="submit">Filter</button>
    <p class="hint">PENDING/CANCELLED are filtered by creation time; PAID is filtered by payment time.</p>
</form>

<table class="table">
    <thead>
    <tr><th>Transaction ID</th><th>Member</th><th>Plan (snapshot)</th><th>Amount</th><th>Method</th>
        <th>Status</th><th>Created at</th><th>Paid at</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over transactions; c:out; format amounts with fmt:formatNumber; Detail link (/transactions/detail?id=).
         No actions to edit the amount/status. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
