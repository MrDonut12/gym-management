package group4.gym.management.controller.member;

import group4.gym.management.model.member.Gender;
import group4.gym.management.model.member.Member;
import group4.gym.management.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Shared form handling for MemberCreateServlet and MemberEditServlet (CN-12, CN-14).
 * Reads the posted profile fields and forwards the shared form view.
 */
final class MemberForm {

    static final String VIEW = "/WEB-INF/member/form.jsp";

    private MemberForm() {
    }

    /**
     * Reads fullName, phone, email, dateOfBirth and gender into a Member.
     * An unparsable date (HL-05) or an unknown gender (HL-06) is reported in fieldErrors;
     * the remaining fields are validated by MemberService.
     */
    static Member read(HttpServletRequest request, Map<String, String> fieldErrors) {
        Member member = new Member();
        member.setFullName(RequestUtil.getString(request, "fullName"));
        member.setPhone(RequestUtil.getString(request, "phone"));
        member.setEmail(RequestUtil.getString(request, "email"));

        try {
            member.setDateOfBirth(RequestUtil.getDate(request, "dateOfBirth"));
        } catch (DateTimeParseException ex) {
            fieldErrors.put("dateOfBirth", "Use a valid date.");
        }

        String genderValue = RequestUtil.getString(request, "gender");
        if (genderValue != null) {
            Gender gender = RequestUtil.getEnum(request, "gender", Gender.class);
            if (gender == null) {
                fieldErrors.put("gender", "Choose a listed gender.");
            }
            member.setGender(gender);
        }
        return member;
    }

    /**
     * Forwards to the form with the mode, gender options and the member being edited.
     */
    static void forward(HttpServletRequest request, HttpServletResponse response,
            String mode, Member member) throws ServletException, IOException {
        request.setAttribute("mode", mode);
        request.setAttribute("member", member);
        request.setAttribute("genders", Gender.values());
        request.getRequestDispatcher(VIEW).forward(request, response);
    }

    /**
     * Forwards back to the form with the submitted values and the validation errors.
     */
    static void forwardWithErrors(HttpServletRequest request, HttpServletResponse response,
            String mode, Member member, String error, Map<String, String> fieldErrors)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("fieldErrors", fieldErrors);
        forward(request, response, mode, member);
    }
}
