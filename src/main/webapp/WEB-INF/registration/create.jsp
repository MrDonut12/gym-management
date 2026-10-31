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
<c:set var="selected" value="${requestScope.member}"/>

<h1 class="h3 mb-3">New registration</h1>

<form class="row g-2 mb-4" method="get" action="${pageContext.request.contextPath}/staff/registrations/create">
    <div class="col-12 col-md-6">
        <label class="visually-hidden" for="keyword">Find member</label>
        <input class="form-control" type="text" id="keyword" name="keyword" maxlength="100"
               placeholder="Member code, phone or full name" value="<c:out value="${requestScope.keyword}"/>">
    </div>
    <div class="col-auto">
        <button class="btn btn-outline-primary" type="submit">Find member</button>
    </div>
</form>

<form method="post" style="max-width: 36rem;" action="${pageContext.request.contextPath}/staff/registrations/create">
    <div class="mb-3">
        <label class="form-label" for="memberId">Member *</label>
        <select class="form-select" id="memberId" name="memberId" required>
            <c:if test="${empty selected}">
                <option value="">-- Select a member --</option>
            </c:if>
            <c:if test="${not empty selected}">
                <option value="${selected.id}" selected>
                    <c:out value="${selected.code} - ${selected.fullName} - ${selected.phone}"/>
                </option>
            </c:if>
            <c:forEach var="m" items="${requestScope.members}">
                <c:if test="${empty selected or m.id != selected.id}">
                    <option value="${m.id}"><c:out value="${m.code} - ${m.fullName} - ${m.phone}"/></option>
                </c:if>
            </c:forEach>
        </select>
        <c:if test="${empty selected and empty requestScope.members}">
            <div class="hint">Search for a member above to fill this list.</div>
        </c:if>
    </div>

    <div class="mb-3">
        <label class="form-label" for="packageId">Package *</label>
        <select class="form-select" id="packageId" name="packageId" required>
            <option value="">-- Select a package --</option>
            <c:forEach var="p" items="${requestScope.packages}">
                <option value="${p.id}">
                    <c:out value="${p.name} - ${p.durationMonths} month(s) - "/><fmt:formatNumber value="${p.price}" groupingUsed="true"/> VND
                </option>
            </c:forEach>
        </select>
        <div class="hint">The price shown is the current price and is fixed when the registration is created.</div>
    </div>

    <button class="btn btn-primary" type="submit">Create registration (awaiting payment)</button>
    <a class="btn btn-link" href="${pageContext.request.contextPath}/staff/registrations">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
