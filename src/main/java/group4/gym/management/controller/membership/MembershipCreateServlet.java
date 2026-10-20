package group4.gym.management.controller.membership;

import group4.gym.management.service.MemberService;
import group4.gym.management.service.MembershipService;
import group4.gym.management.service.PlanTypeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Plan registration / renewal-repurchase: creates a Membership + PENDING transaction (CN-10, CN-11). Manager, Receptionist.
 */
@WebServlet(name = "MembershipCreateServlet", urlPatterns = {"/memberships/create"})
public class MembershipCreateServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/membership/create.jsp";

    private final MemberService memberService = new MemberService();
    private final PlanTypeService planTypeService = new PlanTypeService();
    private final MembershipService membershipService = new MembershipService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read memberId; memberService.getById (soft-deleted member -> show an error, do not allow registration);
        //       planTypeService.listAll(); set the member and the plan list on the request.
        // Output: forward to VIEW.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Read memberId, planTypeId, paymentMethod (ignore any price/start date sent by the client).
        //       membershipService.createPendingMembership(currentAccount, ...).
        // Output: success: redirect to /transactions/confirm?id=<transaction id>; failure: forward to VIEW with errors.
    }
}
