package group4.gym.management.model.member;

/**
 * Member gender (HL-06); may be left empty (null).
 * The database stores the label: CHECK IN (N'Nam', N'Nữ', N'Khác') (DL-02).
 */
public enum Gender {
    /** Male */
    MALE("Nam"),
    /** Female */
    FEMALE("Nữ"),
    /** Other */
    OTHER("Khác");

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
