package group4.gym.management.security;

import group4.gym.management.model.Account;
import group4.gym.management.model.enums.Role;

/**
 * Fixed authorization policy following the matrix in section 2.3 (CN-05, CN-34, QT-08).
 * Used by AuthorizationFilter (per URL) and by the service layer (per object scope).
 * There is no dynamic permission configuration.
 */
public final class RbacPolicy {

    private RbacPolicy() {
    }

    /**
     * Whether the role may perform the action (a "C" cell in the matrix).
     */
    public static boolean hasPermission(Role role, Permission permission) {
        // TODO: Declare a fixed Role -> Set<Permission> mapping that mirrors matrix 2.3 and look it up.
        // Output: true if the cell is C; false for K or "-".
        return false;
    }

    /**
     * Determine the permission required for a servlet path (used in AuthorizationFilter).
     *
     * @param servletPath for example "/members/create"
     * @param httpMethod  GET/POST
     */
    public static Permission resolvePermission(String servletPath, String httpMethod) {
        // TODO: Map the URLs of protected servlets -> Permission.
        //       URLs that only require a login (for example /home, /logout) return null.
        // Output: the required Permission; null if only a login is required.
        return null;
    }

    /**
     * Whether the actor may manage (create/edit/lock/unlock) accounts of the target role.
     * Administrator: any role; Manager: only Receptionist/Accountant; other roles: none.
     */
    public static boolean canManageRole(Account actor, Role targetRole) {
        // TODO: Check according to matrix 2.3 (a Manager cannot manage Administrator/Owner/Manager).
        // Output: true if allowed.
        return false;
    }

    /**
     * Whether the actor may act on the target account (includes forbidding self-deletion and privilege self-escalation).
     */
    public static boolean canManageAccount(Account actor, Account target) {
        // TODO: Combine canManageRole(actor, target.getRole()) with the self-action constraints.
        // Output: true if allowed.
        return false;
    }

    /**
     * Whether the actor may assign a new role to the target account.
     */
    public static boolean canAssignRole(Account actor, Account target, Role newRole) {
        // TODO: Administrator only; forbid changing one's own role (privilege self-escalation).
        // Output: true if allowed.
        return false;
    }

    /**
     * Whether the actor may reset the target account's password.
     */
    public static boolean canResetPassword(Account actor, Account target) {
        // TODO: Administrator only, within the management scope; target must differ from actor.
        // Output: true if allowed.
        return false;
    }
}
