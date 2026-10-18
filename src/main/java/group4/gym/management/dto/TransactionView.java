package group4.gym.management.dto;

import group4.gym.management.model.PaymentTransaction;
import java.time.LocalDateTime;

/**
 * Transaction row with related display information (CN-23).
 */
public class TransactionView {

    /** Transaction */
    private PaymentTransaction transaction;

    /** Member full name */
    private String memberName;

    /** Member is soft-deleted */
    private boolean memberDeleted;

    /** Name of the staff member who created it */
    private String createdByName;

    /** Name of the staff member who confirmed PAID */
    private String confirmedByName;

    /** Registration start_at (once PAID) */
    private LocalDateTime membershipStartAt;

    /** Registration end_exclusive (once PAID) */
    private LocalDateTime membershipEndExclusive;

    public TransactionView() {
    }

    public PaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isMemberDeleted() {
        return memberDeleted;
    }

    public void setMemberDeleted(boolean memberDeleted) {
        this.memberDeleted = memberDeleted;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getConfirmedByName() {
        return confirmedByName;
    }

    public void setConfirmedByName(String confirmedByName) {
        this.confirmedByName = confirmedByName;
    }

    public LocalDateTime getMembershipStartAt() {
        return membershipStartAt;
    }

    public void setMembershipStartAt(LocalDateTime membershipStartAt) {
        this.membershipStartAt = membershipStartAt;
    }

    public LocalDateTime getMembershipEndExclusive() {
        return membershipEndExclusive;
    }

    public void setMembershipEndExclusive(LocalDateTime membershipEndExclusive) {
        this.membershipEndExclusive = membershipEndExclusive;
    }

}
