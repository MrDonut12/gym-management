package group4.gym.management.service;

import group4.gym.management.dao.MemberDAO;
import group4.gym.management.dto.MemberStatusView;
import group4.gym.management.model.Account;
import group4.gym.management.model.Member;
import java.util.List;

/**
 * Member business logic (CN-06, CN-07, CN-08, CN-12).
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * Search members by code/name/phone (CN-07).
     */
    public List<Member> search(String keyword, Boolean deletedFilter) {
        // TODO: Normalize the keyword; call memberDAO.search.
        // Output: list of Member.
        return null;
    }

    /**
     * Get a member profile by ID.
     */
    public Member getById(int memberId) {
        // TODO: memberDAO.findById; not found -> BusinessRuleException.
        // Output: Member.
        return null;
    }

    /**
     * Create a member profile, including before any plan is purchased (CN-06).
     * A duplicate phone (including soft-deleted profiles) is rejected with a hint to check the existing profile.
     */
    public int createMember(Account actor, Member member) {
        // TODO: ValidationUtil.validateMember; memberDAO.existsByPhone; set createdAt = DateTimeUtil.now(); insert.
        // Output: ID of the new member.
        return 0;
    }

    /**
     * Update a member profile that is not soft-deleted (CN-08).
     */
    public void updateMember(Account actor, Member member) {
        // TODO: Check it exists and is not soft-deleted; validateMember; check duplicate phone (excludeId); update.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Soft-delete a member (Owner/Manager, CN-08). PENDING records are not cancelled automatically; links to memberships/transactions/visits are kept.
     */
    public void softDeleteMember(Account actor, int memberId) {
        // TODO: Check it exists and is not soft-deleted; memberDAO.softDelete(memberId, actor.getId()).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Data for the member detail screen: profile, status of each registration, OPEN visit (CN-12).
     * Runs the catch-up close step for visits before reading (CN-15 (d)).
     */
    public MemberStatusView getMemberStatus(int memberId) {
        // TODO: VisitService.autoCloseOverdueVisits(); getById; MembershipService.getMembershipStatuses;
        //       VisitDAO.findOpenByMemberId; determine hasActiveMembership.
        // Output: MemberStatusView.
        return null;
    }
}
