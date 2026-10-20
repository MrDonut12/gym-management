<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Form to create a plan type (Owner) / edit name and price (Owner, Manager) (CN-09).
    Servlets: PlanTypeCreateServlet (/plans/create), PlanTypeEditServlet (/plans/edit).
    Request attributes: mode (create|edit), planType (PlanType), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Plan type" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Plan type</h1>
<%-- TODO: action = /plans/create or /plans/edit depending on mode. --%>
<form method="post" action="" onsubmit="return validatePlanTypeForm(this);">
    <input type="hidden" name="id" value="">

    <label for="name">Plan name *</label>
    <input type="text" id="name" name="name" maxlength="100">

    <label for="duration">Duration *</label>
    <%-- TODO: When mode = create: a select over the fixed catalog. When mode = edit: display only, do not submit. --%>
    <select id="duration" name="duration">
        <option value="1_DAY">1 day (Daily Pass)</option>
        <option value="1_MONTH">1 month (Monthly Pass)</option>
        <option value="6_MONTH">6 months (6-Month Pass)</option>
        <option value="1_YEAR">1 year (Yearly Pass)</option>
    </select>

    <label for="price">Price (VND) * (integer &gt; 0)</label>
    <input type="number" id="price" name="price" min="1" step="1">

    <%-- TODO: Show fieldErrors. --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/plans">Cancel</a>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
