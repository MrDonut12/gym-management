package group4.gym.management.controller.account;

import group4.gym.management.service.account.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login (CN-01) and the remember-username cookie (CN-04, QT-17). Public.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/account/login.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Already logged in -> redirect to /home.
        //       CookieUtil.getRememberedUsername (invalid values are ignored); if present, set username and rememberChecked = true.
        // Output: forward to VIEW (the password is never pre-filled).

        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: username/password via RequestUtil.getRawString (not trimmed); rememberUsername checkbox.
        //       authService.authenticate == null -> error = AppConstants.MSG_LOGIN_FAILED, keep username,
        //       do NOT create/refresh/delete the cookie, forward to VIEW.
        //       Success -> SessionUtil.login (changes the session ID);
        //       checked -> CookieUtil.addRememberUsernameCookie, unchecked -> CookieUtil.clearRememberUsernameCookie; redirect to /home.
        // Output: redirect to /home on success; forward to VIEW with the generic error on failure.
    }
}
