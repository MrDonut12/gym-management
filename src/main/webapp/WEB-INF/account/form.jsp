<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Account details" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<h1>Account details</h1>
<form method="post" action="${pageContext.request.contextPath}/admin/accounts/${mode == 'edit' ? 'edit' : 'create'}" onsubmit="return validateAccountForm(this);">
    <c:if test="${mode == 'edit'}"><input type="hidden" name="id" value="<c:out value='${account.id}'/>"/></c:if>
    <label for="username">Username (4-50 characters)</label>
    <input id="username" name="username" value="<c:out value='${account.username}'/>" maxlength="50" pattern="[a-zA-Z][a-zA-Z0-9_]{3,49}" required ${mode == 'edit' ? 'readonly' : ''}>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.username}"/></div>
    <c:if test="${mode == 'create'}">
    <label for="password">Initial password</label>
    <input type="password" id="password" name="password" minlength="8" maxlength="64" autocomplete="new-password" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.password}"/></div>
    <label for="confirmPassword">Confirm password</label>
    <input type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64" autocomplete="new-password" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>
    </c:if>
    <label for="fullName">Full name</label>
    <input type="text" id="fullName" name="fullName" value="<c:out value='${account.fullName}'/>" minlength="2" maxlength="100" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.fullName}"/></div>
    <label for="phone">Phone</label>
    <input type="tel" id="phone" name="phone" value="<c:out value='${account.phone}'/>" maxlength="10" pattern="0[0-9]{9}" required>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.phone}"/></div>
    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="<c:out value='${account.email}'/>" maxlength="254">
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.email}"/></div>
    <label for="role">Role</label>
    <c:choose>
        <c:when test="${mode == 'edit' and account.id == sessionScope.sessionUser.id}">
            <input id="role" name="role" value="<c:out value='${sessionScope.sessionUser.role}'/>" readonly>
        </c:when>
        <c:otherwise>
            <select id="role" name="role" required>
                <option value="">Select role</option>
                <c:forEach var="item" items="${roles}">
                    <option value="${item}" ${account.role == item ? 'selected' : ''}><c:out value="${item}"/></option>
                </c:forEach>
            </select>
        </c:otherwise>
    </c:choose>
    <div class="text-danger" role="alert"><c:out value="${fieldErrors.role}"/></div>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
</form>
<jsp:include page="/WEB-INF/common/footer.jsp"/>
