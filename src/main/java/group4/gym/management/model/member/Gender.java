package group4.gym.management.model.member;

/**
 * Member gender (VR-06); may be left empty (null).
 * The database stores the label: CHECK IN (N'Male', N'Female', N'Other') (DR-02).
 */
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    /** Value stored in members.gender */
    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Map a members.gender value back to the enum.
     *
     * @return the matching Gender; null for null or an unknown value
     */
    public static Gender fromLabel(String label) {
        for (Gender gender : values()) {
            if (gender.label.equals(label)) {
                return gender;
            }
        }
        return null;
    }
}
