<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create a package / edit name and price (CN-19, CN-20, HL-11 to HL-13). MANAGER only.
    Servlets: PackageCreateServlet (/manager/packages/create), PackageEditServlet (/manager/packages/edit).
    Request attributes: mode (create|edit), gymPackage (GymPackage), price (price text as typed / stored),
                        fieldErrors (keys: name, durationMonths, price), error.
--%>
<c:set var="pageTitle" value="${requestScope.mode == 'edit' ? 'Edit package' : 'Create package'}" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>
<c:set var="isEdit" value="${requestScope.mode == 'edit'}"/>
<c:set var="errors" value="${requestScope.fieldErrors}"/>

<h1 class="h3 mb-3"><c:out value="${pageTitle}"/></h1>
<form method="post" novalidate style="max-width: 32rem;"
      action="${pageContext.request.contextPath}/manager/packages/${isEdit ? 'edit' : 'create'}"
      onsubmit="return validatePackageForm(this);">
    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${requestScope.gymPackage.id}">
    </c:if>

    <div class="mb-3">
        <label class="form-label" for="name">Package name * (2-100 characters)</label>
        <input class="form-control ${not empty errors.name ? 'is-invalid' : ''}" type="text" id="name" name="name"
               maxlength="100" required value="<c:out value="${requestScope.gymPackage.name}"/>">
        <c:if test="${not empty errors.name}"><div class="field-error small"><c:out value="${errors.name}"/></div></c:if>
    </div>

    <div class="mb-3">
        <c:choose>
            <c:when test="${isEdit}">
                <%-- The duration is fixed after creation: shown as text and not submitted (CN-20, KT-15). --%>
                <span class="form-label d-block">Duration</span>
                <div class="form-control-plaintext">
                    <c:out value="${requestScope.gymPackage.durationMonths}"/> month(s)
                    <span class="hint">(cannot be changed)</span>
                </div>
            </c:when>
            <c:otherwise>
                <label class="form-label" for="durationMonths">Duration *</label>
                <select class="form-select ${not empty errors.durationMonths ? 'is-invalid' : ''}" id="durationMonths"
                        name="durationMonths" required>
                    <c:forEach var="months" items="${[1, 3, 6, 12]}">
                        <option value="${months}" ${requestScope.gymPackage.durationMonths == months ? 'selected' : ''}>
                            ${months} ${months == 1 ? 'month' : 'months'}
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${not empty errors.durationMonths}">
                    <div class="field-error small"><c:out value="${errors.durationMonths}"/></div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="mb-3">
        <label class="form-label" for="price">Price (VND) * (integer 1 - 100,000,000)</label>
        <input class="form-control ${not empty errors.price ? 'is-invalid' : ''}" type="text" id="price" name="price"
               inputmode="numeric" pattern="[0-9]+" maxlength="9" required
               value="<c:out value="${requestScope.price}"/>">
        <c:if test="${not empty errors.price}"><div class="field-error small"><c:out value="${errors.price}"/></div></c:if>
    </div>

    <button class="btn btn-primary" type="submit">Save</button>
    <a class="btn btn-link" href="${pageContext.request.contextPath}/staff/packages">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
