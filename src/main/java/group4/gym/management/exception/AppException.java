package group4.gym.management.exception;

/**
 * Root exception of the application. Servlets catch it to show an appropriate message (GT-02).
 */
public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
