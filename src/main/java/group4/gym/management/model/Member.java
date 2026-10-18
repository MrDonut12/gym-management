package group4.gym.management.model;

import group4.gym.management.model.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Member profile (DL-01). Not a login account; profile state is independent from membership state.
 */
public class Member {

    /** Auto-generated member ID/code, not editable */
    private Integer id;

    /** Full name, 2-100 characters */
    private String fullName;

    /** 10 digits starting with 0, unique including soft-deleted profiles */
    private String phone;

    /** Optional, at most 254 characters */
    private String email;

    /** Optional, not after the current date */
    private LocalDate dateOfBirth;

    /** Optional: Male/Female/Other */
    private Gender gender;

    /** Profile creation time (used for new-member statistics) */
    private LocalDateTime createdAt;

    /** Soft-delete flag */
    private boolean deleted;

    /** Soft-delete time */
    private LocalDateTime deletedAt;

    /** ID of the staff member who soft-deleted the profile */
    private Integer deletedBy;

    public Member() {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Integer deletedBy) {
        this.deletedBy = deletedBy;
    }

}
