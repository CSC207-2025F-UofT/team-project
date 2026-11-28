package interface_adapter.browse_pets;

import java.util.EnumSet;
import java.util.Set;

import entity.Temperament;

/**
 * Everything the visitor picked in the filter panel. A new object starts with no filter at all.
 */
public class BrowseChoices {
    private AgeFilterOption age = AgeFilterOption.ANY;
    private SexFilterOption sex = SexFilterOption.ANY;
    private String breed = "";
    private Set<Temperament> temperaments = EnumSet.noneOf(Temperament.class);
    private boolean noMedicalNeedsOnly;
    private boolean vaccinatedOnly;
    private boolean spayedNeuteredOnly;

    /**
     * Returns the age choice.
     *
     * @return the age choice
     */
    public AgeFilterOption getAge() {
        return age;
    }

    /**
     * Sets the age choice.
     *
     * @param age the age choice
     */
    public void setAge(AgeFilterOption age) {
        this.age = age;
    }

    /**
     * Returns the sex choice.
     *
     * @return the sex choice
     */
    public SexFilterOption getSex() {
        return sex;
    }

    /**
     * Sets the sex choice.
     *
     * @param sex the sex choice
     */
    public void setSex(SexFilterOption sex) {
        this.sex = sex;
    }

    /**
     * Returns the text typed for breed.
     *
     * @return part of a breed name, empty for no breed filter
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Sets the text typed for breed.
     *
     * @param breed part of a breed name
     */
    public void setBreed(String breed) {
        this.breed = breed == null ? "" : breed;
    }

    /**
     * Returns the temperament tags a cat must all have.
     *
     * @return the tags, empty for none
     */
    public Set<Temperament> getTemperaments() {
        return temperaments;
    }

    /**
     * Sets the temperament tags a cat must all have.
     *
     * @param temperaments the tags, empty for none
     */
    public void setTemperaments(Set<Temperament> temperaments) {
        this.temperaments = temperaments;
    }

    /**
     * Returns whether only cats that need no medication are wanted.
     *
     * @return true if so
     */
    public boolean isNoMedicalNeedsOnly() {
        return noMedicalNeedsOnly;
    }

    /**
     * Sets whether only cats that need no medication are wanted.
     *
     * @param noMedicalNeedsOnly true if so
     */
    public void setNoMedicalNeedsOnly(boolean noMedicalNeedsOnly) {
        this.noMedicalNeedsOnly = noMedicalNeedsOnly;
    }

    /**
     * Returns whether only vaccinated cats are wanted.
     *
     * @return true if so
     */
    public boolean isVaccinatedOnly() {
        return vaccinatedOnly;
    }

    /**
     * Sets whether only vaccinated cats are wanted.
     *
     * @param vaccinatedOnly true if so
     */
    public void setVaccinatedOnly(boolean vaccinatedOnly) {
        this.vaccinatedOnly = vaccinatedOnly;
    }

    /**
     * Returns whether only spayed or neutered cats are wanted.
     *
     * @return true if so
     */
    public boolean isSpayedNeuteredOnly() {
        return spayedNeuteredOnly;
    }

    /**
     * Sets whether only spayed or neutered cats are wanted.
     *
     * @param spayedNeuteredOnly true if so
     */
    public void setSpayedNeuteredOnly(boolean spayedNeuteredOnly) {
        this.spayedNeuteredOnly = spayedNeuteredOnly;
    }
}
