<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create a package / edit name and price (CN-19, CN-20, HL-11 to HL-13). MANAGER only.
    Servlets: PackageCreateServlet (/manager/packages/create), PackageEditServlet (/manager/packages/edit).
    Request attributes: mode (create|edit), gymPackage (GymPackage), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Package" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Package</h1>
<%-- TODO: action = /manager/packages/create or /manager/packages/edit depending on mode. --%>
<form method="post" action="" onsubmit="return validatePackageForm(this);">
    <input type="hidden" name="id" value="">

    <label for="name">Package name * (2-100 characters)</label>
    <input type="text" id="name" name="name" maxlength="100" required>

    <label for="durationMonths">Duration *</label>
    <%-- TODO: mode = create: this select. mode = edit: display the duration as read-only text and do NOT submit it (CN-20). --%>
    <select id="durationMonths" name="durationMonths">
        <option value="1">1 month</option>
        <option value="3">3 months</option>
        <option value="6">6 months</option>
        <option value="12">12 months</option>
    </select>

    <label for="price">Price (VND) * (integer 1 - 100,000,000)</label>
    <input type="text" id="price" name="price" inputmode="numeric" pattern="[0-9]+" maxlength="9" required>

    <%-- TODO: Re-fill values with c:out; show fieldErrors. --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/staff/packages">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
