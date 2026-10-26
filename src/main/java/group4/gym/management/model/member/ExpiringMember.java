package group4.gym.management.model.member;

import java.time.LocalDate;

/**
 * One row of the expiring-soon list (CN-17, QT-08): code, full name, phone, package name, expiry date, days left.
 */
public class ExpiringMember {

    /** Member profile (code = member.getCode()) */
    private Member member;

    /** Package name snapshot of the PAID registration holding the largest end_date */
    private String packageName;

    /** Largest PAID end_date */
    private LocalDate endDate;

    /** Days of use left including today: end_date - today + 1 (1..7) */
    private int daysLeft;

    public ExpiringMember() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

}
