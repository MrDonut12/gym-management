package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Edit package name and price (CN-20). The duration is read-only. MANAGER only.
 */
@WebServlet(name = "PackageEditServlet", urlPatterns = {"/manager/packages/edit"})
public class PackageEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/package-form.jsp";

    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: id = getPositiveId (null / NotFoundException -> 404); packageService.getById; set gymPackage, mode = edit.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id, name, price; IGNORE any durationMonths parameter (KT-15);
        //       packageService.updateNameAndPrice(id, name, price).
        // Output: success: flash + redirect to /staff/packages; failure: forward to VIEW with errors.
    }
}
