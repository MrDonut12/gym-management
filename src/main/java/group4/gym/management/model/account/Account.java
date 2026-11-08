package group4.gym.management.model.account;

import java.time.LocalDateTime;

/**
 * Staff account (DR-01, FR-05 to FR-10). Never physically deleted (BR-16).
 */
public class Account {

    /** Auto-generated ID */
    private Integer id;

    /** VR-08; unique case-insensitive; not editable after creation */
    private String username;

    /** Hashed password (NFR-03); never passed to a view */
    private String passwordHash;

    /** VR-01 */
    private String fullName;

    /** VR-03: unique within accounts; may equal a member's phone */
    private String phone;

    /** Optional, not required to be unique (VR-04) */
    private String email;

    /** One of the three fixed roles */
    private Role role;

    /** ACTIVE/LOCKED */
    private AccountStatus status;

    /** Creation time (AppClock) */
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
