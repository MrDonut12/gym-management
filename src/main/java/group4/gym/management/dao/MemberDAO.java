package group4.gym.management.dao;

import group4.gym.management.model.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for members (DL-01). No physical deletion.
 */
public class MemberDAO {

    /**
     * Find a member by ID (including soft-deleted).
     */
    public Member findById(int id) {
        // TODO: SELECT by id.
        // Output: Member; null if it does not exist.
        return null;
    }

    /**
     * Find a member by phone (exact match, including soft-deleted).
     */
    public Member findByPhone(String phone) {
        // TODO: SELECT ... WHERE phone = ?.
        // Output: Member; null if it does not exist.
        return null;
    }

    /**
     * Search members (CN-07): code and phone match exactly; name matches partially and case-insensitively.
     *
     * @param keyword       the keyword; null/empty = return all
     * @param deletedFilter null = all; true = only soft-deleted; false = only not soft-deleted
     */
    public List<Member> search(String keyword, Boolean deletedFilter) {
        // TODO: WHERE (id = ? when keyword is numeric) OR phone = ? OR LOWER(full_name) LIKE LOWER(?) [AND is_deleted = ?].
        // Output: list of Member.
        return null;
    }

    /**
     * Whether the phone already exists (including soft-deleted profiles).
     */
    public boolean existsByPhone(String phone, Integer excludeId) {
        // TODO: SELECT COUNT(*) ... WHERE phone = ? [AND id <> ?].
        // Output: true if duplicated.
        return false;
    }

    /**
     * Insert a member profile.
     */
    public int insert(Member member) {
        // TODO: INSERT (full_name, phone, email, date_of_birth, gender, created_at, is_deleted = 0).
        // Output: the generated ID.
        return 0;
    }

    /**
     * Update a member profile (ID, created_at and the soft-delete flag are unchanged).
     */
    public boolean update(Member member) {
        // TODO: UPDATE full_name, phone, email, date_of_birth, gender WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Soft-delete a member; does not touch memberships/transactions/visits.
     */
    public boolean softDelete(int id, int deletedBy) {
        // TODO: UPDATE is_deleted = 1, deleted_at = ?, deleted_by = ? WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a Member.
        // Output: Member.
        return null;
    }
}
