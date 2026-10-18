<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Plan type catalog (CN-09). Servlet: PlanTypeListServlet (/plans). Roles: Owner, Manager.
    Request attributes: planTypes (List<PlanType>).
--%>
<c:set var="pageTitle" value="Plan types" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Plan types</h1>
<%-- TODO: Show the "Create plan type" button only for Owner. --%>
<a href="${pageContext.request.contextPath}/plans/create">Create plan type</a>

<table class="table">
    <thead><tr><th>ID</th><th>Plan name</th><th>Duration</th><th>Price (VND)</th><th></th></tr></thead>
    <tbody>
    <%-- TODO: c:forEach over planTypes; format the price with fmt:formatNumber; Edit name/price link (/plans/edit?id=).
         No delete/retire button. --%>
    </tbody>
</table>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
