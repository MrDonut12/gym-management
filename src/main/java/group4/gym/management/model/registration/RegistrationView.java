package group4.gym.management.model.registration;

/**
 * Registration row with member display data (CN-24, CN-25 review step).
 */
public class RegistrationView {

    /** Registration (snapshot, status, payment and schedule columns) */
    private Registration registration;

    /** Member code, for example HV000123 */
    private String memberCode;

    /** Member full name */
    private String memberName;

    /** Member phone */
    private String memberPhone;

    /** Member is soft-deleted (QT-11: no payment confirmation until restored) */
    private boolean memberDeleted;

    public RegistrationView() {
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public boolean isMemberDeleted() {
        return memberDeleted;
    }

    public void setMemberDeleted(boolean memberDeleted) {
        this.memberDeleted = memberDeleted;
    }

}
