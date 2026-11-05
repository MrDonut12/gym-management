package group4.gym.management.service.account;

import group4.gym.management.dao.account.AccountDAO;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.SessionUser;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import group4.gym.management.exception.*;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.PasswordUtil;
import group4.gym.management.util.ValidationUtil;

/**
 * Staff account management (CN-05 to CN-10, QT-14, QT-16). Only ADMIN reaches these features (/admin/*),
 * except changeOwnPassword (every role). Invalid data throws ValidationException (section 6),
 * rule violations throw BusinessRuleException, unknown IDs throw NotFoundException.
 */
public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Account list (CN-05). A null criterion is ignored.
     */
    public List<Account> listAccounts(String keyword, Role role, AccountStatus status) {
        if (!ValidationUtil.isValidKeyword(keyword)) {
            throw new ValidationException("Invalid search.", Map.of("keyword", "Use at most 100 characters."));
        }
        return accountDAO.search(keyword, role, status);
    }

    /**
     * Get an account to edit/lock/reset (HL-17).
     */
    public Account getById(int accountId) {
        Account account = requireAccount(accountId);
        account.setPasswordHash(null);
        return account;
    }

    /**
     * Create an account (CN-06), status ACTIVE.
     */
    public int createAccount(SessionUser actor, Account newAccount, String rawPassword, String confirmPassword) {
        requireAdmin(actor);
        Map<String, String> errors = validateDetails(newAccount, null);
        if (newAccount.getRole() == Role.ADMIN) errors.put("role", "Only one ADMIN is allowed; select MANAGER or RECEPTIONIST.");
        if (!ValidationUtil.isValidUsername(newAccount.getUsername())) {
            errors.put("username", "Use 4-50 letters, digits or underscores, starting with a letter.");
        } else if (accountDAO.existsByUsername(newAccount.getUsername())) {
            errors.put("username", "Username already exists.");
        }
        validatePassword(rawPassword, confirmPassword, "password", errors);
        rejectErrors(errors);
        newAccount.setPasswordHash(PasswordUtil.hash(rawPassword));
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setCreatedAt(AppClock.now());
        return accountDAO.insert(newAccount);
    }

    /**
     * Update full name, phone, email, role (CN-07). The username is not editable.
     * QT-14: the actor cannot change their own role.
     */
    public void updateAccount(SessionUser actor, Account changes) {
        requireAdmin(actor);
        Account existing = requireAccount(changes.getId() == null ? 0 : changes.getId());
        if (existing.getId() == actor.getId() && changes.getRole() != existing.getRole()) {
            throw new BusinessRuleException("You cannot change your own role.");
        }
        Map<String, String> errors = validateDetails(changes, existing.getId());
        if (existing.getId() != actor.getId() && changes.getRole() == Role.ADMIN) {
            errors.put("role", "Only one ADMIN is allowed; select MANAGER or RECEPTIONIST.");
        }
        rejectErrors(errors);
        requireUpdated(accountDAO.update(changes));
    }

    /**
     * Lock or unlock an account (CN-08). QT-14: the actor cannot lock themselves. Accounts are never deleted.
     */
    public void setLocked(SessionUser actor, int targetId, boolean locked) {
        requireAdmin(actor);
        if (locked && targetId == actor.getId()) throw new BusinessRuleException("You cannot lock yourself.");
        requireAccount(targetId);
        requireUpdated(accountDAO.updateStatus(targetId, locked ? AccountStatus.LOCKED : AccountStatus.ACTIVE));
    }

    /**
     * Change one's own password (CN-09, every role). The current session is kept.
     */
    public void changeOwnPassword(SessionUser actor, String currentPassword, String newPassword, String confirmPassword) {
        if (actor == null) throw new AccessDeniedException("Login required.");
        Account account = requireAccount(actor.getId());
        Map<String, String> errors = new LinkedHashMap<>();
        if (!PasswordUtil.verify(currentPassword, account.getPasswordHash())) {
            errors.put("currentPassword", "Current password is incorrect.");
        }
        validatePassword(newPassword, confirmPassword, "newPassword", errors);
        rejectErrors(errors);
        requireUpdated(accountDAO.updatePasswordHash(actor.getId(), PasswordUtil.hash(newPassword)));
    }

    /**
     * ADMIN sets a new password for another account (CN-10) without the old one.
     * QT-14: not allowed on oneself (use CN-09). The LOCKED/ACTIVE status is not changed (KT-08).
     */
    public void resetPassword(SessionUser actor, int targetId, String newPassword, String confirmPassword) {
        requireAdmin(actor);
        if (targetId == actor.getId()) throw new BusinessRuleException("Use Change password for your own account.");
        requireAccount(targetId);
        Map<String, String> errors = new LinkedHashMap<>();
        validatePassword(newPassword, confirmPassword, "newPassword", errors);
        rejectErrors(errors);
        requireUpdated(accountDAO.updatePasswordHash(targetId, PasswordUtil.hash(newPassword)));
    }
    private Account requireAccount(int id) {
        Account account = id > 0 ? accountDAO.findById(id) : null;
        if (account == null) throw new NotFoundException("Account not found.");
        return account;
    }

    private static void requireAdmin(SessionUser actor) {
        if (actor == null || actor.getRole() != Role.ADMIN) throw new AccessDeniedException("ADMIN required.");
    }

    private static void requireUpdated(boolean updated) {
        if (!updated) throw new NotFoundException("Account no longer exists.");
    }

    private Map<String, String> validateDetails(Account account, Integer excludeId) {
        Map<String, String> errors = new LinkedHashMap<>();
        account.setFullName(account.getFullName() == null ? null : account.getFullName().trim());
        account.setPhone(account.getPhone() == null ? null : account.getPhone().trim());
        account.setEmail(account.getEmail() == null || account.getEmail().isBlank() ? null : account.getEmail().trim());
        if (!ValidationUtil.isValidFullName(account.getFullName())) errors.put("fullName", "Use 2-100 characters without digits.");
        if (!ValidationUtil.isValidPhone(account.getPhone())) {
            errors.put("phone", "Use 10 digits starting with 0.");
        } else if (accountDAO.existsByPhone(account.getPhone(), excludeId)) {
            errors.put("phone", "Phone already belongs to another account.");
        }
        if (account.getEmail() != null && !ValidationUtil.isValidEmail(account.getEmail())) errors.put("email", "Invalid email.");
        if (account.getRole() == null) errors.put("role", "Select a valid role.");
        return errors;
    }

    private static void validatePassword(String password, String confirm, String field, Map<String, String> errors) {
        if (!ValidationUtil.isValidPassword(password)) errors.put(field, "Use 8-64 characters.");
        if (password == null || !password.equals(confirm)) errors.put("confirmPassword", "Passwords do not match.");
    }

    private static void rejectErrors(Map<String, String> errors) {
        if (!errors.isEmpty()) throw new ValidationException("Please correct the highlighted fields.", errors);
    }
}
