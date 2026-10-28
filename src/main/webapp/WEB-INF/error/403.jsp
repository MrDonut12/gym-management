<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 403. Declared in web.xml (error-page, CL-08).
    The action is a "K" cell of the permission matrix (section 3); no data was changed.
--%>
<c:set var="pageTitle" value="Access denied" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>403 - Access denied</h1>
<p>You do not have permission to access this page.</p>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Back to home</a>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
