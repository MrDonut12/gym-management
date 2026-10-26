package group4.gym.management.exception;

/**
 * ID in the URL/form is not a positive integer or the record does not exist (HL-17). Servlets respond with 404
 * without exposing system errors.
 */
public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message);
    }
}
