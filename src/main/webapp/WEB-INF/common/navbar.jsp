<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@page import="group4.gym.management.security.RbacPolicy,group4.gym.management.security.Permission,group4.gym.management.util.SessionUtil"%>
<nav class="navbar" aria-label="Main navigation">
<a class="brand" href="${pageContext.request.contextPath}/home">Gym Management</a>
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
<c:if test="${not empty currentAccount}">
    <span><c:out value="${currentAccount.fullName}"/> (<c:out value="${currentAccount.role}"/>)</span>
    <form method="post" action="${pageContext.request.contextPath}/logout"><button type="submit">Logout</button></form>
</c:if>
</nav>
