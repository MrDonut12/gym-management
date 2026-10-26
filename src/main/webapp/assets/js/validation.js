/*
 * Client-side validation (section 6).
 * UI aid only: the backend always validates again (ValidationUtil + services) and never trusts the client result.
 * The validateXxxForm functions are called through onsubmit="return validateXxxForm(this);".
 * Full name, email, package name and cancel reason are trimmed before checking; username and password are not.
 */

/** Return the named field of a form, or null when it is absent or not submitted (disabled). */
function getActiveField(form, name) {
    var field = form.elements[name];
    if (!field || field.disabled || field.type === 'hidden') {
        return null;
    }
    return field;
}

/** Login form: username and password are required. */
function validateLoginForm(form) {
    clearFieldErrors(form);
    var ok = true;
    var username = getActiveField(form, 'username');
    var password = getActiveField(form, 'password');
    if (username && username.value === '') {
        showFieldError(username, 'Username is required.');
        ok = false;
    }
    if (password && password.value === '') {
        showFieldError(password, 'Password is required.');
        ok = false;
    }
    return ok;
}

/** Member profile form (HL-01, HL-02, HL-04 to HL-06). */
function validateMemberForm(form) {
    clearFieldErrors(form);
    var ok = true;
    var fullName = getActiveField(form, 'fullName');
    var phone = getActiveField(form, 'phone');
    var email = getActiveField(form, 'email');
    var dob = getActiveField(form, 'dateOfBirth');
    if (fullName && !isValidFullName(fullName.value)) {
        showFieldError(fullName, 'Full name must be 2-100 characters and contain no digits.');
        ok = false;
    }
    if (phone && !isValidPhone(phone.value)) {
        showFieldError(phone, 'Phone must be 10 digits and start with 0.');
        ok = false;
    }
    if (email && email.value.trim() !== '' && !isValidEmail(email.value)) {
        showFieldError(email, 'Email is invalid (at most 254 characters).');
        ok = false;
    }
    if (dob && dob.value !== '' && !isValidDateOfBirth(dob.value)) {
        showFieldError(dob, 'Date of birth must be between 01/01/1900 and today.');
        ok = false;
    }
    return ok;
}

/** Staff account form (HL-01, HL-03, HL-04, HL-08 to HL-10). */
function validateAccountForm(form) {
    clearFieldErrors(form);
    var ok = true;
    var username = getActiveField(form, 'username');
    var password = getActiveField(form, 'password');
    var confirm = getActiveField(form, 'confirmPassword');
    var fullName = getActiveField(form, 'fullName');
    var phone = getActiveField(form, 'phone');
    var email = getActiveField(form, 'email');
    var role = getActiveField(form, 'role');

    // The username is editable only on create; on edit it is read-only and the server ignores it.
    if (username && !username.readOnly && !isValidUsername(username.value)) {
        showFieldError(username, 'Username must be 4-50 characters: letters, digits or _, starting with a letter.');
        ok = false;
    }
    // The password fields exist only on create.
    if (password && !isValidPassword(password.value)) {
        showFieldError(password, 'Password must be 8-64 characters.');
        ok = false;
    }
    if (confirm && password && confirm.value !== password.value) {
        showFieldError(confirm, 'Confirmation does not match the password.');
        ok = false;
    }
    if (fullName && !isValidFullName(fullName.value)) {
        showFieldError(fullName, 'Full name must be 2-100 characters and contain no digits.');
        ok = false;
    }
    if (phone && !isValidPhone(phone.value)) {
        showFieldError(phone, 'Phone must be 10 digits and start with 0.');
        ok = false;
    }
    if (email && email.value.trim() !== '' && !isValidEmail(email.value)) {
        showFieldError(email, 'Email is invalid (at most 254 characters).');
        ok = false;
    }
    if (role && role.value === '') {
        showFieldError(role, 'Role is required.');
        ok = false;
    }
    return ok;
}

