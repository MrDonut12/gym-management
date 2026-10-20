package group4.gym.management.dao;

import group4.gym.management.model.PlanType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for plan types (DL-02). No delete/retire operation and no duration editing.
 */
public class PlanTypeDAO {

    /**
     * Get all plan types.
     */
    public List<PlanType> findAll() {
        // TODO: SELECT * ORDER BY duration/name.
        // Output: list of PlanType.
        return null;
    }

    /**
     * Find a plan type by ID.
     */
    public PlanType findById(int id) {
        // TODO: SELECT by id.
        // Output: PlanType; null if it does not exist.
        return null;
    }

    /**
     * Whether the plan name already exists after normalization (collapsed whitespace, case-insensitive).
     */
    public boolean existsByNormalizedName(String normalizedName, Integer excludeId) {
        // TODO: Compare with the normalized name in the DB (a normalized_name column or a normalizing function).
        // Output: true if duplicated.
        return false;
    }

    /**
     * Insert a plan type.
     */
    public int insert(PlanType planType) {
        // TODO: INSERT (name, duration_value, duration_unit, price, created_at).
        // Output: the generated ID.
        return 0;
    }

    /**
     * Edit only name and price (CN-09). Snapshots of existing registrations/transactions are not affected.
     */
    public boolean updateNameAndPrice(int id, String name, long price) {
        // TODO: UPDATE name, price, updated_at WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    private PlanType mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a PlanType.
        // Output: PlanType.
        return null;
    }
}
