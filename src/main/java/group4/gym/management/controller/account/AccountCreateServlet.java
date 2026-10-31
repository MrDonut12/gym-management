package group4.gym.management.controller.account;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.Role;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a staff account (CN-06). ADMIN only.
 */
@WebServlet(name = "AccountCreateServlet", urlPatterns = {"/admin/accounts/create"})
public class AccountCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.setAttribute("roles", new Role[]{Role.MANAGER, Role.RECEPTIONIST});
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = new Account();
        account.setUsername(RequestUtil.getRawString(request, "username"));
        account.setFullName(RequestUtil.getString(request, "fullName"));
        account.setPhone(RequestUtil.getString(request, "phone"));
        account.setEmail(RequestUtil.getString(request, "email"));
        account.setRole(RequestUtil.getEnum(request, "role", Role.class));
        try {
            accountService.createAccount(SessionUtil.getSessionUser(request), account,
                    RequestUtil.getRawString(request, "password"), RequestUtil.getRawString(request, "confirmPassword"));
            SessionUtil.flashMessage(request, "Account saved.");
            response.sendRedirect(request.getContextPath() + "/admin/accounts");
            return;
        } catch (ValidationException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("fieldErrors", ex.getFieldErrors());
        } catch (BusinessRuleException ex) {
            request.setAttribute("error", ex.getMessage());
        }
        account.setPasswordHash(null);
        request.setAttribute("account", account);
        doGet(request, response);
    }
}
