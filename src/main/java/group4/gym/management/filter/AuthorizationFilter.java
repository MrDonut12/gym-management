package group4.gym.management.filter;

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
        // TODO: Skip public paths; get the current Account (SessionUtil.getCurrentAccount);
        //       permission = RbacPolicy.resolvePermission(servletPath, method);
        //       permission != null && !RbacPolicy.hasPermission(role, permission) -> response.sendError(403).
        // Output: a request beyond the user's permissions is blocked with 403 and changes no data.

        // Placeholder: let the request continue while this filter is not implemented.
        chain.doFilter(request, response);
    }
}
