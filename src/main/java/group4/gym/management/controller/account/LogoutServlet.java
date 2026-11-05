package group4.gym.management.controller.account;

import group4.gym.management.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout (CN-02). POST only (GET answers 405). Does not delete the gym_username cookie (QT-17).
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUtil.invalidate(request);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
