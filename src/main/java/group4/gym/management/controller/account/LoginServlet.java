package group4.gym.management.controller.account;

import group4.gym.management.model.account.Account;
import group4.gym.management.util.AppConstants;
import group4.gym.management.util.CookieUtil;
import group4.gym.management.util.RequestUtil;
import group4.gym.management.util.SessionUtil;

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
        if (SessionUtil.getSessionUser(request) != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String username = CookieUtil.getRememberedUsername(request);
        request.setAttribute("username", username);
        request.setAttribute("rememberChecked", username != null);
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = RequestUtil.getRawString(request, "username");
        boolean remember = "true".equals(request.getParameter("rememberUsername"));
        Account account = authService.authenticate(username, RequestUtil.getRawString(request, "password"));
        if (account == null) {
            request.setAttribute("error", AppConstants.MSG_LOGIN_FAILED);
            request.setAttribute("username", username);
            request.setAttribute("rememberChecked", remember);
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }
        SessionUtil.login(request, account);
        if (remember) CookieUtil.addRememberUsernameCookie(request, response, account.getUsername());
        else CookieUtil.clearRememberUsernameCookie(request, response);
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
