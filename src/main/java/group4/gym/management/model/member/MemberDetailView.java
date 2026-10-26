package group4.gym.management.model.member;

import group4.gym.management.model.registration.Registration;
import java.time.LocalDate;
import java.util.List;

/**
 * View data for the member detail screen (CN-13): profile, soft-delete label, membership status today,
 * expiry warning and the registration history of every status (newest first).
 */
public class MemberDetailView {

    /** Member profile */
    private Member member;

    /** Membership status today (QT-07) */
    private MembershipStatus status;

    /** Largest PAID end_date; null if there is no PAID */
    private LocalDate currentEndDate;

    /** Expiry warning (QT-08) */
    private boolean expiringSoon;

    /** Registrations of every status, newest first (RegistrationDAO.findByMember) */
    private List<Registration> registrations;

    public MemberDetailView() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public LocalDate getCurrentEndDate() {
        return currentEndDate;
    }

    public void setCurrentEndDate(LocalDate currentEndDate) {
        this.currentEndDate = currentEndDate;
    }

    public boolean isExpiringSoon() {
        return expiringSoon;
    }

    public void setExpiringSoon(boolean expiringSoon) {
        this.expiringSoon = expiringSoon;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

}
