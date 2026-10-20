package group4.gym.management.controller.account;

import group4.gym.management.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a staff account (CN-04, CN-35). A Manager can only create Receptionist/Accountant accounts.
 */
@WebServlet(name = "AccountCreateServlet", urlPatterns = {"/accounts/create"})
public class AccountCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/account/form.jsp";

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Set mode = create and the roles the actor may assign on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read username, password, confirmPassword, fullName, phone, email, role -> Account.
        //       accountService.createAccount; catch ValidationException/AccessDeniedException/BusinessRuleException.
        // Output: success: redirect to /accounts; failure: forward to VIEW keeping the entered data (except passwords) with errors.
    }
}
