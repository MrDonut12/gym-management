<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member detail and membership status (CN-12). Servlet: MemberDetailServlet (/members/detail).
    Request attributes: memberStatus (MemberStatusView).
--%>
<c:set var="pageTitle" value="Member details" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<h1>Member details</h1>

<section>
    <h2>Profile</h2>
    <%-- TODO: Code, full name, phone, email, date of birth, gender, created at; show the profile status
         (not soft-deleted / soft-deleted) independently from the membership status. --%>
    <%-- TODO: Edit button (/members/edit?id=, Manager/Receptionist, profile not soft-deleted);
         POST form to /members/delete (Owner/Manager); Register/renew button (/memberships/create?memberId=). --%>
</section>

<section>
    <h2>Plan registrations</h2>
    <table class="table">
        <thead>
        <tr><th>Registration ID</th><th>Plan (snapshot)</th><th>Start</th><th>Last training day</th>
            <th>Status</th><th>Transaction</th><th>Warning</th></tr>
        </thead>
        <tbody>
        <%-- TODO: c:forEach over memberStatus.memberships; status label per MembershipState
             (Pending payment / Cancelled / Paid, upcoming / Active / Expired);
             leave the schedule columns empty for PENDING/CANCELLED; show "Expiring soon" when expiringSoon;
             link to /transactions/detail?id=. --%>
        </tbody>
    </table>
</section>

<section>
    <h2>Current visit</h2>
    <%-- TODO: Show the OPEN visit (if any) with its check-in time; link to the history /visits?memberId=. --%>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
