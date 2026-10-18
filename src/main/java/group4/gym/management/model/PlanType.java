package group4.gym.management.model;

import group4.gym.management.model.enums.DurationUnit;
import java.time.LocalDateTime;

/**
 * Plan type (DL-02, CN-09). Only name and price are editable; duration is fixed and cannot be retired.
 */
public class PlanType {

    /** Auto-generated ID */
    private Integer id;

    /** Plan name, unique after whitespace normalization and case folding */
    private String name;

    /** Duration value (positive integer) */
    private int durationValue;

    /** Duration unit */
    private DurationUnit durationUnit;

    /** Price in VND, integer greater than 0 */
    private long price;

    /** Creation time */
    private LocalDateTime createdAt;

    /** Last update time */
    private LocalDateTime updatedAt;

    public PlanType() {
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

    public int getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public DurationUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
