package group4.gym.management.model.payment;

/**
 * Member overview, the MANAGER home page (CN-28).
 */
public class MemberOverview {

    /** Today: profiles not soft-deleted */
    private int activeProfiles;

    /** Today: soft-deleted profiles */
    private int deletedProfiles;

    /** Today: members with a membership status ACTIVE (QT-07) */
    private int membersWithActivePackage;

    /** Today: members expiring soon (QT-08) */
    private int expiringSoon;

    /** In the date range: profiles by created_at, including those soft-deleted later */
    private int newMembers;

    public MemberOverview() {
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

    public int getMembersWithActivePackage() {
        return membersWithActivePackage;
    }

    public void setMembersWithActivePackage(int membersWithActivePackage) {
        this.membersWithActivePackage = membersWithActivePackage;
    }

    public int getExpiringSoon() {
        return expiringSoon;
    }

    public void setExpiringSoon(int expiringSoon) {
        this.expiringSoon = expiringSoon;
    }

    public int getNewMembers() {
        return newMembers;
    }

    public void setNewMembers(int newMembers) {
        this.newMembers = newMembers;
    }

}
