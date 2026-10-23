package group4.gym.management.security;

import group4.gym.management.model.Account;
import group4.gym.management.model.enums.Role;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import static group4.gym.management.security.Permission.*;

public final class RbacPolicy {
    private static final Map<Role, Set<Permission>> PERMISSIONS = new EnumMap<>(Role.class);
    static {
        PERMISSIONS.put(Role.ADMINISTRATOR, EnumSet.of(CHANGE_OWN_PASSWORD, RESET_OTHER_PASSWORD, VIEW_ACCOUNTS, MANAGE_ACCOUNT, LOCK_UNLOCK_ACCOUNT, ASSIGN_ROLE, SOFT_DELETE_ACCOUNT));
        PERMISSIONS.put(Role.OWNER, EnumSet.of(CHANGE_OWN_PASSWORD, VIEW_MEMBERS, SOFT_DELETE_MEMBER, VIEW_PLAN_TYPES, CREATE_PLAN_TYPE, UPDATE_PLAN_TYPE, VIEW_TRANSACTIONS, VIEW_VISIT_HISTORY, VIEW_REVENUE_REPORT, VIEW_MEMBER_REPORT));
        PERMISSIONS.put(Role.MANAGER, EnumSet.of(CHANGE_OWN_PASSWORD, VIEW_ACCOUNTS, MANAGE_ACCOUNT, LOCK_UNLOCK_ACCOUNT, VIEW_MEMBERS, CREATE_UPDATE_MEMBER, SOFT_DELETE_MEMBER, VIEW_PLAN_TYPES, UPDATE_PLAN_TYPE, CREATE_MEMBERSHIP, CONFIRM_PAYMENT, CANCEL_PENDING_TRANSACTION, VIEW_TRANSACTIONS, CHECK_IN_OUT, VIEW_VISIT_HISTORY, VIEW_REVENUE_REPORT, VIEW_MEMBER_REPORT));
        PERMISSIONS.put(Role.RECEPTIONIST, EnumSet.of(CHANGE_OWN_PASSWORD, VIEW_MEMBERS, CREATE_UPDATE_MEMBER, CREATE_MEMBERSHIP, CONFIRM_PAYMENT, CANCEL_PENDING_TRANSACTION, VIEW_TRANSACTIONS, CHECK_IN_OUT, VIEW_VISIT_HISTORY));
        PERMISSIONS.put(Role.ACCOUNTANT, EnumSet.of(CHANGE_OWN_PASSWORD, VIEW_TRANSACTIONS, VIEW_REVENUE_REPORT));
    }
    private RbacPolicy() { }

    public static boolean hasPermission(Role role, Permission permission) {
        return role != null && permission != null && PERMISSIONS.get(role).contains(permission);
    }

    public static Permission resolvePermission(String servletPath, String httpMethod) {
        if (servletPath == null) return null;
        switch (servletPath) {
            case "/account/change-password":
                return CHANGE_OWN_PASSWORD;
            case "/accounts/reset-password":
                return RESET_OTHER_PASSWORD;
            case "/accounts":
                return VIEW_ACCOUNTS;
            case "/accounts/create":
            case "/accounts/edit":
                return MANAGE_ACCOUNT;
            case "/accounts/lock":
                return LOCK_UNLOCK_ACCOUNT;
            case "/accounts/delete":
                return SOFT_DELETE_ACCOUNT;
            case "/members":
            case "/members/detail":
                return VIEW_MEMBERS;
            case "/members/create":
            case "/members/edit":
                return CREATE_UPDATE_MEMBER;
            case "/members/delete":
                return SOFT_DELETE_MEMBER;
            case "/plans":
                return VIEW_PLAN_TYPES;
            case "/plans/create":
                return CREATE_PLAN_TYPE;
            case "/plans/edit":
                return UPDATE_PLAN_TYPE;
            case "/memberships/create":
                return CREATE_MEMBERSHIP;
            case "/transactions/confirm":
                return CONFIRM_PAYMENT;
            case "/transactions/cancel":
                return CANCEL_PENDING_TRANSACTION;
            case "/transactions":
            case "/transactions/detail":
                return VIEW_TRANSACTIONS;
            case "/visits/check-in":
            case "/visits/check-out":
                return CHECK_IN_OUT;
            case "/visits":
                return VIEW_VISIT_HISTORY;
            case "/reports/revenue":
                return VIEW_REVENUE_REPORT;
            case "/reports/members":
            case "/reports/purchases":
                return VIEW_MEMBER_REPORT;
            default: return null;
        }
    }

    public static boolean canManageRole(Account actor, Role targetRole) {
        return actor != null && targetRole != null && (actor.getRole() == Role.ADMINISTRATOR
                || (actor.getRole() == Role.MANAGER
                    && (targetRole == Role.RECEPTIONIST || targetRole == Role.ACCOUNTANT)));
    }

    public static boolean canManageAccount(Account actor, Account target) {
        return target != null && canManageRole(actor, target.getRole())
                && actor.getId() != null && target.getId() != null && !actor.getId().equals(target.getId());
    }

    public static boolean canAssignRole(Account actor, Account target, Role newRole) {
        return actor != null && actor.getRole() == Role.ADMINISTRATOR && newRole != null
                && canManageAccount(actor, target);
    }

    public static boolean canResetPassword(Account actor, Account target) {
        return actor != null && actor.getRole() == Role.ADMINISTRATOR && canManageAccount(actor, target);
    }
}
