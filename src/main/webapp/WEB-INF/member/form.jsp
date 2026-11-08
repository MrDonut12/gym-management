<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create/update form for member profiles.
    Servlets: MemberCreateServlet (/staff/members/create), MemberEditServlet (/staff/members/edit). Roles: MANAGER, RECEPTIONIST.
    Request attributes: mode (create|edit), member (Member), genders (Gender[]), fieldErrors, error.
    After a failed POST the date of birth is re-filled from the submitted parameter, so an invalid value stays visible.
--%>
<c:set var="isEdit" value="${mode == 'edit'}"/>
<c:set var="pageTitle" value="${isEdit ? 'Edit member' : 'Create member'}" scope="request"/>
<c:set var="dateOfBirthValue" value="${not empty param.dateOfBirth ? param.dateOfBirth : member.dateOfBirth}"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
        <div class="card">
            <div class="card-body p-4">
                <h1 class="h3 mb-4">${isEdit ? 'Edit member' : 'Create member'}</h1>
                <form class="d-grid gap-2" method="post"
                      action="${pageContext.request.contextPath}/staff/members/${isEdit ? 'edit' : 'create'}"
                      onsubmit="return validateMemberForm(this);">
                    <c:if test="${isEdit}">
                        <input type="hidden" name="id" value="<c:out value='${member.id}'/>">
                        <label class="form-label mb-0" for="code">ID</label>
                        <input class="form-control" id="code" value="<c:out value='${member.code}'/>" readonly>
                    </c:if>

                    <label class="form-label mt-2 mb-0" for="fullName">Full name * (2-100 characters, no digits)</label>
                    <input class="form-control" type="text" id="fullName" name="fullName" value="<c:out value='${member.fullName}'/>"
                           minlength="2" maxlength="100" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.fullName}"/></div>

                    <label class="form-label mt-2 mb-0" for="phone">Phone * (10 digits, starting with 0)</label>
                    <input class="form-control" type="tel" id="phone" name="phone" value="<c:out value='${member.phone}'/>"
                           maxlength="10" pattern="0[0-9]{9}" required>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.phone}"/></div>

                    <label class="form-label mt-2 mb-0" for="email">Email</label>
                    <input class="form-control" type="email" id="email" name="email" value="<c:out value='${member.email}'/>" maxlength="254">
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.email}"/></div>

                    <label class="form-label mt-2 mb-0" for="dateOfBirth">Date of birth</label>
                    <input class="form-control" type="date" id="dateOfBirth" name="dateOfBirth" value="<c:out value='${dateOfBirthValue}'/>" min="1900-01-01">
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.dateOfBirth}"/></div>

                    <label class="form-label mt-2 mb-0" for="gender">Gender</label>
                    <select class="form-select" id="gender" name="gender">
                        <option value="">-- Not provided --</option>
                        <c:forEach var="item" items="${genders}">
                            <option value="${item}" ${member.gender == item ? 'selected' : ''}><c:out value="${item.label}"/></option>
                        </c:forEach>
                    </select>
                    <div class="small text-danger" role="alert"><c:out value="${fieldErrors.gender}"/></div>

                    <button class="btn btn-primary mt-3" type="submit">Save</button>
                    <c:choose>
                        <c:when test="${isEdit}">
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members/detail?id=${member.id}">Cancel</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/staff/members">Cancel</a>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
