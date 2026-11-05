package group4.gym.management.controller.account;

import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Change own password (CN-09). Every role; the current session is kept.
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/change-password.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            accountService.changeOwnPassword(SessionUtil.getSessionUser(request),
                    RequestUtil.getRawString(request, "currentPassword"),
                    RequestUtil.getRawString(request, "newPassword"),
                    RequestUtil.getRawString(request, "confirmPassword"));
            SessionUtil.flashMessage(request, "Password changed.");
            response.sendRedirect(request.getContextPath() + "/change-password");
        } catch (ValidationException ex) {
            request.setAttribute("fieldErrors", ex.getFieldErrors());
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher(VIEW).forward(request, response);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
