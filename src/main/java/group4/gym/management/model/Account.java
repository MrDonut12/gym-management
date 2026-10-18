package group4.gym.management.model;

import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import java.time.LocalDateTime;

/**
 * Staff account and basic staff information (DL-07, CN-04, CN-35).
 */
public class Account {

    /** Auto-generated ID */
    private Integer id;

    /** Unique, case-insensitive (including locked/soft-deleted accounts) */
    private String username;

    /** Securely hashed password; plaintext is never stored */
    private String passwordHash;

    /** Staff full name (required) */
    private String fullName;

    /** Exactly 10 digits, unique within the staff set */
    private String phone;

    /** Optional, not required to be unique */
    private String email;

    /** One of the five fixed roles */
    private Role role;

    /** ACTIVE/LOCKED */
    private AccountStatus status;

    /** Soft-delete flag (independent from LOCKED) */
    private boolean deleted;

    /** Creation time */
    private LocalDateTime createdAt;

    /** Last update time */
    private LocalDateTime updatedAt;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
