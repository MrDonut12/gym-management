<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create/edit form for staff accounts (CN-04, CN-35, DL-07).
    Servlets: AccountCreateServlet (/accounts/create), AccountEditServlet (/accounts/edit).
    Request attributes: mode (create|edit), account (Account), assignableRoles (List<Role>), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Account details" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Account details</h1>
<%-- TODO: action = /accounts/create when mode = create, /accounts/edit when mode = edit. --%>
<form method="post" action="" onsubmit="return validateAccountForm(this);">
    <%-- TODO: hidden id when mode = edit. --%>
    <input type="hidden" name="id" value="">

    <label for="username">Username *</label>
    <%-- TODO: editable only on create; read-only on edit. --%>
    <input type="text" id="username" name="username" maxlength="50">

    <%-- TODO: Show the two password fields only when mode = create. --%>
    <label for="password">Password *</label>
    <input type="password" id="password" name="password">
    <label for="confirmPassword">Confirm password *</label>
    <input type="password" id="confirmPassword" name="confirmPassword">

    <label for="fullName">Full name *</label>
    <input type="text" id="fullName" name="fullName" maxlength="100">

    <label for="phone">Phone * (10 digits)</label>
    <input type="text" id="phone" name="phone" maxlength="10">

    <label for="email">Email</label>
    <input type="email" id="email" name="email" maxlength="254">

    <label for="role">Role *</label>
    <select id="role" name="role">
        <%-- TODO: c:forEach over assignableRoles. A Manager only sees Receptionist/Accountant;
             on edit, only an Administrator may change the role. --%>
    </select>

    <%-- TODO: Show fieldErrors next to each field. --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/accounts">Cancel</a>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
