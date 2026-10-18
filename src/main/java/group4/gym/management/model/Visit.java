package group4.gym.management.model;

import group4.gym.management.model.enums.VisitStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Gym visit (DL-04). Never physically deleted; at most one OPEN visit per member.
 */
public class Visit {

    /** Auto-generated ID */
    private Integer id;

    /** Member */
    private Integer memberId;

    /** Membership used for the check-in */
    private Integer membershipId;

    /** Recorded date (check-in date, Vietnam time) */
    private LocalDate checkInDate;

    /** Check-in time */
    private LocalDateTime checkInAt;

    /** Check-out time; always 20:00 on the check-in day for AUTO_CLOSED */
    private LocalDateTime checkOutAt;

    /** OPEN/COMPLETED/AUTO_CLOSED */
    private VisitStatus status;

    /** Staff member who created the visit */
    private Integer createdBy;

    /** Staff member who ended the visit; null when closed by the system */
    private Integer closedBy;

    /** true when the visit was closed automatically by the system */
    private boolean closedBySystem;

    public Visit() {
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

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateTime getCheckInAt() {
        return checkInAt;
    }

    public void setCheckInAt(LocalDateTime checkInAt) {
        this.checkInAt = checkInAt;
    }

    public LocalDateTime getCheckOutAt() {
        return checkOutAt;
    }

    public void setCheckOutAt(LocalDateTime checkOutAt) {
        this.checkOutAt = checkOutAt;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy) {
        this.closedBy = closedBy;
    }

    public boolean isClosedBySystem() {
        return closedBySystem;
    }

    public void setClosedBySystem(boolean closedBySystem) {
        this.closedBySystem = closedBySystem;
    }

}
