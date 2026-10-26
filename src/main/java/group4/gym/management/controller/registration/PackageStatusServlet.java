package group4.gym.management.controller.registration;

import group4.gym.management.service.registration.PackageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Stop selling / resume selling a package (CN-21, QT-04). Packages are never deleted. MANAGER only, POST only.
 */
@WebServlet(name = "PackageStatusServlet", urlPatterns = {"/manager/packages/status"})
public class PackageStatusServlet extends HttpServlet {

    private final PackageService packageService = new PackageService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id and action (deactivate | activate); packageService.setActive(id, "activate".equals(action)).
        // Output: redirect to /staff/packages with a flash message.
    }
}
