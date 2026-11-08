package group4.gym.management.filter;

import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import group4.gym.management.service.account.AuthService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Session and authorization filter (FR-02, FR-03, section 3). Registered in web.xml and runs after EncodingFilter.
 * Public paths: /login and static resources (/assets/*). Every other URL requires a valid session.
 * Authorization by URL prefix, fixed in code (no dynamic permission table):
 * <ul>
 *   <li>/admin/*   - ADMIN</li>
 *   <li>/manager/* - MANAGER</li>
 *   <li>/staff/*   - MANAGER, RECEPTIONIST</li>
 *   <li>other protected URLs (/home, /logout, /change-password) - any logged-in role</li>
 * </ul>
 * Finer rules inside one URL (for example only MANAGER may list soft-deleted members, BR-11) are checked in the service layer.
 */
public class AuthFilter implements Filter {

    private final AuthService authService = new AuthService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        if (isPublic(path)) {
            if ("/login".equals(path)) setNoCacheHeaders(res);
            chain.doFilter(request, response);
            return;
        }
        setNoCacheHeaders(res);
        SessionUser user = SessionUtil.getSessionUser(req);
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Account account;
        try {
            account = authService.loadAccount(user.getId());
        } catch (RuntimeException ex) {
            req.getServletContext().log("Unable to validate session account.", ex);
            SessionUtil.invalidate(req);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (account == null || account.getStatus() != AccountStatus.ACTIVE || account.getRole() != user.getRole()) {
            SessionUtil.invalidate(req);
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!isAllowed(path, user.getRole())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * Paths reachable without a session.
     */
    static boolean isPublic(String path) {
        return "/login".equals(path) || path.startsWith("/assets/");
    }

    /**
     * Whether the role may access the path according to its prefix (section 3).
     */
    static boolean isAllowed(String path, Role role) {
        if (role == null) return false;
        if (path.equals("/admin") || path.startsWith("/admin/")) return role == Role.ADMIN;
        if (path.equals("/manager") || path.startsWith("/manager/")) return role == Role.MANAGER;
        if (path.equals("/staff") || path.startsWith("/staff/")) return role == Role.MANAGER || role == Role.RECEPTIONIST;
        return true;
    }

    /**
     * Anti-cache headers for protected pages (FR-02).
     */
    private static void setNoCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
