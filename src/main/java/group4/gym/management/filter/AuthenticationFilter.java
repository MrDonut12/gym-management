package group4.gym.management.filter;

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
        // TODO: Skip public paths: /login, /assets/*.
        //       SessionUtil.getAccountId == null (not logged in / session expired) -> redirect to /login
        //       (non-page requests such as AJAX -> return 401 without any data).
        //       authService.loadActiveAccount(id) == null (locked/soft-deleted mid-session) -> SessionUtil.invalidate, redirect to /login.
        //       Valid -> put the Account into the request attribute AppConstants.REQUEST_CURRENT_ACCOUNT.
        // Output: only valid requests continue down the filter chain.

        // Placeholder: let the request continue while this filter is not implemented.
        chain.doFilter(request, response);
    }
}
