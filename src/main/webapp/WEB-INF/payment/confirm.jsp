<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Two-step payment confirmation (FR-25). Servlet: PaymentConfirmServlet (/staff/payments/confirm). Roles: MANAGER, RECEPTIONIST.
    Request attributes:
      view (RegistrationView) - always: registration (snapshot, status, payment/cancel columns) and member display data
      preview (SchedulePreview) - step 1 while PENDING: expected start/end computed by BR-05 (nothing is saved)
      blockedReason - PENDING but cannot be confirmed (member soft-deleted, BR-11)
    After a successful step 2 the registration is PAID and the schedule actually saved is shown.
    No amount or date input: the backend never reads them from the client (AC-20).
--%>
<c:set var="pageTitle" value="Confirm payment" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="reg" value="${view.registration}"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">
        <c:choose>
            <c:when test="${reg.status == 'PAID'}">Payment confirmed</c:when>
            <c:otherwise>Confirm payment</c:otherwise>
        </c:choose>
    </h1>
    <div class="d-flex flex-wrap gap-2">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/registrations">Back to registrations</a>
        <c:if test="${not view.memberDeleted or sessionScope.sessionUser.role == 'MANAGER'}">
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members/detail?id=${reg.memberId}">Member details</a>
        </c:if>
    </div>
</div>

<c:if test="${not empty blockedReason}">
    <div class="alert alert-warning" role="alert"><c:out value="${blockedReason}"/></div>
</c:if>

<div class="row g-4">
    <section class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-body">
                <h2 class="h5 mb-3">Registration</h2>
                <dl class="row mb-0">
                    <dt class="col-sm-5">Code</dt><dd class="col-sm-7"><c:out value="${reg.code}"/></dd>
                    <dt class="col-sm-5">Member</dt>
                    <dd class="col-sm-7">
                        <c:out value="${view.memberCode}"/> - <c:out value="${view.memberName}"/>
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
        <c:choose>
            <%-- Step 1: review. Leaving this page (Back) changes nothing. --%>
            <c:when test="${not empty preview}">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5 mb-3">Expected schedule</h2>
                        <dl class="row">
                            <dt class="col-sm-5">Start date</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(preview.startDate)}"/></dd>
                            <dt class="col-sm-5">End date</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(preview.endDate)}"/></dd>
                        </dl>
                        <p class="hint">The dates are computed by the system and fixed again at the moment of confirmation:
                            the package starts today, or the day after the member's last paid package ends.</p>
                        <form method="post" action="${pageContext.request.contextPath}/staff/payments/confirm"
                              onsubmit="return confirm('Confirm that the full amount has been received?');">
                            <input type="hidden" name="id" value="${reg.id}">
                            <fieldset class="mb-3">
                                <legend class="form-label fs-6">Payment method *</legend>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" id="methodCash" name="paymentMethod" value="CASH" required>
                                    <label class="form-check-label" for="methodCash">Cash</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" id="methodTransfer" name="paymentMethod" value="TRANSFER">
                                    <label class="form-check-label" for="methodTransfer">Bank transfer</label>
                                </div>
                            </fieldset>
                            <button class="btn btn-primary" type="submit">Confirm PAID</button>
                            <a class="btn btn-link" href="${pageContext.request.contextPath}/staff/registrations">Back</a>
                        </form>
                    </div>
                </div>
            </c:when>

            <%-- After step 2: the schedule actually saved. --%>
            <c:when test="${reg.status == 'PAID'}">
                <div class="card h-100 border-success">
                    <div class="card-body">
                        <h2 class="h5 mb-3">Saved schedule</h2>
                        <dl class="row mb-0">
                            <dt class="col-sm-5">Paid at</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(reg.paidAt)}"/></dd>
                            <dt class="col-sm-5">Method</dt>
                            <dd class="col-sm-7">${reg.paymentMethod == 'CASH' ? 'Cash' : 'Bank transfer'}</dd>
                            <dt class="col-sm-5">Start date</dt><dd class="col-sm-7 fw-semibold"><c:out value="${DateTimeUtil.format(reg.startDate)}"/></dd>
                            <dt class="col-sm-5">End date</dt><dd class="col-sm-7 fw-semibold"><c:out value="${DateTimeUtil.format(reg.endDate)}"/></dd>
                        </dl>
                    </div>
                </div>
            </c:when>

            <c:when test="${reg.status == 'CANCELLED'}">
                <div class="card h-100">
                    <div class="card-body">
                        <h2 class="h5 mb-3">Cancelled</h2>
                        <p>This registration is no longer awaiting payment and cannot be confirmed.</p>
                        <dl class="row mb-0">
                            <dt class="col-sm-5">Cancelled at</dt><dd class="col-sm-7"><c:out value="${DateTimeUtil.format(reg.cancelledAt)}"/></dd>
                            <dt class="col-sm-5">Reason</dt><dd class="col-sm-7"><c:out value="${reg.cancelReason}"/></dd>
                        </dl>
                    </div>
                </div>
            </c:when>
        </c:choose>
    </section>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
