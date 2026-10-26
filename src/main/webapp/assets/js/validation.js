/*
 * Client-side validation (section 6).
 * UI aid only: the backend always validates again (ValidationUtil + services) and never trusts the client result.
 * The validateXxxForm functions are called through onsubmit="return validateXxxForm(this);".
 * Placeholder: the validateXxxForm functions temporarily return true so forms can still be submitted until they are implemented.
 * Full name, email, package name and cancel reason are trimmed before checking; username and password are not.
 */

/** Login form: username and password are required. */
function validateLoginForm(form) {
    // TODO: Check that username and password are not empty; show errors with showFieldError.
    // Output: true if valid (allow submit), false otherwise.
    return true;
}

/** Member profile form (HL-01, HL-02, HL-04 to HL-06). */
function validateMemberForm(form) {
    // TODO: clearFieldErrors; isValidFullName, isValidPhone, isValidEmail (if provided), isValidDateOfBirth (if provided).
    // Output: true if valid, false otherwise.
    return true;
}

/** Staff account form (HL-01, HL-03, HL-04, HL-08 to HL-10). */
function validateAccountForm(form) {
    // TODO: isValidUsername and password + exact confirmation on create; isValidFullName; isValidPhone; email if provided; role.
    // Output: true if valid, false otherwise.
    return true;
}

/** Package form (HL-11 to HL-13). */
function validatePackageForm(form) {
    // TODO: Name 2-100 characters after trim; price digits only within 1..100000000; duration 1/3/6/12 on create.
    // Output: true if valid, false otherwise.
    return true;
}

/** Change/reset password form (HL-09): 8-64 characters and an exact confirmation. */
function validatePasswordForm(form) {
    // TODO: isValidPassword(newPassword) and newPassword === confirmPassword (no trim).
    // Output: true if valid, false otherwise.
    return true;
}

/** Cancel registration form (HL-15). */
function validateCancelForm(form) {
    // TODO: Reason 3-255 characters after trim.
    // Output: true if valid, false otherwise.
    return true;
}

/** HL-01: 2-100 characters after trim, no digits. */
function isValidFullName(value) {
    // TODO: Trim, check the length and /\d/.
    // Output: boolean.
    return false;
}

/** HL-02 / HL-03: /^0[0-9]{9}$/ for members and staff. */
function isValidPhone(value) {
    // TODO: Regex /^0[0-9]{9}$/.
    // Output: boolean.
    return false;
}

/** HL-04: at most 254 characters, /^[^@\s]+@[^@\s]+\.[^@\s]+$/. */
function isValidEmail(value) {
    // TODO: Check the length and the format.
    // Output: boolean.
    return false;
}

/** HL-05: date (yyyy-MM-dd from input type="date") from 1900-01-01 to today. */
function isValidDateOfBirth(value) {
    // TODO: Compare with 1900-01-01 and the current date.
    // Output: boolean.
    return false;
}

/** HL-08: /^[a-zA-Z][a-zA-Z0-9_]{3,49}$/. */
function isValidUsername(value) {
    // TODO: Regex check (no trim).
    // Output: boolean.
    return false;
}

/** HL-09: 8-64 characters, kept as entered. */
function isValidPassword(value) {
    // TODO: Length check (no trim).
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
