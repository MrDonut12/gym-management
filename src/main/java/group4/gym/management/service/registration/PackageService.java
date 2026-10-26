package group4.gym.management.service.registration;

import group4.gym.management.dao.registration.PackageDAO;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.registration.GymPackage;
import java.util.List;

/**
 * Package type management (CN-18 to CN-21, QT-03, QT-04, QT-16).
 * Create/edit/deactivate are reached through /manager/* only; listing is shared with RECEPTIONIST (on-sale packages only).
 */
public class PackageService {

    private final PackageDAO packageDAO = new PackageDAO();

    /**
     * Package list (CN-18). MANAGER sees everything and may filter on sale / not on sale;
     * RECEPTIONIST always gets on-sale packages only, whatever filter was sent.
     *
     * @param activeFilter null = all (MANAGER only)
     */
    public List<GymPackage> listPackages(SessionUser actor, Boolean activeFilter) {
        // TODO: actor is RECEPTIONIST -> packageDAO.findAll(true); otherwise packageDAO.findAll(activeFilter).
        // Output: list of GymPackage.
        return null;
    }

    /**
     * Packages that can be chosen when creating a registration (CN-22, QT-04).
     */
    public List<GymPackage> listOnSale() {
        // TODO: packageDAO.findAll(true).
        // Output: list of GymPackage on sale.
        return null;
    }

    /**
     * Get a package by ID (HL-17).
     */
    public GymPackage getById(int packageId) {
        // TODO: packageDAO.findById; null -> NotFoundException.
        // Output: GymPackage.
        return null;
    }

    /**
     * Create a package (CN-19), on sale by default.
     *
     * @param rawPrice the price exactly as typed (HL-12 accepts digits only)
     */
    public int createPackage(String name, Integer durationMonths, String rawPrice) {
        // TODO: HL-11 name (trimmed) + existsByNormalizedName(normalizePackageName(name), null); HL-13 duration; HL-12 parsePrice;
        //       any error -> ValidationException; active = true; createdAt = AppClock.now(); packageDAO.insert.
        // Output: ID of the new package.
        return 0;
    }

    /**
     * Edit name and price only (CN-20). The duration is read-only: any duration parameter is ignored (KT-15).
     * Existing registrations keep their snapshot (QT-03).
     */
    public void updateNameAndPrice(int packageId, String name, String rawPrice) {
        // TODO: getById; HL-11 (existsByNormalizedName excluding this id); HL-12; packageDAO.updateNameAndPrice.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Stop selling or resume selling (CN-21, QT-04). Packages are never deleted.
     * PENDING registrations of a package that stopped selling can still be confirmed.
     */
    public void setActive(int packageId, boolean active) {
        // TODO: getById; packageDAO.updateActive.
        // Output: nothing returned; throws an exception on failure.
    }
}
