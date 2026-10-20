package group4.gym.management.model;

import group4.gym.management.model.enums.DurationUnit;
import java.time.LocalDateTime;

/**
 * Plan registration (DL-03). One purchase = one record; the schedule is set only when PAID.
 */
public class Membership {

    /** Auto-generated ID */
    private Integer id;

    /** Member */
    private Integer memberId;

    /** Source plan type */
    private Integer planTypeId;

    /** Plan name snapshot taken when the PENDING record is created */
    private String planNameSnapshot;

    /** Duration value snapshot taken when the PENDING record is created */
    private int durationValueSnapshot;

    /** Duration unit snapshot taken when the PENDING record is created */
    private DurationUnit durationUnitSnapshot;

    /** Registration creation time */
    private LocalDateTime createdAt;

    /** Staff member who created the registration */
    private Integer createdBy;

    /** Start boundary (00:00 Vietnam time); null until PAID */
    private LocalDateTime startAt;

    /** Exclusive end boundary; null until PAID */
    private LocalDateTime endExclusive;

    public Membership() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(Integer planTypeId) {
        this.planTypeId = planTypeId;
    }

    public String getPlanNameSnapshot() {
        return planNameSnapshot;
    }

    public void setPlanNameSnapshot(String planNameSnapshot) {
        this.planNameSnapshot = planNameSnapshot;
    }

    public int getDurationValueSnapshot() {
        return durationValueSnapshot;
    }

    public void setDurationValueSnapshot(int durationValueSnapshot) {
        this.durationValueSnapshot = durationValueSnapshot;
    }

    public DurationUnit getDurationUnitSnapshot() {
        return durationUnitSnapshot;
    }

    public void setDurationUnitSnapshot(DurationUnit durationUnitSnapshot) {
        this.durationUnitSnapshot = durationUnitSnapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndExclusive() {
        return endExclusive;
    }

    public void setEndExclusive(LocalDateTime endExclusive) {
        this.endExclusive = endExclusive;
    }

}
