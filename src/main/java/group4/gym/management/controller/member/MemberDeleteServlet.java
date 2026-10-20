package group4.gym.management.controller.member;

import group4.gym.management.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Soft-delete a member (CN-08). Owner, Manager. POST only.
 */
@WebServlet(name = "MemberDeleteServlet", urlPatterns = {"/members/delete"})
public class MemberDeleteServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read id; memberService.softDeleteMember(currentAccount, id). PENDING records are not cancelled automatically.
        // Output: redirect to /members/detail?id=<id> with a message; 403 if the actor lacks permission.
    }
}
