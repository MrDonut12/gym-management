<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create/edit form for staff accounts (CN-06, CN-07, DL-01, HL-01, HL-03, HL-04, HL-08 to HL-10).
    Servlets: AccountCreateServlet (/admin/accounts/create), AccountEditServlet (/admin/accounts/edit). ADMIN only.
    Request attributes: mode (create|edit), account (Account), roles (Role[]), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Account details" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Account details</h1>
<%-- TODO: action = /admin/accounts/create when mode = create, /admin/accounts/edit when mode = edit. --%>
<form method="post" action="" onsubmit="return validateAccountForm(this);">
    <%-- TODO: hidden id when mode = edit. --%>
    <input type="hidden" name="id" value="">

    <label for="username">Username * (4-50 characters, letters/digits/_, starts with a letter)</label>
    <%-- TODO: editable only on create; read-only text on edit (the username cannot be changed). --%>
    <input type="text" id="username" name="username" maxlength="50" pattern="[a-zA-Z][a-zA-Z0-9_]{3,49}">

    <%-- TODO: Show the two password fields only when mode = create; never re-fill them. --%>
    <label for="password">Initial password * (8-64 characters)</label>
    <input type="password" id="password" name="password" minlength="8" maxlength="64">
    <label for="confirmPassword">Confirm password *</label>
    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64">

    <label for="fullName">Full name *</label>
    <input type="text" id="fullName" name="fullName" maxlength="100">

    <label for="phone">Phone * (10 digits, starting with 0)</label>
    <input type="text" id="phone" name="phone" maxlength="10" pattern="0[0-9]{9}">

    <label for="email">Email</label>
    <input type="email" id="email" name="email" maxlength="254">

    <label for="role">Role *</label>
    <select id="role" name="role">
        <%-- TODO: c:forEach over roles. On edit of the current user's own account, the role is read-only (QT-14). --%>
    </select>

    <%-- TODO: Re-fill values with c:out; show fieldErrors next to each field. --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
