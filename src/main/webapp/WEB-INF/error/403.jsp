<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 403. Declared in web.xml (error-page).
    The action exceeds the permissions of the RBAC matrix (CN-05, CN-34).
--%>
<c:set var="pageTitle" value="Access denied" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>403 - Access denied</h1>
<p>You do not have permission to access this page.</p>
<a href="${pageContext.request.contextPath}/home">Back to home</a>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
