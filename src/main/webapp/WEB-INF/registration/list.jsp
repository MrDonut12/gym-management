<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Registration/transaction list (CN-24, QT-13). Servlet: RegistrationListServlet (/staff/registrations).
    Roles: MANAGER, RECEPTIONIST.
    Request attributes: page (Page<RegistrationView>), filter (RegistrationFilter), packages (List<GymPackage>), error.
--%>
<c:set var="pageTitle" value="Registrations" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Registrations</h1>
<a href="${pageContext.request.contextPath}/staff/registrations/create">New registration</a>

<form method="get" action="${pageContext.request.contextPath}/staff/registrations">
    <select name="status">
        <option value="">All statuses</option>
        <option value="PENDING">PENDING</option>
        <option value="PAID">PAID</option>
        <option value="CANCELLED">CANCELLED</option>
    </select>
    <select name="packageId">
        <option value="">All packages</option>
        <%-- TODO: c:forEach over packages. --%>
    </select>
    <input type="text" name="memberKeyword" maxlength="100" placeholder="Member code or phone">
    <select name="axis">
        <option value="CREATED_AT">By created date</option>
        <option value="PAID_AT">By payment date (PAID only)</option>
    </select>
    <label>From <input type="date" name="fromDate"></label>
    <label>To <input type="date" name="toDate"></label>
    <%-- TODO: Re-select/re-fill the current filter values (default range: current month). --%>
    <button type="submit">Filter</button>
</form>

<table class="table">
    <thead>
    <tr><th>Code</th><th>Member</th><th>Package (snapshot)</th><th>Amount</th><th>Status</th><th>Method</th>
        <th>Created at</th><th>Paid at</th><th>Start - End</th><th></th></tr>
    </thead>
    <tbody>
    <%-- TODO: c:forEach over page.items; c:out; fmt:formatNumber for amounts.
         PENDING rows: "Confirm" link (/staff/payments/confirm?id=) and a POST form to /staff/registrations/cancel
         (onsubmit="return validateCancelForm(this);") with hidden id and a required reason input (3-255 characters, HL-15).
         PAID/CANCELLED rows: no actions. --%>
    </tbody>
</table>
<%-- TODO: Pagination; links keep every filter value. --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
