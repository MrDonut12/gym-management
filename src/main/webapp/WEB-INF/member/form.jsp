<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Create/update form for member profiles (CN-06, CN-08, section 3.4).
    Servlets: MemberCreateServlet (/members/create), MemberEditServlet (/members/edit). Roles: Manager, Receptionist.
    Request attributes: mode (create|edit), member (Member), fieldErrors, error.
--%>
<c:set var="pageTitle" value="Member profile" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Member profile</h1>
<%-- TODO: action = /members/create or /members/edit depending on mode. --%>
<form method="post" action="" onsubmit="return validateMemberForm(this);">
    <input type="hidden" name="id" value="">

    <label for="fullName">Full name * (2-100 characters)</label>
    <input type="text" id="fullName" name="fullName" maxlength="100">

    <label for="phone">Phone * (10 digits, starting with 0)</label>
    <input type="text" id="phone" name="phone" maxlength="10">

    <label for="email">Email</label>
    <input type="email" id="email" name="email" maxlength="254">

    <label for="dateOfBirth">Date of birth</label>
    <input type="date" id="dateOfBirth" name="dateOfBirth">

    <label for="gender">Gender</label>
    <select id="gender" name="gender">
        <option value="">-- Not provided --</option>
        <option value="MALE">Male</option>
        <option value="FEMALE">Female</option>
        <option value="OTHER">Other</option>
    </select>

    <%-- TODO: Re-fill values (c:out) when editing or after an error; show fieldErrors next to each field;
         on a duplicate phone, show a link to the existing profile. --%>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/members">Cancel</a>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
