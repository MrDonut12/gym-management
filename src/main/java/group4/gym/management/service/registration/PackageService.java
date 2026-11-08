package group4.gym.management.service.registration;

import group4.gym.management.dao.registration.PackageDAO;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.registration.GymPackage;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.ValidationUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Package type management.
 * Create/edit/deactivate are reached through /manager/* only; listing is shared with RECEPTIONIST (on-sale packages only).
 */
public class PackageService {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_DURATION = "durationMonths";
    private static final String FIELD_PRICE = "price";
    private static final String MSG_PRICE = "Price must be a whole number from 1 to 100,000,000 VND (digits only).";

    private final PackageDAO packageDAO = new PackageDAO();

    /**
     * Package list. MANAGER sees everything and may filter on sale / not on sale;
     * RECEPTIONIST always gets on-sale packages only, whatever filter was sent.
     *
     * @param activeFilter null = all (MANAGER only)
     */
    public List<GymPackage> listPackages(SessionUser actor, Boolean activeFilter) {
        if (actor.getRole() == Role.RECEPTIONIST) {
            return packageDAO.findAll(true);
        }
        return packageDAO.findAll(activeFilter);
    }

    /**
     * Packages that can be chosen when creating a registration.
     */
    public List<GymPackage> listOnSale() {
        return packageDAO.findAll(true);
    }

    /**
     * Get a package by ID.
     */
    public GymPackage getById(int packageId) {
        GymPackage gymPackage = packageDAO.findById(packageId);
        if (gymPackage == null) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
        return gymPackage;
    }

    /**
     * Create a package, on sale by default.
     *
     * @param rawPrice the price exactly as typed (digits only)
     */
    public int createPackage(String name, Integer durationMonths, String rawPrice) {
        String trimmedName = name == null ? null : name.trim();
        Map<String, String> errors = new LinkedHashMap<>();
        validateName(trimmedName, null, errors);
        if (!ValidationUtil.isValidDurationMonths(durationMonths)) {
            errors.put(FIELD_DURATION, "Duration must be 1, 3, 6 or 12 months.");
        }
        Integer price = ValidationUtil.parsePrice(rawPrice);
        if (price == null) {
            errors.put(FIELD_PRICE, MSG_PRICE);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Invalid package data", errors);
        }

        GymPackage gymPackage = new GymPackage();
        gymPackage.setName(trimmedName);
        gymPackage.setDurationMonths(durationMonths);
        gymPackage.setPrice(price);
        gymPackage.setActive(true);
        gymPackage.setCreatedAt(AppClock.now());
        return packageDAO.insert(gymPackage);
    }

    /**
     * Edit name and price only. The duration is read-only: any duration parameter is ignored.
     * Existing registrations keep their snapshot.
     */
    public void updateNameAndPrice(int packageId, String name, String rawPrice) {
        getById(packageId);

        String trimmedName = name == null ? null : name.trim();
        Map<String, String> errors = new LinkedHashMap<>();
        validateName(trimmedName, packageId, errors);
        Integer price = ValidationUtil.parsePrice(rawPrice);
        if (price == null) {
            errors.put(FIELD_PRICE, MSG_PRICE);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Invalid package data", errors);
        }

        if (!packageDAO.updateNameAndPrice(packageId, trimmedName, price)) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
    }

    /**
     * Stop selling or resume selling. Packages are never deleted.
     * PENDING registrations of a package that stopped selling can still be confirmed.
     */
    public void setActive(int packageId, boolean active) {
        getById(packageId);
        if (!packageDAO.updateActive(packageId, active)) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
    }

    /**
     * Length 2-100 (name already trimmed) and unique after normalization.
     *
     * @param excludeId package being edited; null when creating
     */
    private void validateName(String trimmedName, Integer excludeId, Map<String, String> errors) {
        if (!ValidationUtil.isValidPackageName(trimmedName)) {
            errors.put(FIELD_NAME, "Package name must be 2-100 characters.");
        } else if (packageDAO.existsByNormalizedName(ValidationUtil.normalizePackageName(trimmedName), excludeId)) {
            errors.put(FIELD_NAME, "Package name already exists.");
        }
    }
}
