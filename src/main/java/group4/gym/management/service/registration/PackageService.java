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
 * Package type management (CN-18 to CN-21, QT-03, QT-04, QT-16).
 * Create/edit/deactivate are reached through /manager/* only; listing is shared with RECEPTIONIST (on-sale packages only).
 */
public class PackageService {

    private static final String FIELD_NAME = "name";
    private static final String FIELD_DURATION = "durationMonths";
    private static final String FIELD_PRICE = "price";
    private static final String MSG_PRICE = "Giá gói phải là số nguyên từ 1 đến 100.000.000 VND";

    private final PackageDAO packageDAO = new PackageDAO();

    /**
     * Package list (CN-18). MANAGER sees everything and may filter on sale / not on sale;
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
     * Packages that can be chosen when creating a registration (CN-22, QT-04).
     */
    public List<GymPackage> listOnSale() {
        return packageDAO.findAll(true);
    }

    /**
     * Get a package by ID (HL-17).
     */
    public GymPackage getById(int packageId) {
        GymPackage gymPackage = packageDAO.findById(packageId);
        if (gymPackage == null) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
        return gymPackage;
    }

    /**
     * Create a package (CN-19), on sale by default.
     *
     * @param rawPrice the price exactly as typed (HL-12 accepts digits only)
     */
    public int createPackage(String name, Integer durationMonths, String rawPrice) {
        String trimmedName = name == null ? null : name.trim();
        Map<String, String> errors = new LinkedHashMap<>();
        validateName(trimmedName, null, errors);
        if (!ValidationUtil.isValidDurationMonths(durationMonths)) {
            errors.put(FIELD_DURATION, "Thời hạn gói phải là 1, 3, 6 hoặc 12 tháng");
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
     * Edit name and price only (CN-20). The duration is read-only: any duration parameter is ignored (KT-15).
     * Existing registrations keep their snapshot (QT-03).
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
     * Stop selling or resume selling (CN-21, QT-04). Packages are never deleted.
     * PENDING registrations of a package that stopped selling can still be confirmed.
     */
    public void setActive(int packageId, boolean active) {
        getById(packageId);
        if (!packageDAO.updateActive(packageId, active)) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
    }

    /**
     * HL-11: length 2-100 (name already trimmed) and unique after normalization.
     *
     * @param excludeId package being edited; null when creating
     */
    private void validateName(String trimmedName, Integer excludeId, Map<String, String> errors) {
        if (!ValidationUtil.isValidPackageName(trimmedName)) {
            errors.put(FIELD_NAME, "Tên gói phải từ 2 đến 100 ký tự");
        } else if (packageDAO.existsByNormalizedName(ValidationUtil.normalizePackageName(trimmedName), excludeId)) {
            errors.put(FIELD_NAME, "Tên gói đã tồn tại");
        }
    }
}
