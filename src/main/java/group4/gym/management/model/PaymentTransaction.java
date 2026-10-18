package group4.gym.management.model;

import group4.gym.management.model.enums.PaymentMethod;
import group4.gym.management.model.enums.TransactionStatus;
import java.time.LocalDateTime;

/**
 * Payment transaction (DL-06) linked to one Membership.
 */
public class PaymentTransaction {

    /** Transaction code */
    private Integer id;

    /** Member */
    private Integer memberId;

    /** Related plan registration */
    private Integer membershipId;

    /** Plan name snapshot taken when the PENDING record is created */
    private String planNameSnapshot;

    /** Amount snapshot taken when the transaction is created; never recomputed from current configuration */
    private long amount;

    /** Cash/Bank transfer */
    private PaymentMethod paymentMethod;

    /** PENDING/PAID/CANCELLED */
    private TransactionStatus status;

    /** Creation time (filter key for PENDING/CANCELLED) */
    private LocalDateTime createdAt;

    /** Staff member who created the transaction */
    private Integer createdBy;

    /** PAID confirmation time (filter key for PAID and revenue) */
    private LocalDateTime paidAt;

    /** Staff member who confirmed PAID */
    private Integer confirmedBy;

    /** PAID confirmation order recorded by the system (paid-order, CN-29) */
    private Long paidSequence;

    /** Bank transfer proof image (optional) */
    private String proofImagePath;

    /** PENDING cancellation time */
    private LocalDateTime cancelledAt;

    /** Staff member who cancelled the PENDING transaction */
    private Integer cancelledBy;

    public PaymentTransaction() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public String getPlanNameSnapshot() {
        return planNameSnapshot;
    }

    public void setPlanNameSnapshot(String planNameSnapshot) {
        this.planNameSnapshot = planNameSnapshot;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public Integer getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(Integer confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Long getPaidSequence() {
        return paidSequence;
    }

    public void setPaidSequence(Long paidSequence) {
        this.paidSequence = paidSequence;
    }

    public String getProofImagePath() {
        return proofImagePath;
    }

    public void setProofImagePath(String proofImagePath) {
        this.proofImagePath = proofImagePath;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Integer getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(Integer cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

}
