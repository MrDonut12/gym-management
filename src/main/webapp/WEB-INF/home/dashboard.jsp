<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Home page after login. Servlet: HomeServlet (/home).
    Request attributes: currentAccount.
--%>
<c:set var="pageTitle" value="Home" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Home</h1>
<%-- TODO: Greet the user by full name (c:out) and show shortcuts to the features available to the user's role. --%>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
