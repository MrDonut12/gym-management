package group4.gym.management.controller.registration;

import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.service.registration.PackageService;
import group4.gym.management.service.registration.RegistrationService;
import group4.gym.management.util.AppConstants;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a PENDING registration (CN-22): choose a member not soft-deleted and a package on sale. MANAGER, RECEPTIONIST.
 * The price is taken from the database, never from the form.
 */
@WebServlet(name = "RegistrationCreateServlet", urlPatterns = {"/staff/registrations/create"})
public class RegistrationCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/create.jsp";

    private final RegistrationService registrationService = new RegistrationService();
    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer memberId = RequestUtil.getPositiveId(request, "memberId");
        String keyword = RequestUtil.getString(request, "keyword");
        showForm(request, response, memberId, keyword, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only the two IDs are read: any price or date parameter is ignored (QT-03).
        Integer memberId = RequestUtil.getPositiveId(request, "memberId");
        Integer packageId = RequestUtil.getPositiveId(request, "packageId");
        if (memberId == null || packageId == null) {
            showForm(request, response, memberId, null, "Please select a member and a package.");
            return;
        }

        int registrationId;
        try {
            registrationId = registrationService.createPending(SessionUtil.getSessionUser(request), memberId, packageId);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (BusinessRuleException ex) {
            showForm(request, response, memberId, null, ex.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath() + "/staff/payments/confirm?id=" + registrationId);
    }

    /**
     * Fill the request and forward to the form. A missing member is a 404; a soft-deleted or invalid input
     * only shows an error and leaves the member unselected.
     */
    private void showForm(HttpServletRequest request, HttpServletResponse response, Integer memberId, String keyword,
            String error) throws ServletException, IOException {
        try {
            if (memberId != null) {
                request.setAttribute("member", registrationService.getSelectableMember(memberId));
            }
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (BusinessRuleException ex) {
            error = ex.getMessage();
        }

        if (keyword != null) {
            try {
                request.setAttribute("members", registrationService.findSelectableMembers(keyword));
            } catch (ValidationException ex) {
                error = ex.getFieldErrors().values().iterator().next();
            }
            request.setAttribute("keyword", keyword);
        }

        request.setAttribute("packages", packageService.listOnSale());
        if (error != null) {
            request.setAttribute(AppConstants.ATTR_ERROR, error);
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
