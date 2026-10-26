<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Two-step payment confirmation (CN-25). Servlet: PaymentConfirmServlet (/staff/payments/confirm). Roles: MANAGER, RECEPTIONIST.
    Request attributes:
      preview (SchedulePreview) - step 1 while PENDING: member, package snapshot, amount snapshot, expected start/end (QT-05)
      registration (Registration) - after a successful confirmation: the schedule actually saved
      error
    No amount or date input: the backend never reads them from the client (KT-20).
--%>
<c:set var="pageTitle" value="Confirm payment" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Confirm payment</h1>

<%-- TODO: When preview is present (step 1, review):
     show registration code, member code/full name/phone, package name snapshot, duration, amount (fmt:formatNumber),
     expected start date and end date; then the form below. A "Back" link returns to /staff/registrations without any change. --%>
<form method="post" action="${pageContext.request.contextPath}/staff/payments/confirm">
    <input type="hidden" name="id" value="">

    <fieldset>
        <legend>Payment method *</legend>
        <label><input type="radio" name="paymentMethod" value="CASH" required> Cash</label>
        <label><input type="radio" name="paymentMethod" value="TRANSFER"> Bank transfer</label>
    </fieldset>

    <button type="submit">Confirm PAID</button>
    <a href="${pageContext.request.contextPath}/staff/registrations">Back</a>
</form>

<%-- TODO: When registration is PAID (after the redirect): show paid_at, method, confirmed start date and end date. --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
