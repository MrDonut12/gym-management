package group4.gym.management.model.account;

import java.io.Serializable;

/**
 * Logged-in user stored in the HttpSession (shared interface SRS 9.1, CN-01).
 * Holds no password or hash. AuthFilter compares the role with the database on every request (CN-03).
 */
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Account ID */
    private final int id;

    /** Username */
    private final String username;

    /** Full name shown in the navbar */
    private final String fullName;

    /** Role at login time */
    private final Role role;

    public SessionUser(int id, String username, String fullName, Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }

}
