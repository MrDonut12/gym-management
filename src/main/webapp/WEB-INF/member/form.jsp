<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create/update form for member profiles (CN-12, CN-14, HL-01, HL-02, HL-04 to HL-06).
    Servlets: MemberCreateServlet (/staff/members/create), MemberEditServlet (/staff/members/edit). Roles: MANAGER, RECEPTIONIST.
    Request attributes: mode (create|edit), member (Member), genders (Gender[]), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Member profile" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Member profile</h1>
<%-- TODO: action = /staff/members/create or /staff/members/edit depending on mode; on edit show the code (read-only). --%>
<form method="post" action="" onsubmit="return validateMemberForm(this);">
    <input type="hidden" name="id" value="">

    <label for="fullName">Full name * (2-100 characters, no digits)</label>
    <input type="text" id="fullName" name="fullName" maxlength="100" required>

    <label for="phone">Phone * (10 digits, starting with 0)</label>
    <input type="text" id="phone" name="phone" maxlength="10" pattern="0[0-9]{9}" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" maxlength="254">

    <label for="dateOfBirth">Date of birth</label>
    <input type="date" id="dateOfBirth" name="dateOfBirth" min="1900-01-01">

    <label for="gender">Gender</label>
    <select id="gender" name="gender">
        <option value="">-- Not provided --</option>
        <%-- TODO: c:forEach over genders: value = name() (MALE/FEMALE/OTHER), text = label (Nam/Nữ/Khác). --%>
    </select>

    <%-- TODO: Re-fill values (c:out) when editing or after an error; show fieldErrors next to each field
         (a duplicate phone shows the code of the profile using it). --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/staff/members">Cancel</a>
</form>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
