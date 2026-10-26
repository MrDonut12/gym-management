package group4.gym.management.dao.member;

import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.member.MemberListItem;
import group4.gym.management.model.member.MemberScope;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access for members (table members, DL-02). No physical deletion (QT-16).
 * Every query uses PreparedStatement parameters (CL-02). Methods without a Connection open their own with
 * DBContext.getConnection() and wrap SQLException in AppException; methods with a Connection run inside the
 * caller's transaction (SRS 9.1) and throw SQLException.
 *
 * Search keyword (CN-11): "HV000123" or "123" -> id exact; "0912345678" -> phone exact; otherwise
 * full_name LIKE %keyword% (case-insensitive). A partial phone therefore finds nothing (KT-09).
 */
public class MemberDAO {

    /**
     * Find a member by ID, including soft-deleted (shared interface SRS 9.1, used by TV3).
     */
    public Member findById(int id) {
        // TODO: SELECT ... FROM members WHERE id = ?.
        // Output: Member; null if it does not exist.
        return null;
    }

    /**
     * Lock the member row at the start of a QT-06 transaction (shared interface SRS 9.1, used by CN-15, CN-22, CN-25).
     */
    public Member lockForUpdate(Connection conn, int memberId) throws SQLException {
        // TODO: SELECT ... FROM members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?.
        // Output: the locked Member; null if it does not exist.
        return null;
    }

    /**
     * Find a member by phone, including soft-deleted (CN-12: a duplicate phone shows the code of the profile using it).
     */
    public Member findByPhone(String phone) {
        // TODO: SELECT ... FROM members WHERE phone = ?.
        // Output: Member; null if it does not exist.
        return null;
    }

    /**
     * One page of the member list (CN-11), newest first, with the PAID aggregates needed by QT-07.
     *
     * @param keyword null = no search
     * @param today   AppClock.today(), used for paidCoveringToday
     */
    public List<MemberListItem> search(String keyword, MemberScope scope, LocalDate today, int offset, int limit) {
        // TODO: SELECT m.*, MAX(r.end_date) AS current_end_date,
        //              MAX(CASE WHEN r.start_date <= ? AND r.end_date >= ? THEN 1 ELSE 0 END) AS covers_today
        //       FROM members m LEFT JOIN registrations r ON r.member_id = m.id AND r.status = 'PAID'
        //       WHERE <scope> AND <keyword> GROUP BY m.* ORDER BY m.created_at DESC, m.id DESC
        //       OFFSET ? ROWS FETCH NEXT ? ROWS ONLY (CL-09). Leave status empty: the service derives it.
        // Output: list of MemberListItem.
        return null;
    }

    /**
     * Total rows of search() for the same keyword and scope (pagination).
     */
    public int count(String keyword, MemberScope scope) {
        // TODO: SELECT COUNT(*) FROM members WHERE <scope> AND <keyword>.
        // Output: number of matching members.
        return 0;
    }

    /**
     * Search members that are not soft-deleted (shared interface SRS 9.1: TV3 picks a member for CN-22).
     */
    public List<Member> searchActive(String keyword, int limit) {
        // TODO: Same keyword rules as search(), AND is_deleted = 0, ORDER BY full_name, TOP (?).
        // Output: list of Member.
        return null;
    }

    /**
     * Members expiring soon (QT-08, CN-17): not soft-deleted, some PAID covers today, and the largest PAID end_date
     * is within [today, today + maxDaysAhead]. A member with a follow-on PAID is excluded because MAX(end_date) moves forward.
     */
    public List<ExpiringMember> findExpiringSoon(LocalDate today, int maxDaysAhead) {
        // TODO: Aggregate PAID per member (MAX(end_date), covers today), keep the package_name_snapshot of the row with
        //       the largest end_date, filter by the window, ORDER BY end_date ASC. daysLeft = DATEDIFF(DAY, today, end_date) + 1.
        // Output: list of ExpiringMember.
        return null;
    }

    /**
     * Insert a member profile (CN-12).
     */
    public int insert(Member member) {
        // TODO: INSERT (full_name, phone, email, date_of_birth, gender (label), is_deleted = 0, created_at, created_by).
        // Output: the generated ID.
        return 0;
    }

    /**
     * Update a profile that is not soft-deleted (CN-14). id and created_at are never updated.
     */
    public boolean update(Member member) {
        // TODO: UPDATE members SET full_name, phone, email, date_of_birth, gender WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated; false if missing or soft-deleted.
        return false;
    }

    /**
     * Soft-delete inside the QT-06/QT-09 transaction of CN-15.
     */
    public boolean markDeleted(Connection conn, int id) throws SQLException {
        // TODO: UPDATE members SET is_deleted = 1 WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Restore a soft-deleted profile (CN-16, QT-10); code, created_at and history are kept.
     */
    public boolean restore(int id) {
        // TODO: UPDATE members SET is_deleted = 0 WHERE id = ? AND is_deleted = 1.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a Member (gender via Gender.fromLabel).
        // Output: Member.
        return null;
    }
}
