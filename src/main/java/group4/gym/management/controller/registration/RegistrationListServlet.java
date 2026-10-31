package group4.gym.management.controller.registration;

import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.registration.DateAxis;
import group4.gym.management.model.registration.RegistrationFilter;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.service.registration.PackageService;
import group4.gym.management.service.registration.RegistrationService;
import group4.gym.management.util.AppConstants;
import group4.gym.management.util.Page;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.List;

/**
 * Registration/transaction list (CN-24): filter by status, package, member code or phone, and a date range on the
 * chosen axis (created_at or paid_at, QT-13). 20 rows per page. PENDING rows show Confirm and Cancel.
 * MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "RegistrationListServlet", urlPatterns = {"/staff/registrations"})
public class RegistrationListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/list.jsp";

    private final RegistrationService registrationService = new RegistrationService();
    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegistrationFilter filter = new RegistrationFilter();
        filter.setStatus(RequestUtil.getEnum(request, "status", RegistrationStatus.class));
        filter.setPackageId(RequestUtil.getPositiveId(request, "packageId"));
        filter.setMemberKeyword(RequestUtil.getString(request, "memberKeyword"));
        filter.setAxis(RequestUtil.getEnum(request, "axis", DateAxis.class));
        filter.setPage(RequestUtil.getPage(request));

        String error = null;
        try {
            filter.setFromDate(RequestUtil.getDate(request, "fromDate"));
            filter.setToDate(RequestUtil.getDate(request, "toDate"));
        } catch (DateTimeException ex) {
            // HL-16: an unreadable date is a field error; the list stays empty and the bad date is dropped.
            error = "Date must be a valid date (dd/mm/yyyy).";
            filter.setFromDate(null);
            filter.setToDate(null);
        }

        Page<RegistrationView> page = null;
        if (error == null) {
            try {
                page = registrationService.search(filter);
            } catch (ValidationException ex) {
                error = ex.getFieldErrors().values().iterator().next();
                request.setAttribute(AppConstants.ATTR_FIELD_ERRORS, ex.getFieldErrors());
            }
        }
        if (page == null) {
            page = new Page<>(List.of(), 1, AppConstants.PAGE_SIZE, 0);
        }

        request.setAttribute("page", page);
        request.setAttribute("filter", filter);
        request.setAttribute("packages", packageService.listPackages(SessionUtil.getSessionUser(request), null));
        if (error != null) {
            request.setAttribute(AppConstants.ATTR_ERROR, error);
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
