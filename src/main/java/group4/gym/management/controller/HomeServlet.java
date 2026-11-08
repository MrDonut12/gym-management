package group4.gym.management.controller;

import group4.gym.management.model.account.SessionUser;
import group4.gym.management.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Home page by role after login: ADMIN -> account list, MANAGER -> member overview,
 * RECEPTIONIST -> member list. Any logged-in role.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUser user = SessionUtil.getSessionUser(request);
        String destination = user == null ? "/login" : switch (user.getRole()) {
            case ADMIN -> "/admin/accounts";
            case MANAGER -> "/manager/overview";
            case RECEPTIONIST -> "/staff/members";
        };
        response.sendRedirect(request.getContextPath() + destination);
    }
}
