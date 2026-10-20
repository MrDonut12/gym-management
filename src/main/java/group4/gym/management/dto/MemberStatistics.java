package group4.gym.management.dto;

/**
 * Member statistics (CN-28).
 */
public class MemberStatistics {

    /** Total profiles (including soft-deleted, after the status filter) */
    private int totalMembers;

    /** Number of profiles that are not soft-deleted */
    private int activeProfiles;

    /** Number of soft-deleted profiles */
    private int deletedProfiles;

    /** Number of profiles with created_at inside the period */
    private int newMembers;

    public MemberStatistics() {
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(int activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    public int getDeletedProfiles() {
        return deletedProfiles;
    }

    public void setDeletedProfiles(int deletedProfiles) {
        this.deletedProfiles = deletedProfiles;
    }

    public int getNewMembers() {
        return newMembers;
    }

    public void setNewMembers(int newMembers) {
        this.newMembers = newMembers;
    }

}
