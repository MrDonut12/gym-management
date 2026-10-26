package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Package list (CN-18). MANAGER: every package, filter on sale / not on sale, action buttons.
 * RECEPTIONIST: on-sale packages only, no action buttons. MANAGER, RECEPTIONIST.
 */
@WebServlet(name = "PackageListServlet", urlPatterns = {"/staff/packages"})
public class PackageListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/package-list.jsp";

    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read active filter ("" = all, "true", "false"); packageService.listPackages(sessionUser, activeFilter);
        //       set packages and the filter on the request.
        // Output: forward to VIEW.
    }
}
