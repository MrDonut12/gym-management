package group4.gym.management.exception;

/**
 * Business rule violation (section 4), for example: registration is no longer PENDING (QT-02),
 * package is not on sale (QT-04), member is soft-deleted (QT-11), soft-delete blocked by an unexpired PAID (QT-09),
 * self-lock/self-role-change (QT-14).
 */
public class BusinessRuleException extends AppException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
