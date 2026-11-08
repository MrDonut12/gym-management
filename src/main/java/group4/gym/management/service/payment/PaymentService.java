package group4.gym.management.service.payment;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.payment.SchedulePreview;
import group4.gym.management.model.registration.PaymentMethod;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.AppConstants;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payment confirmation and schedule.
 * Amount and dates are never taken from the client.
 */
public class PaymentService {

    /** No payment confirmation for a soft-deleted member until the profile is restored. */
    public static final String MSG_MEMBER_DELETED =
            "The member profile is soft-deleted. Restore it before confirming the payment.";

    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * Registration with member display data, any status (used to show the saved schedule after step 2).
     */
    public RegistrationView getView(int regId) {
        RegistrationView view = regId > 0 ? registrationDAO.findViewById(regId) : null;
        if (view == null) {
            throw new NotFoundException("Registration not found.");
        }
        return view;
    }

    /**
     * Step 1 (review): member, package, amount snapshot and the expected schedule. Changes no data.
     * The schedule is only a preview: step 2 computes it again inside the transaction.
     */
    public SchedulePreview previewSchedule(int regId) {
        RegistrationView view = getView(regId);
        Registration registration = view.getRegistration();
        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING);
        }
        if (view.isMemberDeleted()) {
            throw new BusinessRuleException(MSG_MEMBER_DELETED);
        }

        LocalDate start = computeStartDate(AppClock.today(),
                registrationDAO.findMaxPaidEndDate(registration.getMemberId()));
        SchedulePreview preview = new SchedulePreview();
        preview.setRegistration(view);
        preview.setStartDate(start);
        preview.setEndDate(computeEndDate(start, registration.getDurationMonthsSnapshot()));
        return preview;
    }

    /**
     * Step 2 (final confirmation): PENDING -> PAID with paid_at, confirmed_by, payment_method, start_date, end_date
     * in ONE transaction. The order of confirmations decides the schedule order.
     *
     * @return the registration as saved, with the actual schedule
     */
    public Registration confirm(int regId, PaymentMethod method, int byId) {
        if (method == null) {
            throw new ValidationException("Select a payment method.",
                    Map.of("paymentMethod", "Select Cash or Bank transfer."));
        }
        // Read outside the transaction only to learn which member row to lock first.
        Registration existing = regId > 0 ? registrationDAO.findById(regId) : null;
        if (existing == null) {
            throw new NotFoundException("Registration not found.");
        }
        int memberId = existing.getMemberId();

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Lock the member row first, then re-check everything and write.
                Member member = memberDAO.lockForUpdate(conn, memberId);
                if (member == null) {
                    throw new NotFoundException("Member not found.");
                }
                if (member.isDeleted()) {
                    throw new BusinessRuleException(MSG_MEMBER_DELETED);
                }
                Registration registration = registrationDAO.findById(conn, regId);
                if (registration == null) {
                    throw new NotFoundException("Registration not found.");
                }
                if (registration.getStatus() != RegistrationStatus.PENDING) {
                    throw new BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING);
                }

                LocalDate start = computeStartDate(AppClock.today(),
                        registrationDAO.findMaxPaidEndDate(conn, memberId));
                LocalDate end = computeEndDate(start, registration.getDurationMonthsSnapshot());
                LocalDateTime paidAt = AppClock.now();
                // Conditional UPDATE: 0 rows means another request confirmed or cancelled it first.
                if (!registrationDAO.markPaidIfPending(conn, regId, method, paidAt, byId, start, end)) {
                    throw new BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING);
                }
                conn.commit();

                registration.setStatus(RegistrationStatus.PAID);
                registration.setPaymentMethod(method);
                registration.setPaidAt(paidAt);
                registration.setConfirmedBy(byId);
                registration.setStartDate(start);
                registration.setEndDate(end);
                return registration;
            } catch (SQLException | RuntimeException ex) {
                // Never leave a PAID without its schedule.
                DBContext.rollbackQuietly(conn);
                throw ex;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot confirm payment of registration " + regId, ex);
        }
    }

    /**
     * Start date: max(today, max PAID end_date + 1 day); the second term is skipped when there is no PAID.
     */
    public static LocalDate computeStartDate(LocalDate today, LocalDate maxPaidEndDate) {
        if (maxPaidEndDate == null) {
            return today;
        }
        LocalDate nextDay = maxPaidEndDate.plusDays(1);
        return nextDay.isAfter(today) ? nextDay : today;
    }

    /**
     * End date: startDate.plusMonths(durationMonths).minusDays(1). plusMonths clamps to the last day of a shorter month,
     * e.g. 31/01/2027 + 1 month -> 28/02/2027, so the end date is 27/02/2027.
     */
    public static LocalDate computeEndDate(LocalDate startDate, int durationMonths) {
        return startDate.plusMonths(durationMonths).minusDays(1);
    }
}
