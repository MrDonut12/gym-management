package group4.gym.management.dao.registration;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.model.registration.GymPackage;
import group4.gym.management.util.ValidationUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        String sql = "SELECT id, name, duration_months, price, is_active, created_at FROM packages"
                + (active == null ? "" : " WHERE is_active = ?")
                + " ORDER BY duration_months, name";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (active != null) {
                ps.setBoolean(1, active);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<GymPackage> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot list packages", ex);
        }
    }

    /**
     * Find a package by ID.
     */
    public GymPackage findById(int id) {
        String sql = "SELECT id, name, duration_months, price, is_active, created_at FROM packages WHERE id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read package " + id, ex);
        }
    }

    /**
     * Whether a package name already exists after normalization (HL-11).
     *
     * @param normalizedName ValidationUtil.normalizePackageName(name)
     * @param excludeId      ignore this package when updating; null when creating
     */
    public boolean existsByNormalizedName(String normalizedName, Integer excludeId) {
        String sql = "SELECT id, name FROM packages";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                if (excludeId != null && rs.getInt("id") == excludeId) {
                    continue;
                }
                if (ValidationUtil.normalizePackageName(rs.getString("name")).equals(normalizedName)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            throw new AppException("Cannot check package name", ex);
        }
    }

    /**
     * Insert a package (CN-19), on sale by default.
     */
    public int insert(GymPackage gymPackage) {
        String sql = "INSERT INTO packages (name, duration_months, price, is_active, created_at) VALUES (?, ?, ?, 1, ?)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, gymPackage.getName());
            ps.setInt(2, gymPackage.getDurationMonths());
            ps.setInt(3, gymPackage.getPrice());
            ps.setObject(4, gymPackage.getCreatedAt());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("INSERT INTO packages returned no generated key");
                }
                return keys.getInt(1);
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot insert package", ex);
        }
    }

    /**
     * Update name and price only (CN-20). Existing registrations keep their snapshot (QT-03).
     */
    public boolean updateNameAndPrice(int id, String name, int price) {
        String sql = "UPDATE packages SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new AppException("Cannot update package " + id, ex);
        }
    }

    /**
     * Stop selling / resume selling (CN-21, QT-04).
     */
    public boolean updateActive(int id, boolean active) {
        String sql = "UPDATE packages SET is_active = ? WHERE id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new AppException("Cannot update status of package " + id, ex);
        }
    }

    private GymPackage mapRow(ResultSet rs) throws SQLException {
        GymPackage p = new GymPackage();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDurationMonths(rs.getInt("duration_months"));
        p.setPrice(rs.getInt("price"));
        p.setActive(rs.getBoolean("is_active"));
        p.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return p;
    }
}
