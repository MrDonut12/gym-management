package group4.gym.management.dto;

/**
 * Plan revenue grouped by plan name snapshot (CN-27).
 */
public class PlanRevenueSummary {

    /** Plan name snapshot */
    private String planNameSnapshot;

    /** Number of PAID transactions */
    private int transactionCount;

    /** Total amount */
    private long totalAmount;

    public PlanRevenueSummary() {
    }

    public String getPlanNameSnapshot() {
        return planNameSnapshot;
    }

    public void setPlanNameSnapshot(String planNameSnapshot) {
        this.planNameSnapshot = planNameSnapshot;
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
