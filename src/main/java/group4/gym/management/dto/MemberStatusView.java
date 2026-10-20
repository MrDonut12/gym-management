package group4.gym.management.dto;

import group4.gym.management.model.Member;
import group4.gym.management.model.Visit;
import java.util.List;

/**
 * View data for the member detail screen: profile + registrations + OPEN visit (CN-12).
 */
public class MemberStatusView {

    /** Member profile */
    private Member member;

    /** Plan registrations with their status */
    private List<MembershipStatusView> memberships;

    /** Current OPEN visit (after the catch-up close step); may be null */
    private Visit openVisit;

    /** Whether an active membership exists at viewing time */
    private boolean hasActiveMembership;

    public MemberStatusView() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<MembershipStatusView> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<MembershipStatusView> memberships) {
        this.memberships = memberships;
    }

    public Visit getOpenVisit() {
        return openVisit;
    }

    public void setOpenVisit(Visit openVisit) {
        this.openVisit = openVisit;
    }

    public boolean isHasActiveMembership() {
        return hasActiveMembership;
    }

    public void setHasActiveMembership(boolean hasActiveMembership) {
        this.hasActiveMembership = hasActiveMembership;
    }

}
