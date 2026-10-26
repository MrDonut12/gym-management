package group4.gym.management.service.member;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.MembershipStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Membership status (QT-07) and expiring-soon rule (QT-08), derived from PAID registrations; nothing is stored.
 * Shared interface SRS 9.1 (owner TV2, used by TV4 for CN-28).
 */
public class MembershipStatusService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    /**
     * Membership status of one member today (QT-07).
     */
    public MembershipStatus statusOf(int memberId) {
        // TODO: PAID rows of registrationDAO.findByMember(memberId); paidCoveringToday and the largest end_date;
        //       resolve(..., AppClock.today()).
        // Output: MembershipStatus.
        return null;
    }

    /**
     * Members expiring soon today (QT-08).
     */
    public List<ExpiringMember> expiringSoon() {
        // TODO: memberDAO.findExpiringSoon(AppClock.today(), AppConstants.EXPIRING_MAX_DAYS_AHEAD).
        // Output: list of ExpiringMember sorted by end date ascending.
        return null;
    }

    /**
     * QT-07 priority: ACTIVE (some PAID covers today) -> UPCOMING (largest end_date > today, none covers today)
     * -> EXPIRED (every end_date < today) -> NONE (no PAID).
     *
     * @param currentEndDate largest PAID end_date; null if the member has no PAID
     */
    public MembershipStatus resolve(boolean paidCoveringToday, LocalDate currentEndDate, LocalDate today) {
        // TODO: currentEndDate == null -> NONE; paidCoveringToday -> ACTIVE; currentEndDate >= today -> UPCOMING; otherwise EXPIRED.
        // Output: MembershipStatus.
        return null;
    }

    /**
     * QT-08 for one member who is not soft-deleted: status ACTIVE and the largest PAID end_date is 0..6 days after today
     * (1..7 days of use left including today). PENDING never counts as a renewal.
     */
    public boolean isExpiringSoon(MembershipStatus status, LocalDate currentEndDate, LocalDate today) {
        // TODO: status == ACTIVE && 0 <= DAYS.between(today, currentEndDate) <= AppConstants.EXPIRING_MAX_DAYS_AHEAD.
        // Output: true if the warning must be shown (KT-14).
        return false;
    }
}
