package interface_adapter.save_preferences;

/**
 * What the My profile page shows: the five answers, and a message about the last save.
 */
public class PreferencesState {
    private boolean hasChildren;
    private boolean hasOtherPets;
    private boolean canHandleMedicalNeeds;
    private boolean canHandleKitten;
    private boolean canHandlePregnantAnimal;
    private String message = "";
    private String error = "";

    /**
     * Returns whether there are children at home.
     *
     * @return true if there are
     */
    public boolean hasChildren() {
        return hasChildren;
    }

    /**
     * Sets whether there are children at home.
     *
     * @param hasChildren true if there are
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
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
     * Sets whether there are other pets at home.
     *
     * @param hasOtherPets true if there are
     */
    public void setHasOtherPets(boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
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
     * Sets whether the adopter can care for a cat with medical needs.
     *
     * @param canHandleMedicalNeeds true if they can
     */
    public void setCanHandleMedicalNeeds(boolean canHandleMedicalNeeds) {
        this.canHandleMedicalNeeds = canHandleMedicalNeeds;
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
     * Sets whether the adopter can take on a kitten.
     *
     * @param canHandleKitten true if they can
     */
    public void setCanHandleKitten(boolean canHandleKitten) {
        this.canHandleKitten = canHandleKitten;
    }

    /**
     * Returns whether the adopter can take on a pregnant cat.
     *
     * @return true if they can
     */
    public boolean canHandlePregnantAnimal() {
        return canHandlePregnantAnimal;
    }

    /**
     * Sets whether the adopter can take on a pregnant cat.
     *
     * @param canHandlePregnantAnimal true if they can
     */
    public void setCanHandlePregnantAnimal(boolean canHandlePregnantAnimal) {
        this.canHandlePregnantAnimal = canHandlePregnantAnimal;
    }

    /**
     * Returns the message about the last save.
     *
     * @return the message, empty if none
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message about the last save.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the error from the last attempt.
     *
     * @return the error, empty if none
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error from the last attempt.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }
}
