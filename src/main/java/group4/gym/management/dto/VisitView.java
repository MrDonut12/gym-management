package group4.gym.management.dto;

import group4.gym.management.model.Visit;

/**
 * Visit history row (CN-16).
 */
public class VisitView {

    /** Visit */
    private Visit visit;

    /** Member full name */
    private String memberName;

    /** Plan name snapshot of the membership used for the check-in */
    private String planNameSnapshot;

    /** Staff member who created the visit */
    private String createdByName;

    /** Staff member who ended the visit ("System" if closed automatically) */
    private String closedByName;

    public VisitView() {
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPlanNameSnapshot() {
        return planNameSnapshot;
    }

    public void setPlanNameSnapshot(String planNameSnapshot) {
        this.planNameSnapshot = planNameSnapshot;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getClosedByName() {
        return closedByName;
    }

    public void setClosedByName(String closedByName) {
        this.closedByName = closedByName;
    }

}
