<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Account details" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
        <div class="card">
            <div class="card-body p-4">
                <h1 class="h3 mb-4">Account details</h1>
                <form class="d-grid gap-2" method="post" action="${pageContext.request.contextPath}/admin/accounts/${mode == 'edit' ? 'edit' : 'create'}" onsubmit="return validateAccountForm(this);">
                    <c:if test="${mode == 'edit'}"><input type="hidden" name="id" value="<c:out value='${account.id}'/>"/></c:if>
                        <label class="form-label mt-2 mb-0" for="username">Username (4-50 characters)</label>
                        <input class="form-control" id="username" name="username" value="<c:out value='${account.username}'/>" maxlength="50" pattern="[a-zA-Z][a-zA-Z0-9_]{3,49}" required ${mode == 'edit' ? 'readonly' : ''}>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.username}"/></div>
                    <c:if test="${mode == 'create'}">
                        <label class="form-label mt-2 mb-0" for="password">Initial password</label>
                        <input class="form-control" type="password" id="password" name="password" minlength="8" maxlength="64" autocomplete="new-password" required>
                        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.password}"/></div>
                        <label class="form-label mt-2 mb-0" for="confirmPassword">Confirm password</label>
                        <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" minlength="8" maxlength="64" autocomplete="new-password" required>
                        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.confirmPassword}"/></div>
                    </c:if>
                    <label class="form-label mt-2 mb-0" for="fullName">Full name</label>
                    <input class="form-control" type="text" id="fullName" name="fullName" value="<c:out value='${account.fullName}'/>" minlength="2" maxlength="100" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.fullName}"/></div>
                    <label class="form-label mt-2 mb-0" for="phone">Phone</label>
                    <input class="form-control" type="tel" id="phone" name="phone" value="<c:out value='${account.phone}'/>" maxlength="10" pattern="0[0-9]{9}" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.phone}"/></div>
                    <label class="form-label mt-2 mb-0" for="email">Email</label>
                    <input class="form-control" type="email" id="email" name="email" value="<c:out value='${account.email}'/>" maxlength="254">
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.email}"/></div>
                    <label class="form-label mt-2 mb-0" for="role">Role</label>
                    <c:choose>
                        <c:when test="${mode == 'edit' and account.id == sessionScope.sessionUser.id}">
                            <input class="form-control" id="role" name="role" value="<c:out value='${sessionScope.sessionUser.role}'/>" readonly>
                        </c:when>
                        <c:otherwise>
                            <select class="form-select" id="role" name="role" required>
                                <option value="">Select role</option>
                                <c:forEach var="item" items="${roles}">
                                    <option value="${item}" ${account.role == item ? 'selected' : ''}><c:out value="${item}"/></option>
                                </c:forEach>
                            </select>
                        </c:otherwise>
                    </c:choose>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.role}"/></div>
                    <button class="btn btn-primary mt-3" type="submit">Save</button>
                    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/accounts">Cancel</a>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
