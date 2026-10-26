<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%--
    Shared message area (CL-11).
    Request attributes: error (String), message (String).
    Session attributes (flash after Post/Redirect/Get, CL-07): flashError, flashMessage - shown once, then removed.
--%>
<%-- TODO: If error / sessionScope.flashError is present -> <div class="alert alert-danger"> with c:out;
     then <c:remove var="flashError" scope="session"/>. --%>
<%-- TODO: If message / sessionScope.flashMessage is present -> <div class="alert alert-success"> with c:out;
     then <c:remove var="flashMessage" scope="session"/>. --%>
