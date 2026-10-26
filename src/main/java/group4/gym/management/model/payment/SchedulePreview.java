package group4.gym.management.model.payment;

import group4.gym.management.model.registration.RegistrationView;
import java.time.LocalDate;

/**
 * Review step of the payment confirmation (CN-25 step 1): member, package and amount snapshot, and the expected
 * schedule computed by QT-05 at viewing time. The final schedule is computed again inside the confirm transaction.
 */
public class SchedulePreview {

    /** PENDING registration with member data */
    private RegistrationView registration;

    /** Expected start_date */
    private LocalDate startDate;

    /** Expected end_date */
    private LocalDate endDate;

    public SchedulePreview() {
    }

    public RegistrationView getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationView registration) {
        this.registration = registration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
