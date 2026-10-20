package group4.gym.management.dto;

import group4.gym.management.model.enums.VisitStatus;
import java.time.LocalDate;

/**
 * Visit history filter criteria (CN-16): member, date, status.
 */
public class VisitFilter {

    /** Filter by member (optional) */
    private Integer memberId;

    /** From check-in date (inclusive) */
    private LocalDate fromDate;

    /** To check-in date (inclusive) */
    private LocalDate toDate;

    /** Visit status (optional) */
    private VisitStatus status;

    public VisitFilter() {
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

}
