/*
 * Client-side validation (CN-30, section 3.4).
 * UI aid only: the backend always validates again (ValidationUtil) and never trusts the client result.
 * The validateXxxForm functions are called through onsubmit="return validateXxxForm(this);".
 * Every rule here must stay identical to ValidationUtil.java.
 * Errors are rendered as <span class="field-error js-field-error"> right after the input, using textContent only.
 */

/** Minimum password length; keep in sync with ValidationUtil.MIN_PASSWORD_LENGTH. */
var MIN_PASSWORD_LENGTH = 8;

/** Allowed plan durations, same values as the options of the duration select in plan/form.jsp. */
var PLAN_DURATIONS = ['1_DAY', '1_MONTH', '6_MONTH', '1_YEAR'];

/** Login form: username and password are required. */
function validateLoginForm(form) {
    clearFieldErrors(form);
    var username = fieldOf(form, 'username');
    var password = fieldOf(form, 'password');

    if (username && isBlankValue(username.value)) {
        showFieldError(username, 'Username is required.');
    }
    if (password && password.value === '') {
        showFieldError(password, 'Password is required.');
    }
    return finishValidation(form);
}

/** Member profile form: full name, phone, email, date of birth, gender. */
function validateMemberForm(form) {
    clearFieldErrors(form);
    var fullName = fieldOf(form, 'fullName');
    var phone = fieldOf(form, 'phone');
    var email = fieldOf(form, 'email');
    var dateOfBirth = fieldOf(form, 'dateOfBirth');

    if (fullName && !isValidFullName(fullName.value)) {
        showFieldError(fullName, 'Full name must be 2-100 characters.');
    }
    if (phone) {
        if (isBlankValue(phone.value)) {
            showFieldError(phone, 'Phone is required.');
        } else if (!isValidMemberPhone(phone.value)) {
            showFieldError(phone, 'Phone must be exactly 10 digits and start with 0.');
        }
    }
    if (email && !isBlankValue(email.value) && !isValidEmail(email.value)) {
        showFieldError(email, 'Email is not in a valid format (at most 254 characters).');
    }
    if (dateOfBirth && !isBlankValue(dateOfBirth.value) && !isNotFutureDate(dateOfBirth.value)) {
        showFieldError(dateOfBirth, 'Date of birth must not be after today.');
    }
    // Gender is optional and limited to the select options, nothing to validate.
    return finishValidation(form);
}

/** Staff account form (DL-07). */
function validateAccountForm(form) {
    clearFieldErrors(form);
    var username = fieldOf(form, 'username');
    var password = fieldOf(form, 'password');
    var confirmPassword = fieldOf(form, 'confirmPassword');
    var fullName = fieldOf(form, 'fullName');
    var phone = fieldOf(form, 'phone');
    var email = fieldOf(form, 'email');
    var role = fieldOf(form, 'role');

    // The username is only editable (and validated by the backend) on create.
    if (username && !username.readOnly && !username.disabled) {
        if (isBlankValue(username.value)) {
            showFieldError(username, 'Username is required.');
        } else if (username.value.trim().length > 50) {
            showFieldError(username, 'Username must be at most 50 characters.');
        }
    }
    // The password fields only exist on create.
    if (password) {
        if (isBlankValue(password.value)) {
            showFieldError(password, 'Password is required.');
        } else if (password.value.length < MIN_PASSWORD_LENGTH) {
            showFieldError(password, 'Password must be at least ' + MIN_PASSWORD_LENGTH + ' characters.');
        }
        if (confirmPassword) {
            if (isBlankValue(confirmPassword.value)) {
                showFieldError(confirmPassword, 'Please confirm the password.');
            } else if (confirmPassword.value !== password.value) {
                showFieldError(confirmPassword, 'Confirmation does not match the password.');
            }
        }
    }
    if (fullName && !isValidFullName(fullName.value)) {
        showFieldError(fullName, 'Full name must be 2-100 characters.');
    }
    if (phone) {
        if (isBlankValue(phone.value)) {
            showFieldError(phone, 'Phone is required.');
        } else if (!isValidStaffPhone(phone.value)) {
            showFieldError(phone, 'Phone must be exactly 10 digits.');
        }
    }
    if (email && !isBlankValue(email.value) && !isValidEmail(email.value)) {
        showFieldError(email, 'Email is not in a valid format (at most 254 characters).');
    }
    if (role && !role.disabled && isBlankValue(role.value)) {
        showFieldError(role, 'Role is required.');
    }
    return finishValidation(form);
}

/** Plan type form: name required, price is a positive integer. */
function validatePlanTypeForm(form) {
    clearFieldErrors(form);
    var name = fieldOf(form, 'name');
    var duration = fieldOf(form, 'duration');
    var price = fieldOf(form, 'price');

    if (name) {
        if (isBlankValue(name.value)) {
            showFieldError(name, 'Plan name is required.');
        } else if (name.value.trim().length > 100) {
            showFieldError(name, 'Plan name must be at most 100 characters.');
        }
    }
    // The duration is only submitted on create; on edit it is read-only and the backend ignores it.
    if (duration && !duration.disabled && PLAN_DURATIONS.indexOf(duration.value) === -1) {
        showFieldError(duration, 'Duration must be one of: 1 day, 1 month, 6 months, 1 year.');
    }
    if (price && !isPositiveInteger(price.value)) {
        showFieldError(price, 'Price must be an integer greater than 0.');
    }
    return finishValidation(form);
}

