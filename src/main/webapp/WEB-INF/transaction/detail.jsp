<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Transaction detail (CN-23). Servlet: TransactionDetailServlet (/transactions/detail).
    Request attributes: transactionView (TransactionView), message, error.
--%>
<c:set var="pageTitle" value="Transaction details" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Transaction details</h1>
<%-- TODO: Transaction ID, member, plan snapshot, amount snapshot, method, status, created_at, creator,
     paid_at, confirmer, registration schedule (start_at, last training day) once PAID. --%>

<%-- TODO: When PENDING and the user is a Manager/Receptionist:
     - link "Confirm payment" to /transactions/confirm?id=
     - POST form to /transactions/cancel (hidden id) with a "Cancel transaction" button. --%>
<form method="post" action="${pageContext.request.contextPath}/transactions/cancel">
    <input type="hidden" name="id" value="">
    <button type="submit">Cancel PENDING transaction</button>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
