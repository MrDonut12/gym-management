package group4.gym.management.service.member;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.MembershipStatus;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.AppConstants;
import java.time.LocalDate;
import java.util.List;

public class MembershipStatusService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();

    /**
     * Calculates today's membership status using only paid registrations.
     */
    public MembershipStatus statusOf(int memberId) {
        LocalDate today = AppClock.today();
        LocalDate currentEndDate = null;
        boolean coversToday = false;
        List<Registration> registrations = registrationDAO.findByMember(memberId);
        for (Registration registration : registrations) {
            if (registration.getStatus() == RegistrationStatus.PAID) {
                LocalDate startDate = registration.getStartDate();
                LocalDate endDate = registration.getEndDate();

                if (currentEndDate == null) {
                    currentEndDate = endDate;
                } else if (endDate.isAfter(currentEndDate)) {
                    currentEndDate = endDate;
                }

                boolean hasStarted = !startDate.isAfter(today);
                boolean hasNotEnded = !endDate.isBefore(today);
                if (hasStarted && hasNotEnded) {
                    coversToday = true;
                }
            }
        }
        return resolve(coversToday, currentEndDate, today);
    }

    /**
     * Lists profiles whose latest paid package has one to seven days of use left.
     */
    public List<ExpiringMember> expiringSoon() {
        return memberDAO.findExpiringSoon(AppClock.today(), AppConstants.EXPIRING_MAX_DAYS_AHEAD);
    }

    /**
     * Resolves paid-package information into NONE, ACTIVE, UPCOMING or EXPIRED.
     */
    public MembershipStatus resolve(boolean paidCoveringToday, LocalDate currentEndDate, LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("today is required");
        }
        if (currentEndDate == null) {
            return MembershipStatus.NONE;
        }
        if (paidCoveringToday) {
            return MembershipStatus.ACTIVE;
        }
        if (currentEndDate.isBefore(today)) {
            return MembershipStatus.EXPIRED;
        }
        return MembershipStatus.UPCOMING;
    }

    /**
     * Checks whether an active membership ends between today and six days from today.
     */
    public boolean isExpiringSoon(MembershipStatus status, LocalDate currentEndDate, LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("today is required");
        }
        if (status != MembershipStatus.ACTIVE) {
            return false;
        }
        if (currentEndDate == null) {
            return false;
        }
        if (currentEndDate.isBefore(today)) {
            return false;
        }

        LocalDate lastWarningDate = today.plusDays(AppConstants.EXPIRING_MAX_DAYS_AHEAD);
        if (currentEndDate.isAfter(lastWarningDate)) {
            return false;
        }
        return true;
    }
}
