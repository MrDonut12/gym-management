package group4.gym.management.exception;

/**
 * Action is a "K" cell of the permission matrix (section 3) or a finer rule checked in the service layer
 * (for example a RECEPTIONIST asking for soft-deleted members, QT-11). Servlets respond with 403 and change no data.
 */
public class AccessDeniedException extends AppException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
