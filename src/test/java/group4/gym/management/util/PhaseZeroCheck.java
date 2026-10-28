package group4.gym.management.util;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.Role;
import jakarta.servlet.http.*;
import java.lang.reflect.*;
import java.util.*;

/** Dependency-free checks. Run with java -ea and the Servlet API on the classpath. */
public class PhaseZeroCheck {
    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    public static void main(String[] args) {
        Map<String, Object> attributes = new HashMap<>();
        boolean[] state = new boolean[3]; // session exists, ID rotated, invalidated
        int[] timeout = {0};
        HttpSession session = proxy(HttpSession.class, (p, m, a) -> {
            switch (m.getName()) {
                case "setAttribute": attributes.put((String) a[0], a[1]); return null;
                case "getAttribute": return attributes.get(a[0]);
                case "setMaxInactiveInterval": timeout[0] = (int) a[0]; return null;
                case "invalidate": state[2] = true; state[0] = false; attributes.clear(); return null;
                default: throw new AssertionError(m.getName());
            }
        });
        String[] path = {"/gym-management"};
        Cookie[][] incoming = {null};
        HttpServletRequest request = proxy(HttpServletRequest.class, (p, m, a) -> {
            switch (m.getName()) {
                case "getSession":
                    if (a == null || (boolean) a[0]) state[0] = true;
                    return state[0] ? session : null;
                case "changeSessionId": assert state[0]; state[1] = true; return "new-id";
                case "getContextPath": return path[0];
                case "getCookies": return incoming[0];
                default: throw new AssertionError(m.getName());
            }
        });
        List<Cookie> outgoing = new ArrayList<>();
        HttpServletResponse response = proxy(HttpServletResponse.class, (p, m, a) -> {
            if (!m.getName().equals("addCookie")) throw new AssertionError(m.getName());
            outgoing.add((Cookie) a[0]); return null;
        });
        assert SessionUtil.getSessionUser(request) == null;
        SessionUtil.invalidate(request);
        assert !state[0] && !state[2];
        Account account = new Account();
        account.setId(7); account.setUsername("admin01");
        account.setFullName("Test Admin"); account.setRole(Role.ADMIN);
        SessionUtil.login(request, account);
        var user = SessionUtil.getSessionUser(request);
        assert state[1] && timeout[0] == 1800;
        assert user.getId() == 7 && user.getRole() == Role.ADMIN;
        assert user.getUsername().equals("admin01") && user.getFullName().equals("Test Admin");
        SessionUtil.flashMessage(request, "Saved"); SessionUtil.flashError(request, "Failed");
        assert attributes.get(AppConstants.FLASH_MESSAGE).equals("Saved");
        assert attributes.get(AppConstants.FLASH_ERROR).equals("Failed");
        assert CookieUtil.getRememberedUsername(request) == null;
        incoming[0] = new Cookie[]{new Cookie("gym_username", "<script>alert(1)</script>")};
        assert CookieUtil.getRememberedUsername(request) == null;
        incoming[0] = new Cookie[]{new Cookie("other", "ignored"), new Cookie("gym_username", "admin01")};
        assert CookieUtil.getRememberedUsername(request).equals("admin01");
        for (String context : new String[]{"/gym-management", ""}) {
            path[0] = context;
            CookieUtil.addRememberUsernameCookie(request, response, "admin01");
            CookieUtil.clearRememberUsernameCookie(request, response);
            Cookie added = outgoing.get(outgoing.size() - 2), cleared = outgoing.get(outgoing.size() - 1);
            assert added.getName().equals("gym_username") && added.getValue().equals("admin01");
            assert added.getMaxAge() == 2592000 && added.isHttpOnly();
            assert added.getPath().equals(context.isEmpty() ? "/" : context);
            assert cleared.getPath().equals(added.getPath()) && cleared.getMaxAge() == 0;
            assert cleared.isHttpOnly() && cleared.getValue().isEmpty();
        }
        int count = outgoing.size();
        try {
            CookieUtil.addRememberUsernameCookie(request, response, "bad value");
            throw new AssertionError("Invalid username accepted");
        } catch (IllegalArgumentException expected) {
            assert outgoing.size() == count;
        }
        SessionUtil.invalidate(request);
        assert state[2] && SessionUtil.getSessionUser(request) == null;
        assert outgoing.size() == count; // Logout must not delete the remembered username.
        System.out.println("PASS: session creation/rotation/timeout/logout, flash storage, cookie validation/path/lifetime/deletion");
    }
}
