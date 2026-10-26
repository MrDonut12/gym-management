package group4.gym.management.model.payment;

import java.time.LocalDate;

/**
 * One revenue row (QT-12): per period (CN-26) or per package (CN-27), and the total row at the end of the table.
 * Only PAID registrations with paid_at inside the range are counted, using price_snapshot.
 */
public class RevenueRow {

    /** CN-26: first day of the period (the day, or the 1st of the month); null for package rows and the total row */
    private LocalDate periodStart;

    /** CN-27: package ID the row is grouped by (not the name snapshot); null otherwise */
    private Integer packageId;

    /** Display label: period, CURRENT package name (CN-27), or "Total" */
    private String label;

    /** Number of PAID registrations */
    private int transactionCount;

    /** Sum of price_snapshot */
    private long revenue;

    /** CN-27: share of the total revenue in percent; 0 for CN-26 rows */
    private double sharePercent;

    public RevenueRow() {
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(double sharePercent) {
        this.sharePercent = sharePercent;
    }

}
