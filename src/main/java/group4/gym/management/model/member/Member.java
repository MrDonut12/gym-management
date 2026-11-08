package group4.gym.management.model.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Member profile. Not a login account. The soft-delete flag and the
 * membership status are
 * independent; the profile can exist before any package is bought.
 */
public class Member {

    /** Auto-generated database ID. */
    private Integer id;

    /** Full name */
    private String fullName;

    /** Unique including soft-deleted profiles */
    private String phone;

    /** Optional */
    private String email;

    /** Optional */
    private LocalDate dateOfBirth;

    /** Optional */
    private Gender gender;

    /** Soft-delete flag */
    private boolean deleted;

    /** Creation time (AppClock); not editable */
    private LocalDateTime createdAt;

    /** Account that created the profile */
    private Integer createdBy;

    public Member() {
    }

    /**
     * Display code: "HV" + the ID on 6 digits, for example id 123 -> HV000123.
     */
    public String getCode() {
        return id == null ? null : String.format("HV%06d", id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

}
