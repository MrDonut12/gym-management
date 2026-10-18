<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Plan registration or renewal/repurchase - creates a Membership + PENDING transaction (CN-10, CN-11).
    Servlet: MembershipCreateServlet (/memberships/create). Roles: Manager, Receptionist.
    Request attributes: member (Member), planTypes (List<PlanType>), error.
    No start-date field (the start date is decided by the system when PAID; FUT-03).
--%>
<c:set var="pageTitle" value="Register / renew plan" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Register / renew plan</h1>
<%-- TODO: Show the member's code, full name and phone (c:out). A soft-deleted member -> show a message and hide the form. --%>

<form method="post" action="${pageContext.request.contextPath}/memberships/create">
    <input type="hidden" name="memberId" value="">

    <label for="planTypeId">Plan type *</label>
    <select id="planTypeId" name="planTypeId">
        <%-- TODO: c:forEach over planTypes: name - duration - current price (for reference only; the amount is decided by the backend). --%>
    </select>

    <label for="paymentMethod">Payment method *</label>
    <select id="paymentMethod" name="paymentMethod">
        <option value="CASH">Cash</option>
        <option value="BANK_TRANSFER">Bank transfer</option>
    </select>

    <button type="submit">Create registration (awaiting payment)</button>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
