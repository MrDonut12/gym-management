package group4.gym.management.controller.account;

import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.service.account.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Staff account list (CN-05), the ADMIN home page. ADMIN only.
 */
@WebServlet(name = "AccountListServlet", urlPatterns = {"/admin/accounts"})
public class AccountListServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/list.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = RequestUtil.getString(request, "keyword");
        Role role = RequestUtil.getEnum(request, "role", Role.class);
        AccountStatus status = RequestUtil.getEnum(request, "status", AccountStatus.class);
        request.setAttribute("keyword", keyword);
        request.setAttribute("role", role);
        request.setAttribute("status", status);
        if ((RequestUtil.getString(request, "role") != null && role == null)
                || (RequestUtil.getString(request, "status") != null && status == null)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            request.setAttribute("accounts", accountService.listAccounts(keyword, role, status));
        } catch (ValidationException ex) {
            request.setAttribute("error", ex.getMessage());
            request.setAttribute("fieldErrors", ex.getFieldErrors());
        }
        request.getRequestDispatcher(VIEW).forward(request, response);
    }
}
