package group4.gym.management.controller.registration;

import group4.gym.management.exception.NotFoundException;
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
 * Edit package name and price (CN-20). The duration is read-only. MANAGER only.
 */
@WebServlet(name = "PackageEditServlet", urlPatterns = {"/manager/packages/edit"})
public class PackageEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/registration/package-form.jsp";

    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        GymPackage gymPackage;
        try {
            gymPackage = packageService.getById(id);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("mode", "edit");
        request.setAttribute("gymPackage", gymPackage);
        request.setAttribute("price", String.valueOf(gymPackage.getPrice()));
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // Any durationMonths parameter is deliberately never read (KT-15).
        String name = RequestUtil.getString(request, "name");
        String price = RequestUtil.getRawString(request, "price");

        try {
            packageService.updateNameAndPrice(id, name, price);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (ValidationException ex) {
            // Reload the stored package so the read-only duration is shown, then overlay what was typed.
            GymPackage gymPackage;
            try {
                gymPackage = packageService.getById(id);
            } catch (NotFoundException notFound) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            gymPackage.setName(name);
            request.setAttribute("mode", "edit");
            request.setAttribute("gymPackage", gymPackage);
            request.setAttribute("price", price);
            request.setAttribute(AppConstants.ATTR_FIELD_ERRORS, ex.getFieldErrors());
            request.setAttribute(AppConstants.ATTR_ERROR, ex.getMessage());
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }

        SessionUtil.flashMessage(request, "Đã cập nhật gói tập");
        response.sendRedirect(request.getContextPath() + "/staff/packages");
    }
}
