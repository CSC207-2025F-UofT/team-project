package interface_adapter.browse_pets;

import entity.PetSex;

/**
 * The sex choices offered in the gallery's filter panel.
 */
public enum SexFilterOption {
    /** No sex filter. */
    ANY("Any sex", null),
    /** Female cats. */
    FEMALE("Female", PetSex.FEMALE),
    /** Male cats. */
    MALE("Male", PetSex.MALE);

    private final String label;
    private final PetSex sex;

    SexFilterOption(String label, PetSex sex) {
        this.label = label;
        this.sex = sex;
    }

    /**
     * Returns the sex this choice stands for.
     *
     * @return the sex, or null for no sex filter
     */
    public PetSex getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return label;
    }
}
