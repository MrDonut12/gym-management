package group4.gym.management.exception;

import java.util.Map;

/**
 * Invalid input according to section 6 (HL-01 to HL-17). Invalid data is rejected and never saved;
 * the form is shown again with the entered values (except passwords) and an error next to each field.
 */
public class ValidationException extends AppException {

    /** Errors per field: key = form field name, value = error message. */
    private final Map<String, String> fieldErrors;

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
