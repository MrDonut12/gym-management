package group4.gym.management.dao.registration;

import group4.gym.management.model.registration.GymPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for package types (table packages, DL-03). No delete and no duration update (QT-16, CN-20).
 * Every query uses PreparedStatement parameters (CL-02). Methods open their own connection with
 * DBContext.getConnection() and wrap SQLException in AppException.
 */
public class PackageDAO {

    /**
     * List packages (CN-18).
     *
     * @param active null = all; true = on sale; false = not on sale
     */
    public List<GymPackage> findAll(Boolean active) {
        // TODO: SELECT ... FROM packages [WHERE is_active = ?] ORDER BY duration_months, name.
        // Output: list of GymPackage.
        return null;
    }

    /**
     * Find a package by ID.
     */
    public GymPackage findById(int id) {
        // TODO: SELECT ... FROM packages WHERE id = ?.
        // Output: GymPackage; null if it does not exist.
        return null;
    }

    /**
     * Whether a package name already exists after normalization (HL-11).
     *
     * @param normalizedName ValidationUtil.normalizePackageName(name)
     * @param excludeId      ignore this package when updating; null when creating
     */
    public boolean existsByNormalizedName(String normalizedName, Integer excludeId) {
        // TODO: Load the (id, name) pairs and compare ValidationUtil.normalizePackageName(name) in Java
        //       (collapsing whitespace is awkward in T-SQL); the table is small.
        // Output: true if duplicated.
        return false;
    }

    /**
     * Insert a package (CN-19), on sale by default.
     */
    public int insert(GymPackage gymPackage) {
        // TODO: INSERT (name, duration_months, price, is_active = 1, created_at).
        // Output: the generated ID.
        return 0;
    }

    /**
     * Update name and price only (CN-20). Existing registrations keep their snapshot (QT-03).
     */
    public boolean updateNameAndPrice(int id, String name, int price) {
        // TODO: UPDATE packages SET name = ?, price = ? WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Stop selling / resume selling (CN-21, QT-04).
     */
    public boolean updateActive(int id, boolean active) {
        // TODO: UPDATE packages SET is_active = ? WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    private GymPackage mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a GymPackage.
        // Output: GymPackage.
        return null;
    }
}
