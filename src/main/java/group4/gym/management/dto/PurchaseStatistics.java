package group4.gym.management.dto;

/**
 * Successful purchase statistics (CN-29).
 */
public class PurchaseStatistics {

    /** Number of first-ever PAID purchases per member with paid_at inside the period */
    private int newRegistrations;

    /** Number of subsequent PAID purchases (renewal/repurchase) with paid_at inside the period */
    private int renewals;

    public PurchaseStatistics() {
    }

    public int getNewRegistrations() {
        return newRegistrations;
    }

    public void setNewRegistrations(int newRegistrations) {
        this.newRegistrations = newRegistrations;
    }

    public int getRenewals() {
        return renewals;
    }

    public void setRenewals(int renewals) {
        this.renewals = renewals;
    }

}
