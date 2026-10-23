package group4.gym.management.controller.auth;

import group4.gym.management.model.Account;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.util.CookieUtil;
import group4.gym.management.util.AppConstants;

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
        response.setHeader("Cache-Control", "no-store");
        Integer id = SessionUtil.getAccountId(request);
        if (id != null) {
            if (authService.loadActiveAccount(id) != null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            SessionUtil.invalidate(request);
        }
        String username = CookieUtil.getRememberedUsername(request);
        request.setAttribute("username", username);
        request.setAttribute("rememberChecked", username != null);
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean remember = "true".equals(request.getParameter("rememberUsername"));
        Account account = authService.authenticate(username, password);
        if (account == null) {
            request.setAttribute("username", username);
            request.setAttribute("rememberChecked", remember);
            request.setAttribute(AppConstants.ATTR_ERROR, "Tên đăng nhập hoặc mật khẩu không hợp lệ.");
            request.getRequestDispatcher(VIEW).forward(request, response);
            return;
        }
        SessionUtil.login(request, account);
        if (remember) CookieUtil.addRememberUsernameCookie(request, response, account.getUsername());
        else CookieUtil.clearRememberUsernameCookie(request, response);
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
