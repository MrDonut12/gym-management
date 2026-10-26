package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import group4.gym.management.service.registration.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        // TODO: Read status, packageId, memberKeyword, axis, fromDate, toDate (invalid date -> error, HL-16), page -> RegistrationFilter.
        //       registrationService.search(filter) (ValidationException from HL-16 -> show the error with an empty list);
        //       packages = packageService.listPackages(sessionUser, null) for the package filter.
        // Output: forward to VIEW.
    }
}
