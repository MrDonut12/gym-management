<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Error page 500. Declared in web.xml (error-page).
    Unexpected error; never show stack traces or sensitive data.
--%>
<c:set var="pageTitle" value="System error" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>500 - System error</h1>
<%-- TODO: Show a friendly message and a link back to /home. --%>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
