<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Registration detail. Servlet: RegistrationDetailServlet (/staff/registrations/detail). Roles: MANAGER, RECEPTIONIST.
    Request attributes: view (RegistrationView) - registration (snapshot, status, payment/cancel columns) and member display data.
    A PENDING registration shows, in the Payment card, a link to the payment confirmation and the cancel form with a reason.
--%>
<c:set var="pageTitle" value="Registration detail" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="reg" value="${view.registration}"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">Registration <c:out value="${reg.code}"/></h1>
    <div class="d-flex flex-wrap gap-2">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/registrations">Back to registrations</a>
        <c:if test="${not view.memberDeleted or sessionScope.sessionUser.role == 'MANAGER'}">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members/detail?id=${reg.memberId}">Member details</a>
        </c:if>
    </div>
</div>

<div class="row g-4">
    <section class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-body">
                <h2 class="h5 mb-3">Registration</h2>
                <dl class="row mb-0">
                    <dt class="col-sm-5">Code</dt><dd class="col-sm-7"><c:out value="${reg.code}"/></dd>
                    <dt class="col-sm-5">Member</dt>
                    <dd class="col-sm-7">
                        <c:out value="${view.memberName}"/>
                        <c:if test="${view.memberDeleted}"><span class="badge text-bg-danger ms-1">Soft-deleted</span></c:if>
                    </dd>
                    <dt class="col-sm-5">Phone</dt><dd class="col-sm-7"><c:out value="${view.memberPhone}"/></dd>
                    <dt class="col-sm-5">Package (snapshot)</dt><dd class="col-sm-7"><c:out value="${reg.packageNameSnapshot}"/></dd>
                    <dt class="col-sm-5">Duration</dt>
                    <dd class="col-sm-7">${reg.durationMonthsSnapshot} ${reg.durationMonthsSnapshot == 1 ? 'month' : 'months'}</dd>
                    <dt class="col-sm-5">Amount (VND)</dt>
                    <dd class="col-sm-7 fw-semibold"><fmt:formatNumber value="${reg.priceSnapshot}" type="number" groupingUsed="true"/></dd>
                    <dt class="col-sm-5">Created at</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(reg.createdAt)}"/></dd>
                    <dt class="col-sm-5">Status</dt>
                    <dd class="col-sm-7">
                        <c:choose>
                            <c:when test="${reg.status == 'PAID'}"><span class="badge badge-paid">Paid</span></c:when>
                            <c:when test="${reg.status == 'PENDING'}"><span class="badge badge-pending">Pending</span></c:when>
                            <c:otherwise><span class="badge badge-cancelled">Cancelled</span></c:otherwise>
                        </c:choose>
                    </dd>
                </dl>
            </div>
        </div>
    </section>

    <section class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-body">
                <c:choose>
                    <c:when test="${reg.status == 'PAID'}">
                        <h2 class="h5 mb-3">Payment</h2>
                        <dl class="row mb-0">
                            <dt class="col-sm-5">Method</dt>
                            <dd class="col-sm-7">${reg.paymentMethod == 'CASH' ? 'Cash' : 'Bank transfer'}</dd>
                            <dt class="col-sm-5">Paid at</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(reg.paidAt)}"/></dd>
                            <dt class="col-sm-5">Start date</dt><dd class="col-sm-7 fw-semibold"><c:out value="${DateTimeUtil.format(reg.startDate)}"/></dd>
                            <dt class="col-sm-5">End date</dt><dd class="col-sm-7 fw-semibold"><c:out value="${DateTimeUtil.format(reg.endDate)}"/></dd>
                        </dl>
                    </c:when>
                    <c:when test="${reg.status == 'CANCELLED'}">
                        <h2 class="h5 mb-3">Cancellation</h2>
                        <dl class="row mb-0">
                            <dt class="col-sm-5">Cancelled at</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(reg.cancelledAt)}"/></dd>
                            <dt class="col-sm-5">Reason</dt><dd class="col-sm-7"><c:out value="${reg.cancelReason}"/></dd>
                        </dl>
                    </c:when>
                    <c:otherwise>
                        <h2 class="h5 mb-3">Payment</h2>
                        <p>Awaiting payment. The package schedule is fixed when the payment is confirmed.</p>
                        <a class="btn btn-primary mb-4" href="${pageContext.request.contextPath}/staff/payments/confirm?id=${reg.id}">Confirm payment</a>
                        <form class="m-0" method="post" action="${pageContext.request.contextPath}/staff/registrations/cancel"
                              onsubmit="return validateCancelForm(this) &amp;&amp; confirm('Cancel this registration?');">
                            <input type="hidden" name="id" value="${reg.id}">
                            <label class="form-label" for="reason">Cancel reason</label>
                            <div class="d-flex gap-2">
                                <input class="form-control" type="text" id="reason" name="reason"
                                       placeholder="At least 3 characters" minlength="3" maxlength="255" required>
                                <button class="btn btn-outline-danger" type="submit">Cancel</button>
                            </div>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
