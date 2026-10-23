package group4.gym.management.security;

import group4.gym.management.model.Account;
import group4.gym.management.model.enums.Role;
import group4.gym.management.util.PasswordUtil;
import java.util.EnumSet;
import static group4.gym.management.security.Permission.*;

/** Run with java -ea; no test dependency required. */
public class AuthSelfTest {
    public static void main(String[] args) {
        check(Role.ADMINISTRATOR, CHANGE_OWN_PASSWORD, RESET_OTHER_PASSWORD, VIEW_ACCOUNTS,
                MANAGE_ACCOUNT, LOCK_UNLOCK_ACCOUNT, ASSIGN_ROLE, SOFT_DELETE_ACCOUNT);
        check(Role.OWNER, CHANGE_OWN_PASSWORD, VIEW_MEMBERS, SOFT_DELETE_MEMBER, VIEW_PLAN_TYPES,
                CREATE_PLAN_TYPE, UPDATE_PLAN_TYPE, VIEW_TRANSACTIONS, VIEW_VISIT_HISTORY,
                VIEW_REVENUE_REPORT, VIEW_MEMBER_REPORT);
        check(Role.MANAGER, CHANGE_OWN_PASSWORD, VIEW_ACCOUNTS, MANAGE_ACCOUNT, LOCK_UNLOCK_ACCOUNT,
                VIEW_MEMBERS, CREATE_UPDATE_MEMBER, SOFT_DELETE_MEMBER, VIEW_PLAN_TYPES, UPDATE_PLAN_TYPE,
                CREATE_MEMBERSHIP, CONFIRM_PAYMENT, CANCEL_PENDING_TRANSACTION, VIEW_TRANSACTIONS,
                CHECK_IN_OUT, VIEW_VISIT_HISTORY, VIEW_REVENUE_REPORT, VIEW_MEMBER_REPORT);
        check(Role.RECEPTIONIST, CHANGE_OWN_PASSWORD, VIEW_MEMBERS, CREATE_UPDATE_MEMBER,
                CREATE_MEMBERSHIP, CONFIRM_PAYMENT, CANCEL_PENDING_TRANSACTION, VIEW_TRANSACTIONS,
                CHECK_IN_OUT, VIEW_VISIT_HISTORY);
        check(Role.ACCOUNTANT, CHANGE_OWN_PASSWORD, VIEW_TRANSACTIONS, VIEW_REVENUE_REPORT);
        assert !RbacPolicy.hasPermission(null, VIEW_ACCOUNTS);
        assert !RbacPolicy.hasPermission(Role.OWNER, null);
        Account admin = account(1, Role.ADMINISTRATOR);
        Account manager = account(2, Role.MANAGER);
        for (Role role : Role.values()) {
            Account target = account(3, role);
            assert RbacPolicy.canManageAccount(admin, target);
            assert RbacPolicy.canManageAccount(manager, target)
                    == (role == Role.RECEPTIONIST || role == Role.ACCOUNTANT);
            assert RbacPolicy.canAssignRole(admin, target, role);
            assert !RbacPolicy.canAssignRole(manager, target, role);
            assert RbacPolicy.canResetPassword(admin, target);
            assert !RbacPolicy.canResetPassword(manager, target);
        }
        assert !RbacPolicy.canManageAccount(admin, admin);
        assert !RbacPolicy.canAssignRole(admin, admin, Role.OWNER);
        assert !RbacPolicy.canResetPassword(admin, admin);
        assert RbacPolicy.resolvePermission("/home", "GET") == null;
        assert RbacPolicy.resolvePermission("/plans/edit", "GET") == UPDATE_PLAN_TYPE;
        assert RbacPolicy.resolvePermission("/plans/edit", "POST") == UPDATE_PLAN_TYPE;
        assert RbacPolicy.resolvePermission("/reports/purchases", "GET") == VIEW_MEMBER_REPORT;
        assert PasswordUtil.verify("seed-format-test", "pbkdf2_sha256$210000$000102030405060708090a0b0c0d0e0f$8f839186ad39bbd53dc13a521b38f7b0b7c201b168a165085323f055307daa91");
        String hash = PasswordUtil.hash("test password");
        assert PasswordUtil.verify("test password", hash);
        assert !PasswordUtil.verify("wrong password", hash);
        assert !hash.equals(PasswordUtil.hash("test password"));
        for (String invalid : new String[] {"", "plaintext", "1:x:x", "-1:a:b", "pbkdf2_sha256$1$xx$zz"}) {
            assert !PasswordUtil.verify("test password", invalid);
        }
        assert !PasswordUtil.verify(null, hash);
        assert !PasswordUtil.verify("test password", null);
        System.out.println("PASS: 5-role permission matrix, account scope, password hashing and invalid hashes");
    }

    private static Account account(int id, Role role) {
        Account account = new Account();
        account.setId(id);
        account.setRole(role);
        return account;
    }

    private static void check(Role role, Permission... expected) {
        EnumSet<Permission> allowed = EnumSet.noneOf(Permission.class);
        for (Permission permission : expected) allowed.add(permission);
        for (Permission permission : Permission.values()) {
            assert RbacPolicy.hasPermission(role, permission) == allowed.contains(permission)
                    : role + " / " + permission;
        }
    }
}
