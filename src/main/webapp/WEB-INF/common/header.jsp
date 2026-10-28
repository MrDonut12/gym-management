<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%--
    Shared page header (CL-11). The including page sets requestScope.pageTitle before including this file.
    Bootstrap 5 is served from static files inside the project so the app runs offline (SRS 1.4, CL-11).
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}"/> - Gym Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<a class="visually-hidden-focusable" href="#main-content">Skip to content</a>
<jsp:include page="/WEB-INF/common/navbar.jsp"/>
<main class="container py-4" id="main-content">
<jsp:include page="/WEB-INF/common/messages.jsp"/>
