<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%--
    Role-based navigation bar. Hiding menu items is only a UI aid; permissions are always re-checked on the backend (CN-05).
    Request attribute: currentAccount (loaded by AuthenticationFilter).
--%>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/home">Gym Management</a>
    <%-- TODO: Show the menu only when the user is logged in (currentAccount is not empty). --%>
    <%-- TODO: Show menu items by role (matrix in SRS section 2.3):
         - Administrator, Manager: Accounts (/accounts)
         - Owner, Manager, Receptionist: Members (/members), Visit history (/visits)
         - Owner, Manager: Plan types (/plans), Member statistics (/reports/members), Purchases (/reports/purchases)
         - Manager, Receptionist: Check-in (/visits/check-in), Check-out (/visits/check-out)
         - Owner, Manager, Receptionist, Accountant: Transactions (/transactions)
         - Owner, Manager, Accountant: Revenue (/reports/revenue) --%>
    <%-- TODO: Show the user's full name + role (use c:out), a Change password link (/account/change-password)
         and a POST form to /logout with a Logout button. --%>
</nav>
