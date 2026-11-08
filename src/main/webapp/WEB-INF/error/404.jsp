<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 404. Declared in web.xml (error-page, NFR-08).
    The path does not exist, or an ID in the URL/form is invalid or unknown (VR-17).
--%>
<c:set var="pageTitle" value="Not found" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="text-center py-5">
<h1 class="h3 mb-3">404 - Not found</h1>
<p class="text-body-secondary mb-4">The page or record you requested could not be found.</p>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Back to home</a>

</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
