package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a package (CN-19): name, duration 1/3/6/12 months, price; on sale by default. MANAGER only.
 */
@WebServlet(name = "PackageCreateServlet", urlPatterns = {"/manager/packages/create"})
public class PackageCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/package-form.jsp";

    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read name (getString), durationMonths (getInteger), price (raw text for HL-12);
        //       packageService.createPackage(name, durationMonths, price); ValidationException -> forward with fieldErrors.
        // Output: success: flash + redirect to /staff/packages; failure: forward to VIEW keeping the values.
    }
}
