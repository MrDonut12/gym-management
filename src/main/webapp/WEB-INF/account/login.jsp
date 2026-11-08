<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Login screen (FR-01, FR-04, BR-17). Servlet: LoginServlet (/login). Public.
    Request attributes: username (pre-filled from the gym_username cookie or the failed attempt), rememberChecked (boolean), error.
    No reCAPTCHA (FUT-02).
--%>
<c:set var="pageTitle" value="Login" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
        <div class="card">
            <div class="card-body p-4">
                <h1 class="h3 mb-4">Login</h1>
                <form class="d-grid gap-2" method="post" action="${pageContext.request.contextPath}/login" onsubmit="return validateLoginForm(this);">
                    <label class="form-label mt-2 mb-0" for="username">Username</label>
                    <input class="form-control" type="text" id="username" name="username" value="<c:out value='${requestScope.username}'/>" autocomplete="username" maxlength="50" required>

                    <label class="form-label mt-2 mb-0" for="password">Password</label>
                    <%-- The password is never pre-filled. --%>
                    <input class="form-control" type="password" id="password" name="password" autocomplete="current-password" maxlength="64" required>

                    <div class="form-check mt-3">
                        <input class="form-check-input" type="checkbox" id="rememberUsername" name="rememberUsername" value="true" ${rememberChecked ? 'checked' : ''}>
                        <label class="form-check-label" for="rememberUsername">Remember username</label>
                    </div>

                    <button class="btn btn-primary mt-3" type="submit">Login</button>
                </form>

            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
