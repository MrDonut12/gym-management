<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create a PENDING registration (CN-22). Servlet: RegistrationCreateServlet (/staff/registrations/create).
    Roles: MANAGER, RECEPTIONIST.
    Request attributes: member (pre-selected Member, optional), keyword, members (List<Member>, not soft-deleted),
                        packages (List<GymPackage>, on sale only), error.
    No price, start date or payment method field: the price snapshot comes from the database (QT-03),
    the schedule is computed when PAID (QT-05) and the method is chosen at confirmation (CN-25).
--%>
<c:set var="pageTitle" value="New registration" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>New registration</h1>

<form method="get" action="${pageContext.request.contextPath}/staff/registrations/create">
    <input type="text" name="keyword" maxlength="100" placeholder="Member code, phone or full name">
    <button type="submit">Find member</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/staff/registrations/create">
    <label for="memberId">Member *</label>
    <select id="memberId" name="memberId" required>
        <%-- TODO: The pre-selected member and/or c:forEach over members: code - full name - phone (c:out). --%>
    </select>

    <label for="packageId">Package *</label>
    <select id="packageId" name="packageId" required>
        <%-- TODO: c:forEach over packages: name - duration - current price (display only). --%>
    </select>

    <button type="submit">Create registration (awaiting payment)</button>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
