<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="group4.gym.management.util.DateTimeUtil"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%--
    Member list and search (CN-11), the RECEPTIONIST home page. Servlet: MemberListServlet (/staff/members).
    Roles: MANAGER, RECEPTIONIST. Plain search, no AJAX (FUT-01).
    Search by full name (partial) or phone (exact); the member code is display-only (team decision).
    Request attributes: page (Page<MemberListItem>), keyword, scope (MemberScope), error, fieldErrors.
--%>
<c:set var="pageTitle" value="Members" scope="request"/>
<c:set var="isManager" value="${sessionScope.sessionUser.role == 'MANAGER'}"/>
<jsp:include page="/WEB-INF/common/header.jsp"/>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
    <h1 class="h3 mb-0">Members</h1>
    <div class="d-flex flex-wrap gap-2">
        <a class="btn btn-outline-warning" href="${pageContext.request.contextPath}/staff/members/expiring">Expiring soon</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/members/create">Create member</a>
    </div>
</div>

<form class="row g-3 align-items-end mb-4" method="get" action="${pageContext.request.contextPath}/staff/members">
    <div class="col-12 ${isManager ? 'col-md-6' : 'col-md-9'}">
        <label class="form-label" for="keyword">Search members</label>
        <input class="form-control" id="keyword" type="text" name="keyword" value="<c:out value='${keyword}'/>"
               maxlength="100" placeholder="Full name or phone number">
        <div class="small text-danger" role="alert"><c:out value="${fieldErrors.keyword}"/></div>
    </div>
    <c:if test="${isManager}">
        <div class="col-12 col-md-3">
            <label class="form-label" for="scope">Profiles</label>
            <select class="form-select" id="scope" name="scope">
                <option value="ACTIVE" ${scope == 'ACTIVE' ? 'selected' : ''}>Managed (not deleted)</option>
                <option value="DELETED" ${scope == 'DELETED' ? 'selected' : ''}>Soft-deleted</option>
                <option value="ALL" ${scope == 'ALL' ? 'selected' : ''}>All</option>
            </select>
        </div>
    </c:if>
    <div class="col-12 col-md-3 d-grid">
        <button class="btn btn-outline-primary" type="submit">Search</button>
    </div>
    <p class="hint mb-0">Full name matches partially; phone must match all 10 digits.</p>
</form>

<div class="table-responsive border rounded">
    <table class="table table-hover align-middle mb-0">
        <caption class="visually-hidden">Members matching the search</caption>
        <thead class="table-light">
            <tr><th scope="col">ID</th><th scope="col">Full name</th><th scope="col">Phone</th>
                <th scope="col">Membership status</th><th scope="col">Expiry date</th><th scope="col">Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${page.items}">
                <tr>
                    <td class="fw-semibold"><c:out value="${item.member.code}"/></td>
                    <td>
                        <c:out value="${item.member.fullName}"/>
                        <c:if test="${item.member.deleted}"><span class="badge text-bg-danger ms-1">Soft-deleted</span></c:if>
                    </td>
                    <td><c:out value="${item.member.phone}"/></td>
                    <td><c:set var="memberStatus" value="${item.status}"/><%@include file="status-badge.jspf"%></td>
                    <td class="text-nowrap">
                        <c:if test="${not empty item.currentEndDate}"><c:out value="${DateTimeUtil.format(item.currentEndDate)}"/></c:if>
                    </td>
                    <td><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/staff/members/detail?id=${item.member.id}">Detail</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty page.items}">
                <tr><td colspan="6" class="text-center text-body-secondary py-5">No members found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<c:if test="${page.totalPages > 1}">
    <nav class="mt-3" aria-label="Member pages">
        <ul class="pagination flex-wrap mb-0">
            <c:choose>
                <c:when test="${page.hasPrevious}">
                    <c:url var="prevUrl" value="/staff/members">
                        <c:param name="keyword" value="${keyword}"/>
                        <c:param name="scope" value="${scope}"/>
                        <c:param name="page" value="${page.page - 1}"/>
                    </c:url>
                    <li class="page-item"><a class="page-link" href="${prevUrl}">Previous</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled"><span class="page-link">Previous</span></li>
                </c:otherwise>
            </c:choose>
            <c:forEach var="number" begin="1" end="${page.totalPages}">
                <c:url var="pageUrl" value="/staff/members">
                    <c:param name="keyword" value="${keyword}"/>
                    <c:param name="scope" value="${scope}"/>
                    <c:param name="page" value="${number}"/>
                </c:url>
                <li class="page-item ${number == page.page ? 'active' : ''}">
                    <a class="page-link" href="${pageUrl}" ${number == page.page ? 'aria-current="page"' : ''}>${number}</a>
                </li>
            </c:forEach>
            <c:choose>
                <c:when test="${page.hasNext}">
                    <c:url var="nextUrl" value="/staff/members">
                        <c:param name="keyword" value="${keyword}"/>
                        <c:param name="scope" value="${scope}"/>
                        <c:param name="page" value="${page.page + 1}"/>
                    </c:url>
                    <li class="page-item"><a class="page-link" href="${nextUrl}">Next</a></li>
                </c:when>
                <c:otherwise>
                    <li class="page-item disabled"><span class="page-link">Next</span></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
    <p class="hint mt-2">Page ${page.page} of ${page.totalPages} (${page.totalItems} members).</p>
</c:if>

<jsp:include page="/WEB-INF/common/footer.jsp"/>
