/*
 * Client-side validation (CN-30, section 3.4).
 * UI aid only: the backend always validates again (ValidationUtil) and never trusts the client result.
 * The validateXxxForm functions are called through onsubmit="return validateXxxForm(this);".
 * Placeholder: the validateXxxForm functions temporarily return true so forms can still be submitted until they are implemented.
 */

/** Login form: username and password are required. */
function validateLoginForm(form) {
    // TODO: Check that username and password are not empty; show errors with showFieldError.
    // Output: true if valid (allow submit), false otherwise.
    return true;
}

/** Member profile form: full name, phone, email, date of birth, gender. */
function validateMemberForm(form) {
    // TODO: clearFieldErrors; isValidFullName, isValidMemberPhone, isValidEmail (if provided), isNotFutureDate (if provided).
    // Output: true if valid, false otherwise.
    return true;
}

/** Staff account form (DL-07). */
function validateAccountForm(form) {
    // TODO: username required; password + confirmation on create; full name; isValidStaffPhone; email if provided; role.
    // Output: true if valid, false otherwise.
    return true;
}

/** Plan type form: name required, price is a positive integer. */
function validatePlanTypeForm(form) {
    // TODO: Name not empty; price is an integer > 0.
    // Output: true if valid, false otherwise.
    return true;
}

/** Change/reset password form: the new password and its confirmation must match. */
function validatePasswordForm(form) {
    // TODO: New password not empty, meets the minimum length, and equals the confirmation field.
    // Output: true if valid, false otherwise.
    return true;
}

/** Full name: 2-100 characters, not only whitespace. */
function isValidFullName(value) {
    // TODO: Trim and check the length.
    // Output: boolean.
    return false;
}

/** Member phone: exactly 10 digits, starting with 0. */
function isValidMemberPhone(value) {
    // TODO: Regex /^0\d{9}$/.
    // Output: boolean.
    return false;
}

/** Staff phone: exactly 10 digits. */
function isValidStaffPhone(value) {
    // TODO: Regex /^\d{10}$/.
    // Output: boolean.
    return false;
}

/** Email: valid format, at most 254 characters. */
function isValidEmail(value) {
    // TODO: Check the length and the format.
    // Output: boolean.
    return false;
}

/** Date (yyyy-MM-dd from input type="date") must not be after today. */
function isNotFutureDate(value) {
    // TODO: Compare with the current date.
    // Output: boolean.
    return false;
}

/** Show an error next to an input. */
function showFieldError(input, message) {
    // TODO: Add/update an error element right after the input (use textContent, never innerHTML).
    // Output: nothing returned.
}

/** Remove every error message inside the form. */
function clearFieldErrors(form) {
    // TODO: Remove the error elements added by showFieldError.
    // Output: nothing returned.
}
