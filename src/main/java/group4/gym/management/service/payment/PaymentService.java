package group4.gym.management.service.payment;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.model.payment.SchedulePreview;
import group4.gym.management.model.registration.PaymentMethod;
import group4.gym.management.model.registration.Registration;
import java.time.LocalDate;

/**
 * Payment confirmation and schedule (CN-25, QT-02, QT-05, QT-06, QT-11).
 * Shared interface SRS 9.1 (owner TV4, used by TV3 for the Confirm button of CN-24).
 * Amount and dates are never taken from the client.
 */
public class PaymentService {

    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * Step 1 (review): member, package, amount snapshot and the expected schedule. Changes no data.
     */
    public SchedulePreview previewSchedule(int regId) {
        // TODO: view = registrationDAO.findViewById(regId); null -> NotFoundException;
        //       status != PENDING -> BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING);
        //       member deleted -> BusinessRuleException (QT-11);
        //       start = computeStartDate(AppClock.today(), registrationDAO.findMaxPaidEndDate(memberId));
        //       end = computeEndDate(start, durationMonthsSnapshot).
        // Output: SchedulePreview.
        return null;
    }

    /**
     * Step 2 (final confirmation): PENDING -> PAID with paid_at, confirmed_by, payment_method, start_date, end_date
     * in ONE transaction (QT-06, CL-06). The order of confirmations decides the schedule order (QT-05, KT-22).
     *
     * @return the registration as saved, with the actual schedule
     */
    public Registration confirm(int regId, PaymentMethod method, int byId) {
        // TODO: method == null -> ValidationException (HL-14);
        //       reg = registrationDAO.findById(regId) null -> NotFoundException (only to learn memberId);
        //       conn = DBContext.getConnection(); setAutoCommit(false);
        //       memberDAO.lockForUpdate(conn, memberId); deleted -> BusinessRuleException (QT-11, KT-23);
        //       reg = registrationDAO.findById(conn, regId); status != PENDING -> BusinessRuleException(MSG_REGISTRATION_NOT_PENDING);
        //       today = AppClock.today(); start = computeStartDate(today, registrationDAO.findMaxPaidEndDate(conn, memberId));
        //       end = computeEndDate(start, reg.getDurationMonthsSnapshot());
        //       registrationDAO.markPaidIfPending(conn, regId, method, AppClock.now(), byId, start, end) == false
        //       -> BusinessRuleException(MSG_REGISTRATION_NOT_PENDING) (KT-23: double confirmation);
        //       commit; any error -> DBContext.rollbackQuietly and rethrow (never leave a PAID without a schedule).
        // Output: the saved Registration.
        return null;
    }

    /**
     * QT-05 start: max(today, max PAID end_date + 1 day); the second term is skipped when there is no PAID.
     */
    public static LocalDate computeStartDate(LocalDate today, LocalDate maxPaidEndDate) {
        // TODO: maxPaidEndDate == null -> today; otherwise the later of today and maxPaidEndDate.plusDays(1).
        // Output: start_date (KT-21 examples in SRS 4.2).
        return null;
    }

    /**
     * QT-05 end: startDate.plusMonths(durationMonths).minusDays(1). plusMonths clamps to the last day of a shorter month.
     */
    public static LocalDate computeEndDate(LocalDate startDate, int durationMonths) {
        // TODO: startDate.plusMonths(durationMonths).minusDays(1).
        // Output: end_date (31/01/2027 + 1 month -> 27/02/2027).
        return null;
    }
}
