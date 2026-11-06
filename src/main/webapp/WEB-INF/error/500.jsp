<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 500. Declared in web.xml (error-page for code 500 and java.lang.Throwable, CL-08).
    Unexpected error; never show stack traces, SQL errors or sensitive data.
--%>
<c:set var="pageTitle" value="System error" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="text-center py-5">
    <h1 class="h3 mb-3">500 - System error</h1>
    <p class="text-body-secondary mb-4">Something went wrong. Please try again later.</p>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Back to home</a>

</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
