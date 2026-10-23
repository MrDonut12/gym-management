package group4.gym.management.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import group4.gym.management.model.Account;
import group4.gym.management.util.SessionUtil;
import group4.gym.management.security.Permission;
import group4.gym.management.security.RbacPolicy;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * Checks permissions by role on the backend, including direct URL access (CN-05, CN-34, GT-01).
 * Detailed scope checks (for example a Manager only manages Receptionist/Accountant) continue in the service layer.
 * Registered in web.xml and runs after AuthenticationFilter.
 */
public class AuthorizationFilter implements Filter {

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
        Account account = SessionUtil.getCurrentAccount(req);
        Permission permission = RbacPolicy.resolvePermission(path, req.getMethod());
        if (account == null || (permission != null && !RbacPolicy.hasPermission(account.getRole(), permission))) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (permission == null && !"/home".equals(path) && !"/logout".equals(path)
                && !"/".equals(path) && !path.isEmpty()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        chain.doFilter(request, response);
    }
}
