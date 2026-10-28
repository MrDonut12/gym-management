<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<nav class="navbar navbar-expand-lg bg-body-tertiary border-bottom" aria-label="Main navigation">
<div class="container flex-wrap gap-3">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Gym Management</a>
    <c:if test="${not empty sessionScope.sessionUser}">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-menu"
                aria-controls="main-menu" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse flex-wrap gap-3" id="main-menu">
            <div class="navbar-nav flex-wrap">
                <c:if test="${sessionScope.sessionUser.role == 'ADMIN'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/accounts">Accounts</a>
                </c:if>
                <c:if test="${sessionScope.sessionUser.role == 'MANAGER'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/manager/overview">Overview</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/manager/reports/revenue">Revenue</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/manager/reports/revenue-by-package">Revenue by package</a>
                </c:if>
                <c:if test="${sessionScope.sessionUser.role == 'MANAGER' or sessionScope.sessionUser.role == 'RECEPTIONIST'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/staff/members">Members</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/staff/members/expiring">Expiring soon</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/staff/packages">Packages</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/staff/registrations">Registrations</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/staff/registrations/create">New registration</a>
                </c:if>
            </div>
            <div class="d-flex align-items-center flex-wrap gap-3 ms-lg-auto">
                <span><c:out value="${sessionScope.sessionUser.fullName}"/> (<c:out value="${sessionScope.sessionUser.role}"/>)</span>
                <a href="${pageContext.request.contextPath}/change-password">Change password</a>
                <form method="post" action="${pageContext.request.contextPath}/logout">
                    <button class="btn btn-outline-secondary" type="submit">Logout</button>
                </form>
            </div>
        </div>
    </c:if>
</div>
</nav>
