package group4.gym.management.service;

import group4.gym.management.dao.AccountDAO;
import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.Account;
import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import group4.gym.management.util.PasswordUtil;

/** Run alone with -ea against local SQL Server. All test data is rolled back. */
public class AccountIntegrationSelfTest {
    public static void main(String[] args) throws Exception {
        try (Connection connection = new DBContext().getConnection()) {
            if (connection == null) throw new IllegalStateException("Database connection unavailable");
            connection.setAutoCommit(false);
            Driver original = DriverManager.getDriver(connection.getMetaData().getURL());
            Connection shared = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[]{Connection.class}, (proxy, method, values) -> {
                        if (method.getName().equals("close")) return null;
                        try { return method.invoke(connection, values); }
                        catch (InvocationTargetException ex) { throw ex.getCause(); }
                    });
            Driver testDriver = new Driver() {
                public Connection connect(String url, Properties info) throws SQLException {
                    return acceptsURL(url) ? shared : null;
                }
                public boolean acceptsURL(String url) throws SQLException { return original.acceptsURL(url); }
                public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) { return new DriverPropertyInfo[0]; }
                public int getMajorVersion() { return 1; }
                public int getMinorVersion() { return 0; }
                public boolean jdbcCompliant() { return false; }
                public Logger getParentLogger() { return Logger.getGlobal(); }
            };
            DriverManager.deregisterDriver(original);
            DriverManager.registerDriver(testDriver);
            try {
                checkAccounts();
            } finally {
                try { connection.rollback(); }
                finally {
                    DriverManager.deregisterDriver(testDriver);
                    DriverManager.registerDriver(original);
                }
            }
        }
        System.out.println("PASS: SQL Server DAO, authentication, RBAC, lock/unlock, soft-delete; test data rolled back");
    }

    private static void checkAccounts() {
        AccountDAO dao = new AccountDAO();
        AccountService service = new AccountService();
        AuthService auth = new AuthService();
        String prefix = "t08_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "T08-test-password";
        String hash = PasswordUtil.hash(password);
        Account admin = create(dao, prefix + "_admin", Role.ADMINISTRATOR, hash);
        Account manager = create(dao, prefix + "_manager", Role.MANAGER, hash);
        Account staff = create(dao, prefix + "_staff", Role.RECEPTIONIST, hash);
        Account owner = create(dao, prefix + "_owner", Role.OWNER, hash);
        assert dao.findById(staff.getId()).getCreatedAt() != null;
        assert dao.findByUsername(staff.getUsername().toUpperCase()).getId().equals(staff.getId());
        assert dao.findByUsername("' OR 1=1 --") == null;
        assert dao.existsByUsername(staff.getUsername().toUpperCase(), null);
        assert !dao.existsByUsername(staff.getUsername(), staff.getId());
        assert dao.existsByPhone(staff.getPhone(), null);
        assert !dao.existsByPhone(staff.getPhone(), staff.getId());
        assert dao.search(prefix, List.of(), null, true).isEmpty();
        assert dao.search(prefix, null, null, true).size() == 4;
        assert service.listManageableAccounts(manager, prefix, null, null, true).size() == 1;
        assert service.listManageableAccounts(manager, prefix, Role.OWNER, null, true).isEmpty();
        assert service.getManageableAccount(manager, staff.getId()).getPasswordHash() == null;
        for (Account item : dao.search(prefix, null, null, true)) assert item.getPasswordHash() == null;
        denied(() -> service.getManageableAccount(manager, owner.getId()));
        denied(() -> service.lockAccount(admin, admin.getId()));
        denied(() -> service.changeRole(manager, staff.getId(), Role.OWNER));
        denied(() -> service.softDeleteAccount(manager, staff.getId()));
        denied(() -> service.resetPassword(manager, staff.getId(), password, password));
        assert auth.authenticate(staff.getUsername(), password) != null;
        assert auth.authenticate(staff.getUsername(), "wrong") == null;
        assert auth.loadActiveAccount(staff.getId()).getPasswordHash() == null;
        service.lockAccount(manager, staff.getId());
        assert auth.loadActiveAccount(staff.getId()) == null;
        assert auth.authenticate(staff.getUsername(), password) == null;
        assert dao.search(prefix, null, AccountStatus.LOCKED, false).size() == 1;
        denied(() -> service.changeOwnPassword(staff, password, "new-password", "new-password"));
        service.unlockAccount(manager, staff.getId());
        assert auth.authenticate(staff.getUsername(), password) != null;
        service.changeRole(admin, staff.getId(), Role.ACCOUNTANT);
        assert dao.findById(staff.getId()).getRole() == Role.ACCOUNTANT;
        staff.setFullName("Updated test name");
        assert dao.updateProfile(staff);
        assert dao.findById(staff.getId()).getFullName().equals("Updated test name");
        try {
            service.changeOwnPassword(staff, "wrong", password, password);
            throw new AssertionError("Wrong current password accepted");
        } catch (ValidationException expected) { }
        // T04 is intentionally unfinished: account writes must fail closed until it returns an error map.
        try {
            service.updateAccount(manager, staff);
            throw new AssertionError("Missing T04 validation was bypassed");
        } catch (AppException expected) {
            assert expected.getMessage().contains("T04");
        }
        String changedPassword = "T08-changed-password";
        assert dao.updatePasswordHash(staff.getId(), PasswordUtil.hash(changedPassword));
        assert auth.authenticate(staff.getUsername(), password) == null;
        assert auth.authenticate(staff.getUsername(), changedPassword) != null;
        service.softDeleteAccount(admin, staff.getId());
        assert dao.findById(staff.getId()).isDeleted();
        assert auth.loadActiveAccount(staff.getId()) == null;
        assert dao.existsByUsername(staff.getUsername(), null) && dao.existsByPhone(staff.getPhone(), null);
        assert !dao.updateProfile(staff) && !dao.updatePasswordHash(staff.getId(), hash);
        assert dao.search(prefix, null, null, false).size() == 3;
        assert dao.search(prefix, null, null, true).size() == 4;
        Account duplicate = new Account();
        duplicate.setUsername(staff.getUsername().toUpperCase());
        duplicate.setPhone(staff.getPhone());
        duplicate.setFullName("Duplicate test");
        duplicate.setRole(Role.RECEPTIONIST);
        duplicate.setStatus(AccountStatus.ACTIVE);
        duplicate.setPasswordHash(hash);
        try { dao.insert(duplicate); throw new AssertionError("Duplicate accepted"); }
        catch (BusinessRuleException expected) { }
    }

    private static Account create(AccountDAO dao, String username, Role role, String hash) {
        Account account = new Account();
        account.setUsername(username);
        account.setFullName("T08 integration test");
        String phone;
        do { phone = "9" + String.format("%09d", new java.util.Random().nextInt(1000000000)); }
        while (dao.existsByPhone(phone, null));
        account.setPhone(phone);
        account.setRole(role);
        account.setStatus(AccountStatus.ACTIVE);
        account.setPasswordHash(hash);
        account.setId(dao.insert(account));
        return account;
    }

    private static void denied(Runnable action) {
        try { action.run(); throw new AssertionError("Unauthorized action accepted"); }
        catch (AccessDeniedException expected) { }
    }
}
