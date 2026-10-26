package group4.gym.management.model.registration;

import java.time.LocalDate;

/**
 * Filter criteria of the registration/transaction list (CN-24). A null field means "not filtered".
 */
public class RegistrationFilter {

    /** Status (optional) */
    private RegistrationStatus status;

    /** Package (optional) */
    private Integer packageId;

    /** Member code (HV000123 or 123) or phone, exact match (optional) */
    private String memberKeyword;

    /** Time axis of the date range (default CREATED_AT) */
    private DateAxis axis;

    /** Start date (inclusive, QT-13) */
    private LocalDate fromDate;

    /** End date (inclusive of the whole day, QT-13) */
    private LocalDate toDate;

    /** Page number >= 1 (HL-07) */
    private int page = 1;

    public RegistrationFilter() {
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getMemberKeyword() {
        return memberKeyword;
    }

    public void setMemberKeyword(String memberKeyword) {
        this.memberKeyword = memberKeyword;
    }

    public DateAxis getAxis() {
        return axis;
    }

    public void setAxis(DateAxis axis) {
        this.axis = axis;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
