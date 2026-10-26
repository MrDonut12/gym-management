package group4.gym.management.filter;

import group4.gym.management.model.account.Role;
import group4.gym.management.service.account.AuthService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Session and authorization filter (CN-02, CN-03, section 3). Registered in web.xml and runs after EncodingFilter.
 * Public paths: /login and static resources (/assets/*). Every other URL requires a valid session.
 * Authorization by URL prefix, fixed in code (no dynamic permission table):
 * <ul>
 *   <li>/admin/*   - ADMIN</li>
 *   <li>/manager/* - MANAGER</li>
 *   <li>/staff/*   - MANAGER, RECEPTIONIST</li>
 *   <li>other protected URLs (/home, /logout, /change-password) - any logged-in role</li>
 * </ul>
 * Finer rules inside one URL (for example only MANAGER may list soft-deleted members, QT-11) are checked in the service layer.
 */
public class AuthFilter implements Filter {

    private final AuthService authService = new AuthService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO: 1. path = HttpServletRequest.getServletPath(); isPublic(path) -> chain.doFilter and return.
        //       2. SessionUser user = SessionUtil.getSessionUser(request); null (not logged in / 30-minute timeout) -> redirect to /login.
        //       3. Re-read the account from the DB on EVERY request: authService.loadAccount(user.getId()).
        //          null, status LOCKED, or role different from user.getRole() -> SessionUtil.invalidate, redirect to /login (KT-03).
        //          Any DB error -> deny (invalidate + redirect to /login); never let the request through.
        //       4. !isAllowed(path, user.getRole()) -> sendError(403) for GET and POST alike, no data changed (KT-05).
        //       5. setNoCacheHeaders(response) (CN-02: Back after logout must not show protected data); chain.doFilter.
        // Output: only authenticated and authorized requests continue down the filter chain.

        // Placeholder: let the request continue while this filter is not implemented.
        chain.doFilter(request, response);
    }

    /**
     * Paths reachable without a session.
     */
    static boolean isPublic(String path) {
        // TODO: "/login" or path starting with "/assets/".
        // Output: true if public.
        return false;
    }

    /**
     * Whether the role may access the path according to its prefix (section 3).
     */
    static boolean isAllowed(String path, Role role) {
        // TODO: "/admin/" -> ADMIN; "/manager/" -> MANAGER; "/staff/" -> MANAGER or RECEPTIONIST; other paths -> true.
        // Output: true if allowed.
        return false;
    }

    /**
     * Anti-cache headers for protected pages (CN-02).
     */
    private static void setNoCacheHeaders(HttpServletResponse response) {
        // TODO: Cache-Control: no-store, no-cache, must-revalidate; Pragma: no-cache; Expires: 0.
        // Output: nothing returned.
    }
}
