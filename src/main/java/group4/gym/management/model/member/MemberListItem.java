package group4.gym.management.model.member;

import java.time.LocalDate;

/**
 * One row of the member list (CN-11): code, full name, phone, membership status (QT-07), current expiry date.
 * MemberDAO fills the raw PAID aggregates; MembershipStatusService derives the status.
 */
public class MemberListItem {

    /** Member profile */
    private Member member;

    /** Some PAID registration covers today (start_date <= today <= end_date) */
    private boolean paidCoveringToday;

    /** Largest end_date of the member's PAID registrations; null if there is none ("Ngày hết hạn gói", QT-07) */
    private LocalDate currentEndDate;

    /** Derived membership status */
    private MembershipStatus status;

    public MemberListItem() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isPaidCoveringToday() {
        return paidCoveringToday;
    }

    public void setPaidCoveringToday(boolean paidCoveringToday) {
        this.paidCoveringToday = paidCoveringToday;
    }

    public LocalDate getCurrentEndDate() {
        return currentEndDate;
    }

    public void setCurrentEndDate(LocalDate currentEndDate) {
        this.currentEndDate = currentEndDate;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

}
