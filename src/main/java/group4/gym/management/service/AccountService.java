package group4.gym.management.service;

import group4.gym.management.dao.AccountDAO;
import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.Account;
import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import group4.gym.management.security.Permission;
import group4.gym.management.security.RbacPolicy;
import group4.gym.management.util.PasswordUtil;
import group4.gym.management.util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Staff business rules; object scope is always checked through RbacPolicy. */
public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> listManageableAccounts(Account actor, String keyword, Role role, AccountStatus status,
            boolean includeDeleted) {
        actor = requireActor(actor, Permission.VIEW_ACCOUNTS);
        List<Role> roles = new ArrayList<>();
        for (Role candidate : Role.values()) {
            if ((role == null || role == candidate) && RbacPolicy.canManageRole(actor, candidate)) roles.add(candidate);
        }
        return accountDAO.search(keyword, roles, status, includeDeleted);
    }

    public Account getManageableAccount(Account actor, int targetId) {
        actor = requireActor(actor, Permission.VIEW_ACCOUNTS);
        Account target = manageableTarget(actor, targetId);
        target.setPasswordHash(null);
        return target;
    }

    public int createAccount(Account actor, Account newAccount, String rawPassword, String confirmPassword) {
        actor = requireActor(actor, Permission.MANAGE_ACCOUNT);
        if (newAccount == null) throw new BusinessRuleException("Thiếu thông tin tài khoản.");
        normalize(newAccount);
        validate(ValidationUtil.validateAccount(newAccount, rawPassword, true));
        validate(ValidationUtil.validateNewPassword(rawPassword, confirmPassword));
        if (!RbacPolicy.canManageRole(actor, newAccount.getRole())) deny();
        if (accountDAO.existsByUsername(newAccount.getUsername(), null)) {
            throw new ValidationException("Tên đăng nhập đã tồn tại.", Map.of("username", "Tên đăng nhập đã tồn tại."));
        }
        checkPhone(newAccount.getPhone(), null);
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setDeleted(false);
        newAccount.setPasswordHash(PasswordUtil.hash(rawPassword));
        try {
            return accountDAO.insert(newAccount);
        } finally {
            newAccount.setPasswordHash(null);
        }
    }

    public void updateAccount(Account actor, Account changes) {
        actor = requireActor(actor, Permission.MANAGE_ACCOUNT);
        if (changes == null || changes.getId() == null) throw new BusinessRuleException("Thiếu mã tài khoản.");
        Account target = manageableTarget(actor, changes.getId());
        requireNotDeleted(target);
        // Profile updates cannot change username, role, status or password.
        target.setFullName(changes.getFullName());
        target.setPhone(changes.getPhone());
        target.setEmail(changes.getEmail());
        normalize(target);
        validate(ValidationUtil.validateAccount(target, null, false));
        checkPhone(target.getPhone(), target.getId());
        requireUpdated(accountDAO.updateProfile(target));
    }

    public void changeRole(Account actor, int targetId, Role newRole) {
        actor = requireActor(actor, Permission.ASSIGN_ROLE);
        Account target = manageableTarget(actor, targetId);
        requireNotDeleted(target);
        if (!RbacPolicy.canAssignRole(actor, target, newRole)) deny();
        requireUpdated(accountDAO.updateRole(targetId, newRole));
    }

    public void lockAccount(Account actor, int targetId) {
        changeStatus(actor, targetId, AccountStatus.LOCKED);
    }

    public void unlockAccount(Account actor, int targetId) {
        changeStatus(actor, targetId, AccountStatus.ACTIVE);
    }

    public void softDeleteAccount(Account actor, int targetId) {
        actor = requireActor(actor, Permission.SOFT_DELETE_ACCOUNT);
        Account target = manageableTarget(actor, targetId);
        requireNotDeleted(target);
        requireUpdated(accountDAO.softDelete(targetId));
    }

    public void changeOwnPassword(Account actor, String currentPassword, String newPassword, String confirmPassword) {
        actor = requireActor(actor, Permission.CHANGE_OWN_PASSWORD);
        if (!PasswordUtil.verify(currentPassword, actor.getPasswordHash())) {
            throw new ValidationException("Mật khẩu hiện tại không đúng.", Map.of("currentPassword", "Mật khẩu hiện tại không đúng."));
        }
        validate(ValidationUtil.validateNewPassword(newPassword, confirmPassword));
        requireUpdated(accountDAO.updatePasswordHash(actor.getId(), PasswordUtil.hash(newPassword)));
    }

    public void resetPassword(Account actor, int targetId, String newPassword, String confirmPassword) {
        actor = requireActor(actor, Permission.RESET_OTHER_PASSWORD);
        Account target = manageableTarget(actor, targetId);
        requireNotDeleted(target);
        if (!RbacPolicy.canResetPassword(actor, target)) deny();
        validate(ValidationUtil.validateNewPassword(newPassword, confirmPassword));
        requireUpdated(accountDAO.updatePasswordHash(targetId, PasswordUtil.hash(newPassword)));
    }

    private Account requireActor(Account actor, Permission permission) {
        if (actor == null || actor.getId() == null) deny();
        Account current = accountDAO.findById(actor.getId());
        if (current == null || current.isDeleted() || current.getStatus() != AccountStatus.ACTIVE
                || !RbacPolicy.hasPermission(current.getRole(), permission)) deny();
        return current;
    }

    private Account manageableTarget(Account actor, int targetId) {
        Account target = accountDAO.findById(targetId);
        if (target == null) throw new BusinessRuleException("Không tìm thấy tài khoản.");
        if (!RbacPolicy.canManageAccount(actor, target)) deny();
        return target;
    }

    private void changeStatus(Account actor, int targetId, AccountStatus status) {
        actor = requireActor(actor, Permission.LOCK_UNLOCK_ACCOUNT);
        Account target = manageableTarget(actor, targetId);
        requireNotDeleted(target);
        requireUpdated(accountDAO.updateStatus(targetId, status));
    }

    private void checkPhone(String phone, Integer excludeId) {
        if (accountDAO.existsByPhone(phone, excludeId)) {
            throw new ValidationException("Số điện thoại đã tồn tại.", Map.of("phone", "Số điện thoại đã tồn tại."));
        }
    }

    private void normalize(Account account) {
        if (account.getUsername() != null) account.setUsername(account.getUsername().trim());
        if (account.getFullName() != null) account.setFullName(account.getFullName().trim());
        if (account.getPhone() != null) account.setPhone(account.getPhone().trim());
        if (account.getEmail() != null) {
            String email = account.getEmail().trim();
            account.setEmail(email.isEmpty() ? null : email);
        }
    }

    private void validate(Map<String, String> errors) {
        // shortcut: T04 still returns null; keep writes blocked until its validators are implemented.
        if (errors == null) throw new AppException("Kiểm tra dữ liệu T04 chưa được triển khai.");
        if (!errors.isEmpty()) throw new ValidationException("Vui lòng kiểm tra thông tin đã nhập.", errors);
    }

    private void requireNotDeleted(Account account) {
        if (account.isDeleted()) throw new BusinessRuleException("Tài khoản đã bị xóa.");
    }

    private void requireUpdated(boolean updated) {
        if (!updated) throw new BusinessRuleException("Tài khoản không còn tồn tại hoặc đã bị xóa.");
    }

    private void deny() {
        throw new AccessDeniedException("Bạn không có quyền thực hiện thao tác này.");
    }
}
