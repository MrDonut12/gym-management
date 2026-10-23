package group4.gym.management.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import group4.gym.management.model.Account;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.util.AppConstants;

import group4.gym.management.service.AuthService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * Checks the login and the account status on EVERY protected request (CN-03, CN-04, session table in section 3.1.1).
 * Registered in web.xml and runs after EncodingFilter.
 */
public class AuthenticationFilter implements Filter {

    private final AuthService authService = new AuthService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        if ("/login".equals(path) || path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }
        res.setHeader("Cache-Control", "no-store");
        Integer id = SessionUtil.getAccountId(req);
        Account account = id == null ? null : authService.loadActiveAccount(id);
        if (account == null) {
            SessionUtil.invalidate(req);
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))
                    || (req.getHeader("Accept") != null && req.getHeader("Accept").contains("application/json"))) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                res.sendRedirect(req.getContextPath() + "/login");
            }
            return;
        }
        req.setAttribute(AppConstants.REQUEST_CURRENT_ACCOUNT, account);
        chain.doFilter(request, response);
    }
}
