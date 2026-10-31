package group4.gym.management.controller.registration;

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
        String active = RequestUtil.getString(request, "active");
        Boolean activeFilter = null;
        if ("true".equals(active)) {
            activeFilter = Boolean.TRUE;
        } else if ("false".equals(active)) {
            activeFilter = Boolean.FALSE;
        } else {
            active = "";
        }

        request.setAttribute("packages", packageService.listPackages(SessionUtil.getSessionUser(request), activeFilter));
        request.setAttribute("active", active);
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
