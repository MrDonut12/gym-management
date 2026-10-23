<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@page import="group4.gym.management.security.RbacPolicy,group4.gym.management.security.Permission,group4.gym.management.util.SessionUtil"%>
<c:set var="pageTitle" value="Home" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<h1>Welcome, <c:out value="${currentAccount.fullName}"/>!</h1>
<p>Role: <c:out value="${currentAccount.role}"/></p>
<h2>Quick links</h2>
<div class="quick-links">
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_ACCOUNTS)) { %>
    <a href="${pageContext.request.contextPath}/accounts">Accounts</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_MEMBERS)) { %>
    <a href="${pageContext.request.contextPath}/members">Members</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_PLAN_TYPES)) { %>
    <a href="${pageContext.request.contextPath}/plans">Plan types</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_TRANSACTIONS)) { %>
    <a href="${pageContext.request.contextPath}/transactions">Transactions</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.CHECK_IN_OUT)) { %>
    <a href="${pageContext.request.contextPath}/visits/check-in">Check-in</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.CHECK_IN_OUT)) { %>
    <a href="${pageContext.request.contextPath}/visits/check-out">Check-out</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_VISIT_HISTORY)) { %>
    <a href="${pageContext.request.contextPath}/visits">Visit history</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_REVENUE_REPORT)) { %>
    <a href="${pageContext.request.contextPath}/reports/revenue">Revenue</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_MEMBER_REPORT)) { %>
    <a href="${pageContext.request.contextPath}/reports/members">Member statistics</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.VIEW_MEMBER_REPORT)) { %>
    <a href="${pageContext.request.contextPath}/reports/purchases">Purchases</a>
<% } %>
<% if (SessionUtil.getCurrentAccount(request) != null && RbacPolicy.hasPermission(SessionUtil.getCurrentAccount(request).getRole(), Permission.CHANGE_OWN_PASSWORD)) { %>
    <a href="${pageContext.request.contextPath}/account/change-password">Change password</a>
<% } %>
</div>
<jsp:include page="/WEB-INF/common/footer.jsp"/>
