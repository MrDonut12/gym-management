<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 404. Declared in web.xml (error-page).
    The path or data does not exist.
--%>
<c:set var="pageTitle" value="Not found" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>404 - Not found</h1>
<%-- TODO: Show a friendly message and a link back to /home. --%>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
