<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Expiring-soon list (FR-17, BR-08). Servlet: ExpiringMemberListServlet (/staff/members/expiring). Roles: MANAGER, RECEPTIONIST.
    Display only; no notification is sent.
    Request attributes: members (List<ExpiringMember>), sorted by end date ascending.
--%>
<c:set var="pageTitle" value="Expiring soon" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-2">
    <h1 class="h3 mb-0">Expiring soon</h1>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members">Back to members</a>
</div>
<p class="hint mb-4">Members with an active package whose last valid day is within the next 7 days (today included) and no follow-on package.</p>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Members whose package expires within 7 days</caption>
        <thead class="table-light">
            <tr><th scope="col">Full name</th><th scope="col">Phone</th><th scope="col">Package</th>
                <th scope="col">Expiry date</th><th scope="col">Days left</th><th scope="col">Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${members}">
                <tr>
                    <td class="fw-semibold"><c:out value="${item.member.fullName}"/></td>
                    <td><c:out value="${item.member.phone}"/></td>
                    <td><c:out value="${item.packageName}"/></td>
                    <td class="text-nowrap"><c:if test="${not empty item.endDate}"><c:out value="${DateTimeUtil.format(item.endDate)}"/></c:if></td>
                    <td><span class="badge ${item.daysLeft <= 1 ? 'text-bg-danger' : 'text-bg-warning'}">${item.daysLeft} ${item.daysLeft == 1 ? 'day' : 'days'}</span></td>
                    <td><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/staff/members/detail?id=${item.member.id}">Detail</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty members}">
                <tr><td colspan="6" class="text-center text-body-secondary py-5">No members are expiring in the next 7 days.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
