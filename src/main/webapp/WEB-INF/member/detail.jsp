<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member detail (CN-13). Servlet: MemberDetailServlet (/staff/members/detail). Roles: MANAGER, RECEPTIONIST.
    Request attributes: detail (MemberDetailView).
--%>
<c:set var="pageTitle" value="Member details" scope="request"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<h1>Member details</h1>

<section>
    <h2>Profile</h2>
    <%-- TODO: Code, full name, phone, email, date of birth, gender label, created at (c:out);
         "Soft-deleted" label when detail.member.deleted (independent from the membership status, QT-01). --%>
    <%-- TODO: Membership status today (QT-07) and expiry date; "Expiring soon" warning when detail.expiringSoon (QT-08). --%>
    <%-- TODO: Actions:
         - not deleted: Edit (/staff/members/edit?id=), Create registration (/staff/registrations/create?memberId=)
         - MANAGER and not deleted: POST form to /manager/members/delete with a confirmation dialog (CN-15)
         - MANAGER and deleted: POST form to /manager/members/restore (CN-16) --%>
</section>

<section>
    <h2>Registrations</h2>
    <table class="table">
        <thead>
        <tr><th>Code</th><th>Package (snapshot)</th><th>Amount</th><th>Status</th><th>Created at</th>
            <th>Paid at</th><th>Start</th><th>End</th><th>Cancel reason</th></tr>
        </thead>
        <tbody>
        <%-- TODO: c:forEach over detail.registrations (every status, newest first); amounts with fmt:formatNumber;
             schedule columns empty for PENDING/CANCELLED. --%>
        </tbody>
    </table>
</section>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
