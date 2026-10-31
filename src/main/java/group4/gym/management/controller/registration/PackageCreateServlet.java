package group4.gym.management.controller.registration;

import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.registration.GymPackage;
import group4.gym.management.service.registration.PackageService;
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
 * Create a package (CN-19): name, duration 1/3/6/12 months, price; on sale by default. MANAGER only.
 */
@WebServlet(name = "PackageCreateServlet", urlPatterns = {"/manager/packages/create"})
public class PackageCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/package-form.jsp";

    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = RequestUtil.getString(request, "name");
        Integer durationMonths = RequestUtil.getInteger(request, "durationMonths");
        String price = RequestUtil.getRawString(request, "price");

        try {
            packageService.createPackage(name, durationMonths, price);
        } catch (ValidationException ex) {
            // Keep the entered values; the price stays as typed text because it may not be a valid number.
            GymPackage entered = new GymPackage();
            entered.setName(name);
            entered.setDurationMonths(durationMonths == null ? 0 : durationMonths);
            request.setAttribute("mode", "create");
            request.setAttribute("gymPackage", entered);
            request.setAttribute("price", price);
            request.setAttribute(AppConstants.ATTR_FIELD_ERRORS, ex.getFieldErrors());
            request.setAttribute(AppConstants.ATTR_ERROR, ex.getMessage());
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }

        SessionUtil.flashMessage(request, "Đã tạo gói tập");
        response.sendRedirect(request.getContextPath() + "/staff/packages");
    }
}
