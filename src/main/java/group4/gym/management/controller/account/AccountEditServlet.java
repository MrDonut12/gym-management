package group4.gym.management.controller.account;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.Role;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Edit full name, phone, email and role (CN-07). The username is read-only; an ADMIN cannot change their own role (QT-14).
 * ADMIN only.
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/admin/accounts/edit"})
public class AccountEditServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = RequestUtil.getPositiveId(request, "id");
        if (id == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            request.setAttribute("account", accountService.getById(id));
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("mode", "edit");
        request.setAttribute("roles", new Role[]{Role.MANAGER, Role.RECEPTIONIST});
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
        Account account;
        try {
            account = accountService.getById(id);
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        account.setFullName(RequestUtil.getString(request, "fullName"));
        account.setPhone(RequestUtil.getString(request, "phone"));
        account.setEmail(RequestUtil.getString(request, "email"));
        account.setRole(RequestUtil.getEnum(request, "role", Role.class));
        try {
            accountService.updateAccount(SessionUtil.getSessionUser(request), account);
            SessionUtil.flashMessage(request, "Account saved.");
            response.sendRedirect(request.getContextPath() + "/admin/accounts");
            return;
        } catch (ValidationException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("fieldErrors", ex.getFieldErrors());
        } catch (BusinessRuleException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (NotFoundException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("account", account);
        request.setAttribute("mode", "edit");
        request.setAttribute("roles", new Role[]{Role.MANAGER, Role.RECEPTIONIST});
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