/** Package form (HL-11 to HL-13). */
function validatePackageForm(form) {
    clearFieldErrors(form);
    var ok = true;
    var name = getActiveField(form, 'name');
    var price = getActiveField(form, 'price');
    var duration = getActiveField(form, 'durationMonths');

    if (name) {
        var trimmed = name.value.trim();
        if (trimmed.length < 2 || trimmed.length > 100) {
            showFieldError(name, 'Package name must be 2-100 characters.');
            ok = false;
        }
    }
    if (price) {
        var raw = price.value.trim();
        var amount = /^[0-9]+$/.test(raw) && raw.length <= 18 ? Number(raw) : NaN;
        if (!(amount >= 1 && amount <= 100000000)) {
            showFieldError(price, 'Price must be a whole number from 1 to 100,000,000 (digits only).');
            ok = false;
        }
    }
    // The duration is submitted only on create (it cannot be changed afterwards, CN-20).
    if (duration && ['1', '3', '6', '12'].indexOf(duration.value) === -1) {
        showFieldError(duration, 'Duration must be 1, 3, 6 or 12 months.');
        ok = false;
    }
    return ok;
}

/** Change/reset password form (HL-09): 8-64 characters and an exact confirmation. */
function validatePasswordForm(form) {
    clearFieldErrors(form);
    var ok = true;
    var current = getActiveField(form, 'currentPassword');
    var newPassword = getActiveField(form, 'newPassword');
    var confirm = getActiveField(form, 'confirmPassword');

    // Only the change-password form has the current-password field.
    if (current && current.value === '') {
        showFieldError(current, 'Current password is required.');
        ok = false;
    }
    if (newPassword && !isValidPassword(newPassword.value)) {
        showFieldError(newPassword, 'Password must be 8-64 characters.');
        ok = false;
    }
    if (confirm && newPassword && confirm.value !== newPassword.value) {
        showFieldError(confirm, 'Confirmation does not match the new password.');
        ok = false;
    }
    return ok;
}

/** Cancel registration form (HL-15). */
function validateCancelForm(form) {
    clearFieldErrors(form);
    var reason = getActiveField(form, 'reason');
    if (reason) {
        var trimmed = reason.value.trim();
        if (trimmed.length < 3 || trimmed.length > 255) {
            showFieldError(reason, 'Cancel reason must be 3-255 characters.');
            return false;
        }
    }
    return true;
}

/** HL-01: 2-100 characters after trim, no digits. */
function isValidFullName(value) {
    var trimmed = String(value).trim();
    return trimmed.length >= 2 && trimmed.length <= 100 && !/[0-9]/.test(trimmed);
}

/** HL-02 / HL-03: /^0[0-9]{9}$/ for members and staff. */
function isValidPhone(value) {
    return /^0[0-9]{9}$/.test(value);
}

/** HL-04: at most 254 characters, /^[^@\s]+@[^@\s]+\.[^@\s]+$/. */
function isValidEmail(value) {
    var trimmed = String(value).trim();
    return trimmed.length <= 254 && /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(trimmed);
}

/** HL-05: date (yyyy-MM-dd from input type="date") from 1900-01-01 to today. */
function isValidDateOfBirth(value) {
    if (!/^\d{4}-\d{2}-\d{2}$/.test(value) || isNaN(new Date(value + 'T00:00:00').getTime())) {
        return false;
    }
    var now = new Date();
    var today = now.getFullYear() + '-'
            + String(now.getMonth() + 1).padStart(2, '0') + '-'
            + String(now.getDate()).padStart(2, '0');
    // yyyy-MM-dd strings compare correctly as text.
    return value >= '1900-01-01' && value <= today;
}

/** HL-08: /^[a-zA-Z][a-zA-Z0-9_]{3,49}$/. */
function isValidUsername(value) {
    return /^[a-zA-Z][a-zA-Z0-9_]{3,49}$/.test(value);
}

/** HL-09: 8-64 characters, kept as entered. */
function isValidPassword(value) {
    return value.length >= 8 && value.length <= 64;
}

/** Show an error next to an input. */
function showFieldError(input, message) {
    var next = input.nextElementSibling;
    var error = (next && next.hasAttribute('data-client-error')) ? next : null;
    if (!error) {
        error = document.createElement('div');
        error.className = 'field-error text-danger small';
        error.setAttribute('data-client-error', '');
        input.insertAdjacentElement('afterend', error);
    }
    error.textContent = message;
    input.classList.add('is-invalid');
}

/** Remove every error message inside the form. */
function clearFieldErrors(form) {
    // Only the elements added by showFieldError; server-rendered errors are left alone.
    var errors = form.querySelectorAll('[data-client-error]');
    for (var i = 0; i < errors.length; i++) {
        errors[i].remove();
    }
    var invalid = form.querySelectorAll('.is-invalid');
    for (var j = 0; j < invalid.length; j++) {
        invalid[j].classList.remove('is-invalid');
    }
}
