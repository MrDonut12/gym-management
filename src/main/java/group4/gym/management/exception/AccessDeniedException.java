package group4.gym.management.exception;

/**
 * Action exceeds the permissions of the RBAC matrix (section 2.3, CN-05, CN-34). Servlets respond with 403.
 */
public class AccessDeniedException extends AppException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
