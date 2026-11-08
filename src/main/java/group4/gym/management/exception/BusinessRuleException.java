package group4.gym.management.exception;

/**
 * Business rule violation (section 4), for example: registration is no longer PENDING,
 * package is not on sale, member is soft-deleted, soft-delete blocked by an unexpired PAID,
 * self-lock/self-role-change.
 */
public class BusinessRuleException extends AppException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
