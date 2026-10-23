package group4.gym.management.util;

import group4.gym.management.model.Account;
import group4.gym.management.model.enums.Role;
import group4.gym.management.filter.AuthorizationFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/** Small servlet API doubles; run with java -ea. */
public class SessionCookieSelfTest {
    public static void main(String[] args) throws Exception {
        Map<String, Object> sessionData = new HashMap<>();
        Map<String, Object> requestData = new HashMap<>();
        boolean[] invalidated = {false};
        int[] timeout = {0};
        String[] header = {null};
        String[] path = {"/accounts"};
        int[] status = {0};
        boolean[] secure = {false};
        Cookie[][] cookies = {null};
        HttpSession session = (HttpSession) Proxy.newProxyInstance(HttpSession.class.getClassLoader(),
                new Class<?>[]{HttpSession.class}, (proxy, method, values) -> {
                    switch (method.getName()) {
                        case "invalidate": invalidated[0] = true; sessionData.clear(); return null;
                        case "getAttribute": return sessionData.get(values[0]);
                        case "setAttribute": sessionData.put((String) values[0], values[1]); return null;
                        case "setMaxInactiveInterval": timeout[0] = (Integer) values[0]; return null;
                        default: throw new UnsupportedOperationException(method.getName());
                    }
                });
        HttpServletRequest request = (HttpServletRequest) Proxy.newProxyInstance(HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class}, (proxy, method, values) -> {
                    switch (method.getName()) {
                        case "getSession": return session;
                        case "getAttribute": return requestData.get(values[0]);
                        case "getContextPath": return "";
                        case "getServletPath": return path[0];
                        case "getMethod": return "GET";
                        case "isSecure": return secure[0];
                        case "getCookies": return cookies[0];
                        default: throw new UnsupportedOperationException(method.getName());
                    }
                });
        HttpServletResponse response = (HttpServletResponse) Proxy.newProxyInstance(HttpServletResponse.class.getClassLoader(),
                new Class<?>[]{HttpServletResponse.class}, (proxy, method, values) -> {
                    switch (method.getName()) {
                        case "addHeader": header[0] = (String) values[1]; return null;
                        case "sendError": status[0] = (Integer) values[0]; return null;
                        default: throw new UnsupportedOperationException(method.getName());
                    }
                });
        Account account = new Account();
        account.setId(7);
        account.setRole(Role.ACCOUNTANT);
        sessionData.put("old", "discard");
        SessionUtil.login(request, account);
        assert invalidated[0] && timeout[0] == 3600;
        assert sessionData.size() == 1 && SessionUtil.getAccountId(request) == 7;
        CookieUtil.addRememberUsernameCookie(request, response, "test user");
        assert header[0].contains("gym_username=test+user;");
        assert header[0].contains("Max-Age=2592000; Path=/; HttpOnly; SameSite=Lax");
        assert !header[0].contains("Secure");
        secure[0] = true;
        CookieUtil.clearRememberUsernameCookie(request, response);
        assert header[0].contains("Max-Age=0;") && header[0].endsWith("; Secure");
        cookies[0] = new Cookie[]{new Cookie("gym_username", "test+user")};
        assert "test user".equals(CookieUtil.getRememberedUsername(request));
        cookies[0] = new Cookie[]{new Cookie("gym_username", "%GG")};
        assert CookieUtil.getRememberedUsername(request) == null;
        requestData.put(AppConstants.REQUEST_CURRENT_ACCOUNT, account);
        assert SessionUtil.getCurrentAccount(request) == account;
        boolean[] passed = {false};
        AuthorizationFilter filter = new AuthorizationFilter();
        filter.doFilter(request, response, (req, res) -> passed[0] = true);
        assert status[0] == 403 && !passed[0];
        path[0] = "/reports/revenue";
        filter.doFilter(request, response, (req, res) -> passed[0] = true);
        assert passed[0];
        passed[0] = false;
        path[0] = "/unknown";
        filter.doFilter(request, response, (req, res) -> passed[0] = true);
        assert status[0] == 404 && !passed[0];
        SessionUtil.invalidate(request);
        assert SessionUtil.getAccountId(request) == null;
        System.out.println("PASS: session replacement/timeout, cookies, authorization allow/deny and unknown paths");
    }
}
