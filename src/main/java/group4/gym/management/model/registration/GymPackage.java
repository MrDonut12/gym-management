package group4.gym.management.model.registration;

import java.time.LocalDateTime;

/**
 * Package type (DL-03, CN-18 to CN-21). Named GymPackage to avoid clashing with java.lang.Package.
 * Only name and price are editable; the duration is fixed after creation. Never deleted: deactivated instead (QT-16).
 */
public class GymPackage {

    /** Auto-generated ID */
    private Integer id;

    /** HL-11: unique after collapsing whitespace and ignoring case */
    private String name;

    /** HL-13: 1, 3, 6 or 12 months; read-only after creation */
    private int durationMonths;

    /** HL-12: integer 1..100,000,000 VND */
    private int price;

    /** true = on sale (default); false = not shown when creating registrations (QT-04) */
    private boolean active;

    /** Creation time (AppClock) */
    private LocalDateTime createdAt;

    public GymPackage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
