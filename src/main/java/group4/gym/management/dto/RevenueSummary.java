package group4.gym.management.dto;

import java.time.LocalDate;

/**
 * One revenue row per period (CN-26): total of PAID transactions by paid_at.
 */
public class RevenueSummary {

    /** First day of the period (the day, or the 1st of the month) */
    private LocalDate periodStart;

    /** Display label of the period */
    private String periodLabel;

    /** Number of PAID transactions in the period */
    private int transactionCount;

    /** Total amount received in the period */
    private long totalAmount;

    public RevenueSummary() {
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

}
