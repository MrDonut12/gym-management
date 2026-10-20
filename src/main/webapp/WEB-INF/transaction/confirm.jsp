<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Confirm PAID (CN-21, CN-22). Servlet: PaymentConfirmServlet (/transactions/confirm). Roles: Manager, Receptionist.
    Request attributes: transactionView (TransactionView), lateDailyPassWarning (boolean), error.
    NO start-date input field (FUT-03).
--%>
<c:set var="pageTitle" value="Confirm payment" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Confirm payment</h1>
<%-- TODO: Show the member, plan snapshot, amount due (snapshot) and current status.
     Show the form only while the transaction is PENDING. --%>

<%-- TODO: If lateDailyPassWarning: warn "This Daily Pass starts today but it is already past 20:00, so check-in is not possible today"
     (does not block the confirmation). --%>

<form method="post" action="${pageContext.request.contextPath}/transactions/confirm" enctype="multipart/form-data">
    <input type="hidden" name="id" value="">

    <label for="paymentMethod">Payment method *</label>
    <select id="paymentMethod" name="paymentMethod">
        <option value="CASH">Cash</option>
        <option value="BANK_TRANSFER">Bank transfer</option>
    </select>

    <label for="proofImage">Bank transfer proof image (optional)</label>
    <input type="file" id="proofImage" name="proofImage" accept="image/*">

    <button type="submit">Confirm PAID</button>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
