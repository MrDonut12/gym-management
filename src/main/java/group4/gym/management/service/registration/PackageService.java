package group4.gym.management.service.registration;

import group4.gym.management.dao.registration.PackageDAO;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.registration.GymPackage;
import java.util.List;

/**
 * Package type management (FR-18 to FR-21, BR-03, BR-04, BR-16).
 * Create/edit/deactivate are reached through /manager/* only; listing is shared with RECEPTIONIST (on-sale packages only).
 */
public class PackageService {

    private final PackageDAO packageDAO = new PackageDAO();

    /**
     * Package list (FR-18). MANAGER sees everything and may filter on sale / not on sale;
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
     * Packages that can be chosen when creating a registration (FR-22, BR-04).
     */
    public List<GymPackage> listOnSale() {
        // TODO: packageDAO.findAll(true).
        // Output: list of GymPackage on sale.
        return null;
    }

    /**
     * Get a package by ID (VR-17).
     */
    public GymPackage getById(int packageId) {
        // TODO: packageDAO.findById; null -> NotFoundException.
        // Output: GymPackage.
        return null;
    }

    /**
     * Create a package (FR-19), on sale by default.
     *
     * @param rawPrice the price exactly as typed (VR-12 accepts digits only)
     */
    public int createPackage(String name, Integer durationMonths, String rawPrice) {
        // TODO: VR-11 name (trimmed) + existsByNormalizedName(normalizePackageName(name), null); VR-13 duration; VR-12 parsePrice;
        //       any error -> ValidationException; active = true; createdAt = AppClock.now(); packageDAO.insert.
        // Output: ID of the new package.
        return 0;
    }

    /**
     * Edit name and price only (FR-20). The duration is read-only: any duration parameter is ignored (AC-15).
     * Existing registrations keep their snapshot (BR-03).
     */
    public void updateNameAndPrice(int packageId, String name, String rawPrice) {
        // TODO: getById; VR-11 (existsByNormalizedName excluding this id); VR-12; packageDAO.updateNameAndPrice.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Stop selling or resume selling (FR-21, BR-04). Packages are never deleted.
     * PENDING registrations of a package that stopped selling can still be confirmed.
     */
    public void setActive(int packageId, boolean active) {
        // TODO: getById; packageDAO.updateActive.
        // Output: nothing returned; throws an exception on failure.
    }
}
