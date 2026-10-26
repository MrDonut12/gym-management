package group4.gym.management.model.registration;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Registration = one package purchase and its payment transaction (DL-04).
 * Snapshot columns are copied from the package at creation and never change (QT-03).
 * Payment columns are set only when PAID; cancel columns only when CANCELLED (cross-column CHECKs in DL-04).
 */
public class Registration {

    /** Auto-generated ID; displayed as DK + 6 digits */
    private Integer id;

    /** Member */
    private Integer memberId;

    /** Source package (revenue by package groups by this ID, QT-12) */
    private Integer packageId;

    /** Package name at creation time */
    private String packageNameSnapshot;

    /** Duration (months) at creation time; used by the schedule formula (QT-05) */
    private int durationMonthsSnapshot;

    /** Price at creation time; the amount paid and counted as revenue */
    private int priceSnapshot;

    /** PENDING/PAID/CANCELLED */
    private RegistrationStatus status;

    /** Creation time (AppClock) */
    private LocalDateTime createdAt;

    /** Account that created the registration */
    private Integer createdBy;

    /** CASH/TRANSFER; null until PAID */
    private PaymentMethod paymentMethod;

    /** Payment confirmation time; null until PAID */
    private LocalDateTime paidAt;

    /** Account that confirmed the payment; null until PAID */
    private Integer confirmedBy;

    /** First valid day (inclusive); null until PAID */
    private LocalDate startDate;

    /** Last valid day (inclusive); null until PAID */
    private LocalDate endDate;

    /** Cancellation time; null unless CANCELLED */
    private LocalDateTime cancelledAt;

    /** Account that cancelled; null unless CANCELLED */
    private Integer cancelledBy;

    /** HL-15 or the fixed reason of QT-09; null unless CANCELLED */
    private String cancelReason;

    public Registration() {
    }

    /**
     * Display code, for example DK000045 (DL-04).
     */
    public String getCode() {
        return id == null ? null : String.format("DK%06d", id);
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

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageNameSnapshot() {
        return packageNameSnapshot;
    }

    public void setPackageNameSnapshot(String packageNameSnapshot) {
        this.packageNameSnapshot = packageNameSnapshot;
    }

    public int getDurationMonthsSnapshot() {
        return durationMonthsSnapshot;
    }

    public void setDurationMonthsSnapshot(int durationMonthsSnapshot) {
        this.durationMonthsSnapshot = durationMonthsSnapshot;
    }

    public int getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(int priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

}
