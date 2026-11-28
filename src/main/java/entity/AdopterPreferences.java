package entity;

import java.util.Objects;

/**
 * What an adopter tells us about their home, saved once and reused to check pet compatibility.
 * Instances are immutable.
 */
public class AdopterPreferences {
    private final boolean hasChildren;
    private final boolean hasOtherPets;
    private final boolean canHandleMedicalNeeds;
    private final boolean canHandleKitten;
    private final boolean canHandlePregnantAnimal;
    private final Species preferredSpecies;

    /**
     * Creates a set of adopter preferences.
     *
     * @param hasChildren whether there are children at home
     * @param hasOtherPets whether there are other pets at home
     * @param canHandleMedicalNeeds whether the adopter can care for a pet with medical needs
     * @param canHandleKitten whether the adopter can take on a kitten
     * @param canHandlePregnantAnimal whether the adopter can take on a pregnant animal
     * @param preferredSpecies the species the adopter wants, or null for no preference
     */
    public AdopterPreferences(boolean hasChildren, boolean hasOtherPets, boolean canHandleMedicalNeeds,
                              boolean canHandleKitten, boolean canHandlePregnantAnimal,
                              Species preferredSpecies) {
        this.hasChildren = hasChildren;
        this.hasOtherPets = hasOtherPets;
        this.canHandleMedicalNeeds = canHandleMedicalNeeds;
        this.canHandleKitten = canHandleKitten;
        this.canHandlePregnantAnimal = canHandlePregnantAnimal;
        this.preferredSpecies = preferredSpecies;
    }

    /**
     * Returns whether there are children at home.
     *
     * @return true if the adopter has children
     */
    public boolean hasChildren() {
        return hasChildren;
    }

    /**
     * Returns whether there are other pets at home.
     *
     * @return true if the adopter has other pets
     */
    public boolean hasOtherPets() {
        return hasOtherPets;
    }

    /**
     * Returns whether the adopter can care for a pet with medical needs.
     *
     * @return true if medical needs are fine
     */
    public boolean canHandleMedicalNeeds() {
        return canHandleMedicalNeeds;
    }

    /**
     * Returns whether the adopter can take on a kitten.
     *
     * @return true if a kitten is fine
     */
    public boolean canHandleKitten() {
        return canHandleKitten;
    }

    /**
     * Returns whether the adopter can take on a pregnant animal.
     *
     * @return true if a pregnant animal is fine
     */
    public boolean canHandlePregnantAnimal() {
        return canHandlePregnantAnimal;
    }

    /**
     * Returns the species the adopter prefers.
     *
     * @return the preferred species, or null if the adopter has no preference
     */
    public Species getPreferredSpecies() {
        return preferredSpecies;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AdopterPreferences)) {
            return false;
        }
        AdopterPreferences that = (AdopterPreferences) other;
        return hasChildren == that.hasChildren
                && hasOtherPets == that.hasOtherPets
                && canHandleMedicalNeeds == that.canHandleMedicalNeeds
                && canHandleKitten == that.canHandleKitten
                && canHandlePregnantAnimal == that.canHandlePregnantAnimal
                && preferredSpecies == that.preferredSpecies;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasChildren, hasOtherPets, canHandleMedicalNeeds, canHandleKitten,
                canHandlePregnantAnimal, preferredSpecies);
    }
}
