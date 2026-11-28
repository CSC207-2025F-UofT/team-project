package use_case.save_preferences;

/**
 * What the adopter ticked on the My profile page.
 */
public class SavePreferencesInputData {
    private final String adopterId;
    private final boolean hasChildren;
    private final boolean hasOtherPets;
    private final boolean canHandleMedicalNeeds;
    private final boolean canHandleKitten;
    private final boolean canHandlePregnantAnimal;

    /**
     * Creates the input.
     *
     * @param adopterId the adopter saving their profile
     * @param hasChildren whether there are children at home
     * @param hasOtherPets whether there are other pets at home
     * @param canHandleMedicalNeeds whether the adopter can care for a cat with medical needs
     * @param canHandleKitten whether the adopter can take on a kitten
     * @param canHandlePregnantAnimal whether the adopter can take on a pregnant cat
     */
    public SavePreferencesInputData(String adopterId, boolean hasChildren, boolean hasOtherPets,
                                    boolean canHandleMedicalNeeds, boolean canHandleKitten,
                                    boolean canHandlePregnantAnimal) {
        this.adopterId = adopterId;
        this.hasChildren = hasChildren;
        this.hasOtherPets = hasOtherPets;
        this.canHandleMedicalNeeds = canHandleMedicalNeeds;
        this.canHandleKitten = canHandleKitten;
        this.canHandlePregnantAnimal = canHandlePregnantAnimal;
    }

    /**
     * Returns the adopter's id.
     *
     * @return the adopter id
     */
    public String getAdopterId() {
        return adopterId;
    }

    /**
     * Returns whether there are children at home.
     *
     * @return true if there are
     */
    public boolean hasChildren() {
        return hasChildren;
    }

    /**
     * Returns whether there are other pets at home.
     *
     * @return true if there are
     */
    public boolean hasOtherPets() {
        return hasOtherPets;
    }

    /**
     * Returns whether the adopter can care for a cat with medical needs.
     *
     * @return true if they can
     */
    public boolean canHandleMedicalNeeds() {
        return canHandleMedicalNeeds;
    }

    /**
     * Returns whether the adopter can take on a kitten.
     *
     * @return true if they can
     */
    public boolean canHandleKitten() {
        return canHandleKitten;
    }

    /**
     * Returns whether the adopter can take on a pregnant cat.
     *
     * @return true if they can
     */
    public boolean canHandlePregnantAnimal() {
        return canHandlePregnantAnimal;
    }
}
