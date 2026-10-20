package group4.gym.management.exception;

/**
 * Business rule violation (section 3.2), for example: check-in at or after 20:00, expired plan,
 * soft-deleted member, duplicate PAID confirmation, check-out without an OPEN visit.
 */
public class BusinessRuleException extends AppException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