/** Change/reset password form: the new password and its confirmation must match. */
function validatePasswordForm(form) {
    clearFieldErrors(form);
    var currentPassword = fieldOf(form, 'currentPassword');
    var newPassword = fieldOf(form, 'newPassword');
    var confirmPassword = fieldOf(form, 'confirmPassword');

    // The current password only exists on the self-service change form; the backend verifies it.
    if (currentPassword && currentPassword.value === '') {
        showFieldError(currentPassword, 'Current password is required.');
    }
    if (newPassword) {
        if (isBlankValue(newPassword.value)) {
            showFieldError(newPassword, 'New password is required.');
        } else if (newPassword.value.length < MIN_PASSWORD_LENGTH) {
            showFieldError(newPassword, 'New password must be at least ' + MIN_PASSWORD_LENGTH + ' characters.');
        }
    }
    if (confirmPassword) {
        if (isBlankValue(confirmPassword.value)) {
            showFieldError(confirmPassword, 'Please confirm the new password.');
        } else if (newPassword && confirmPassword.value !== newPassword.value) {
            showFieldError(confirmPassword, 'Confirmation does not match the new password.');
        }
    }
    return finishValidation(form);
}

/** Full name: 2-100 characters, not only whitespace. */
function isValidFullName(value) {
    if (typeof value !== 'string') {
        return false;
    }
    var length = value.trim().length;
    return length >= 2 && length <= 100;
}

/** Member phone: exactly 10 digits, starting with 0. */
function isValidMemberPhone(value) {
    return typeof value === 'string' && /^0\d{9}$/.test(value);
}

/** Staff phone: exactly 10 digits. */
function isValidStaffPhone(value) {
    return typeof value === 'string' && /^\d{10}$/.test(value);
}

/** Email: valid format, at most 254 characters. */
function isValidEmail(value) {
    if (typeof value !== 'string') {
        return false;
    }
    var trimmed = value.trim();
    return trimmed.length <= 254
        && /^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$/.test(trimmed);
}

/** Date (yyyy-MM-dd from input type="date") must not be after today. */
function isNotFutureDate(value) {
    if (typeof value !== 'string') {
        return false;
    }
    var match = /^(\d{4})-(\d{2})-(\d{2})$/.exec(value);
    if (!match) {
        return false;
    }
    // Reject dates that do not exist, e.g. 2026-02-30.
    var parsed = new Date(Date.UTC(Number(match[1]), Number(match[2]) - 1, Number(match[3])));
    if (parsed.getUTCFullYear() !== Number(match[1])
        || parsed.getUTCMonth() !== Number(match[2]) - 1
        || parsed.getUTCDate() !== Number(match[3])) {
        return false;
    }
    // yyyy-MM-dd strings compare chronologically.
    return value <= todayInVietnam();
}

/** Show an error next to an input. */
function showFieldError(input, message) {
    if (!input || !input.parentNode) {
        return;
    }
    var next = input.nextElementSibling;
    var error = next && next.classList.contains('js-field-error') ? next : null;
    if (!error) {
        error = document.createElement('span');
        error.className = 'field-error js-field-error';
        error.setAttribute('role', 'alert');
        input.parentNode.insertBefore(error, input.nextSibling);
    }
    error.textContent = message;
    input.setAttribute('aria-invalid', 'true');
}

/** Remove every error message inside the form. */
function clearFieldErrors(form) {
    if (!form) {
        return;
    }
    var errors = form.querySelectorAll('.js-field-error');
    for (var i = 0; i < errors.length; i++) {
        errors[i].parentNode.removeChild(errors[i]);
    }
    var invalid = form.querySelectorAll('[aria-invalid]');
    for (var j = 0; j < invalid.length; j++) {
        invalid[j].removeAttribute('aria-invalid');
    }
}

/** Form control by name, or null when the form does not have it. */
function fieldOf(form, name) {
    var control = form.elements[name];
    return control && control.tagName ? control : null;
}

/** Null, undefined or whitespace-only value. */
function isBlankValue(value) {
    return value === null || value === undefined || String(value).trim() === '';
}

/** Whole number greater than 0 (digits only: no sign, decimals or exponent). */
function isPositiveInteger(value) {
    if (typeof value !== 'string' || !/^\d+$/.test(value.trim())) {
        return false;
    }
    var number = Number(value.trim());
    return Number.isSafeInteger(number) && number > 0;
}

/** Today's date as yyyy-MM-dd in Vietnam time (matches DateTimeUtil.VN_ZONE on the server). */
function todayInVietnam() {
    try {
        return new Intl.DateTimeFormat('en-CA', {
            timeZone: 'Asia/Ho_Chi_Minh', year: 'numeric', month: '2-digit', day: '2-digit'
        }).format(new Date());
    } catch (e) {
        var now = new Date();
        var month = String(now.getMonth() + 1).padStart(2, '0');
        var day = String(now.getDate()).padStart(2, '0');
        return now.getFullYear() + '-' + month + '-' + day;
    }
}

/** Focus the first invalid control; returns true when the form has no error. */
function finishValidation(form) {
    var first = form.querySelector('[aria-invalid="true"]');
    if (first) {
        first.focus();
        return false;
    }
    return true;
}
