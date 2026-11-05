package group4.gym.management.exception;

/**
 * Business rule violation (section 4), for example: registration is no longer PENDING (BR-02),
 * package is not on sale (BR-04), member is soft-deleted (BR-11), soft-delete blocked by an unexpired PAID (BR-09),
 * self-lock/self-role-change (BR-14).
 */
public class BusinessRuleException extends AppException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
