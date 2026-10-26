package group4.gym.management.controller.account;

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
        // TODO: Read keyword, role, status; accountService.listAccounts; set accounts and the filters on the request.
        // Output: forward to VIEW.
    }
}
