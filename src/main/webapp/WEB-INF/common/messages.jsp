<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%-- Shared contract: error, message in request or one-time session flash. --%>
<c:if test="${not empty requestScope.error or not empty sessionScope.error}">
    <div class="alert alert-error" role="alert"><c:out value="${not empty requestScope.error ? requestScope.error : sessionScope.error}"/></div>
</c:if>
<c:if test="${not empty requestScope.message or not empty sessionScope.message}">
    <div class="alert alert-success" role="status"><c:out value="${not empty requestScope.message ? requestScope.message : sessionScope.message}"/></div>
</c:if>
<c:remove var="error" scope="session"/>
<c:remove var="message" scope="session"/>
