package group4.gym.management.dto;

import group4.gym.management.model.Membership;
import group4.gym.management.model.PaymentTransaction;
import group4.gym.management.model.enums.MembershipState;
import java.time.LocalDate;

/**
 * View data for the status of one plan registration (CN-12).
 */
public class MembershipStatusView {

    /** Plan registration */
    private Membership membership;

    /** Related transaction */
    private PaymentTransaction transaction;

    /** Business status label at viewing time */
    private MembershipState state;

    /** Expiry warning (<= 7 days; not applicable to Daily Pass) */
    private boolean expiringSoon;

    /** Last training day = the day before end_exclusive */
    private LocalDate lastTrainingDate;

    public MembershipStatusView() {
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public MembershipState getState() {
        return state;
    }

    public void setState(MembershipState state) {
        this.state = state;
    }

    public boolean isExpiringSoon() {
        return expiringSoon;
    }

    public void setExpiringSoon(boolean expiringSoon) {
        this.expiringSoon = expiringSoon;
    }

    public LocalDate getLastTrainingDate() {
        return lastTrainingDate;
    }

    public void setLastTrainingDate(LocalDate lastTrainingDate) {
        this.lastTrainingDate = lastTrainingDate;
    }

}
