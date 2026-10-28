<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${not empty requestScope.error}">
    <div class="alert alert-danger" role="alert"><c:out value="${requestScope.error}"/></div>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <div class="alert alert-danger" role="alert"><c:out value="${sessionScope.flashError}"/></div>
    <c:remove var="flashError" scope="session"/>
</c:if>
<c:if test="${not empty requestScope.message}">
    <div class="alert alert-success" role="status"><c:out value="${requestScope.message}"/></div>
</c:if>
<c:if test="${not empty sessionScope.flashMessage}">
    <div class="alert alert-success" role="status"><c:out value="${sessionScope.flashMessage}"/></div>
    <c:remove var="flashMessage" scope="session"/>
</c:if>
