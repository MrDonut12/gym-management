<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%--
    Role-based menu (CL-11). Hiding items is only a UI aid; AuthFilter and the services always re-check permissions (section 3).
    Session attribute: sessionUser (SessionUser: id, username, fullName, role).
--%>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/home">Gym Management</a>
    <%-- TODO: Show the menu only when sessionScope.sessionUser is not empty. Items by role (section 3):
         - ADMIN:                 Accounts (/admin/accounts)
         - MANAGER:               Overview (/manager/overview), Revenue (/manager/reports/revenue),
                                  Revenue by package (/manager/reports/revenue-by-package)
         - MANAGER, RECEPTIONIST: Members (/staff/members), Expiring soon (/staff/members/expiring),
                                  Packages (/staff/packages), Registrations (/staff/registrations),
                                  New registration (/staff/registrations/create) --%>
    <%-- TODO: Show the user's full name + role (c:out), a Change password link (/change-password)
         and a POST form to /logout with a Logout button. --%>
</nav>
