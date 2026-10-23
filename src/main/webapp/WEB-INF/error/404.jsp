<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 404. Declared in web.xml (error-page).
    The path or data does not exist.
--%>
<c:set var="pageTitle" value="Not found" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>404 - Not found</h1>
<p>The requested page could not be found.</p>
<a href="${pageContext.request.contextPath}/home">Back to home</a>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
