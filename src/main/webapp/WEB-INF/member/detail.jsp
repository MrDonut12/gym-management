<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member detail (CN-13). Servlet: MemberDetailServlet (/staff/members/detail). Roles: MANAGER, RECEPTIONIST.
    Request attributes: detail (MemberDetailView).
--%>
<c:set var="pageTitle" value="Member details" scope="request"/>
<c:set var="member" value="${detail.member}"/>
<c:set var="isManager" value="${sessionScope.sessionUser.role == 'MANAGER'}"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">
        Member details
        <c:if test="${member.deleted}"><span class="badge text-bg-danger align-middle ms-2">Soft-deleted</span></c:if>
    </h1>
    <div class="d-flex flex-wrap gap-2">
        <c:if test="${not member.deleted}">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/registrations/create?memberId=${member.id}">Create registration</a>
            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/staff/members/edit?id=${member.id}">Edit</a>
            <c:if test="${isManager}">
                <form class="m-0" method="post" action="${pageContext.request.contextPath}/manager/members/delete"
                      onsubmit="return confirm('Delete this member profile? All pending registrations will be cancelled.');">
                    <input type="hidden" name="id" value="${member.id}">
                    <button class="btn btn-outline-danger" type="submit">Delete</button>
                </form>
            </c:if>
        </c:if>
        <c:if test="${member.deleted and isManager}">
            <form class="m-0" method="post" action="${pageContext.request.contextPath}/manager/members/restore"
                  onsubmit="return confirm('Restore this member profile?');">
                <input type="hidden" name="id" value="${member.id}">
                <button class="btn btn-outline-success" type="submit">Restore</button>
            </form>
        </c:if>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members">Back to list</a>
    </div>
</div>

<c:if test="${detail.expiringSoon}">
    <div class="alert alert-warning" role="status">
        Expiring soon: the current package ends on <strong><c:out value="${DateTimeUtil.format(detail.currentEndDate)}"/></strong>
        and there is no follow-on package.
    </div>
</c:if>

<div class="row g-4 mb-4">
    <section class="col-12 col-lg-7">
        <div class="card h-100">
            <div class="card-body">
                <h2 class="h5 mb-3">Profile</h2>
                <dl class="row mb-0">
                    <dt class="col-sm-4">ID</dt><dd class="col-sm-8"><c:out value="${member.code}"/></dd>
                    <dt class="col-sm-4">Full name</dt><dd class="col-sm-8"><c:out value="${member.fullName}"/></dd>
                    <dt class="col-sm-4">Phone</dt><dd class="col-sm-8"><c:out value="${member.phone}"/></dd>
                    <dt class="col-sm-4">Email</dt><dd class="col-sm-8"><c:out value="${member.email}" default="-"/></dd>
                    <dt class="col-sm-4">Date of birth</dt>
                    <dd class="col-sm-8">
                        <c:choose>
                            <c:when test="${not empty member.dateOfBirth}"><c:out value="${DateTimeUtil.format(member.dateOfBirth)}"/></c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </dd>
                    <dt class="col-sm-4">Gender</dt><dd class="col-sm-8"><c:out value="${member.gender.label}" default="-"/></dd>
                    <dt class="col-sm-4">Created at</dt>
                    <dd class="col-sm-8"><c:if test="${not empty member.createdAt}"><c:out value="${DateTimeUtil.format(member.createdAt)}"/></c:if></dd>
                </dl>
            </div>
        </div>
    </section>
    <section class="col-12 col-lg-5">
        <div class="card h-100">
            <div class="card-body">
                <h2 class="h5 mb-3">Membership today</h2>
                <dl class="row mb-0">
                    <dt class="col-sm-5">Status</dt>
                    <dd class="col-sm-7"><c:set var="memberStatus" value="${detail.status}"/><%@include file="status-badge.jspf"%></dd>
                    <dt class="col-sm-5">Expiry date</dt>
                    <dd class="col-sm-7">
                        <c:choose>
                            <c:when test="${not empty detail.currentEndDate}"><c:out value="${DateTimeUtil.format(detail.currentEndDate)}"/></c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </dd>
                </dl>
            </div>
        </div>
    </section>
</div>

<section>
    <h2 class="h5 mb-3">Registrations</h2>
    <div class="table-responsive border rounded">
        <table class="table table-hover align-middle mb-0">
            <caption class="visually-hidden">Registration history of this member, newest first</caption>
            <thead class="table-light">
                <tr><th scope="col">Code</th><th scope="col">Package (snapshot)</th><th scope="col">Amount (VND)</th>
                    <th scope="col">Status</th><th scope="col">Created at</th><th scope="col">Paid at</th>
                    <th scope="col">Start</th><th scope="col">End</th><th scope="col">Cancel reason</th></tr>
            </thead>
            <tbody>
                <c:forEach var="registration" items="${detail.registrations}">
                    <tr>
                        <td class="fw-semibold"><c:out value="${registration.code}"/></td>
                        <td><c:out value="${registration.packageNameSnapshot}"/></td>
                        <td class="text-end"><fmt:formatNumber value="${registration.priceSnapshot}" type="number" groupingUsed="true"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${registration.status == 'PAID'}"><span class="badge badge-paid">Paid</span></c:when>
                                <c:when test="${registration.status == 'PENDING'}"><span class="badge badge-pending">Pending</span></c:when>
                                <c:otherwise><span class="badge badge-cancelled">Cancelled</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-nowrap"><c:if test="${not empty registration.createdAt}"><c:out value="${DateTimeUtil.format(registration.createdAt)}"/></c:if></td>
                        <td class="text-nowrap"><c:if test="${not empty registration.paidAt}"><c:out value="${DateTimeUtil.format(registration.paidAt)}"/></c:if></td>
                        <td class="text-nowrap"><c:if test="${not empty registration.startDate}"><c:out value="${DateTimeUtil.format(registration.startDate)}"/></c:if></td>
                        <td class="text-nowrap"><c:if test="${not empty registration.endDate}"><c:out value="${DateTimeUtil.format(registration.endDate)}"/></c:if></td>
                        <td><c:out value="${registration.cancelReason}"/></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty detail.registrations}">
                    <tr><td colspan="9" class="text-center text-body-secondary py-5">No registrations yet.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
