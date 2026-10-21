<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Change own password (CN-36). Servlet: ChangePasswordServlet (/account/change-password). Every role.
    Request attributes: fieldErrors (Map), error, message.
--%>
<c:set var="pageTitle" value="Change password" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Change password</h1>
<form method="post" action="${pageContext.request.contextPath}/account/change-password" onsubmit="return validatePasswordForm(this);">
    <label for="currentPassword">Current password</label>
    <input type="password" id="currentPassword" name="currentPassword">

    <label for="newPassword">New password</label>
    <input type="password" id="newPassword" name="newPassword">

    <label for="confirmPassword">Confirm new password</label>
    <input type="password" id="confirmPassword" name="confirmPassword">

    <%-- TODO: Show fieldErrors next to each field. --%>
    <button type="submit">Save</button>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
