package group4.gym.management.controller.auth;

import group4.gym.management.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login (CN-01) and the remember-username cookie (CN-37).
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/auth/login.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: If already logged in -> redirect to /home.
        //       Read CookieUtil.getRememberedUsername; if present, set username and rememberChecked = true on the request (the password must still be entered).
        // Output: forward to VIEW (login form).
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read username, password, rememberUsername.
        //       Validate required fields on the backend (CN-30).
        //       authService.authenticate; on failure -> generic error message, do NOT create/refresh the cookie, forward to VIEW.
        //       On success -> SessionUtil.login (changes the session ID);
        //       if rememberUsername is checked -> CookieUtil.addRememberUsernameCookie, otherwise CookieUtil.clearRememberUsernameCookie.
        //       Redirect to /home.
        // Output: redirect to /home on success; forward to VIEW with an error on failure.
    }
}
