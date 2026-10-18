package group4.gym.management.dto;

import group4.gym.management.model.enums.TransactionStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Transaction filter criteria (CN-24).
 * PENDING/CANCELLED filter by created_at; PAID filters by paid_at; the date range includes the whole first and last day.
 */
public class TransactionFilter {

    /** Start date (inclusive) */
    private LocalDate fromDate;

    /** End date (inclusive of the whole day) */
    private LocalDate toDate;

    /** Selected statuses; empty = all */
    private List<TransactionStatus> statuses;

    /** Filter by member (optional) */
    private Integer memberId;

    public TransactionFilter() {
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

    public List<TransactionStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<TransactionStatus> statuses) {
        this.statuses = statuses;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

}
