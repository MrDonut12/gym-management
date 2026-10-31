package group4.gym.management.controller.registration;

import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.registration.PackageService;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
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
        Integer id = RequestUtil.getPositiveId(request, "id");
        String action = RequestUtil.getString(request, "action");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!"activate".equals(action) && !"deactivate".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean activate = "activate".equals(action);
        try {
            packageService.setActive(id, activate);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        SessionUtil.flashMessage(request, activate ? "Đã mở bán lại gói tập" : "Đã ngừng bán gói tập");
        response.sendRedirect(request.getContextPath() + "/staff/packages");
    }
}
