package interface_adapter.manage_pet;

import java.util.List;

import use_case.manage_pet.ManagePetOutputData.PetDetails;

/** Read-only listing data used by the management screen and public preview. */
public final class PetViewData {
    private final PetDetails details;

    PetViewData(PetDetails details) {
        this.details = details;
    }

    /** @return the listing ID */
    public String getId() {
        return details.getId();
    }

    /** @return the pet's name */
    public String getName() {
        return details.getName();
    }

    /** @return the species name */
    public String getSpecies() {
        return details.getSpecies();
    }

    /** @return the pet's breed */
    public String getBreed() {
        return details.getBreed();
    }

    /** @return age in whole years */
    public int getAgeYears() {
        return details.getAgeYears();
    }

    /** @return the pet's sex */
    public String getSex() {
        return details.getSex();
    }

    /** @return the temperament tag names */
    public List<String> getTemperamentTags() {
        return details.getTemperamentTags();
    }

    /** @return the health notes */
    public String getHealthNotes() {
        return details.getHealthNotes();
    }

    /** @return whether the pet needs medication */
    public boolean needsMedication() {
        return details.needsMedication();
    }

    /** @return whether the pet is vaccinated */
    public boolean isVaccinated() {
        return details.isVaccinated();
    }

    /** @return whether the pet is spayed or neutered */
    public boolean isSpayedNeutered() {
        return details.isSpayedNeutered();
    }

    /** @return the fee as a decimal string */
    public String getAdoptionFee() {
        return details.getAdoptionFee();
    }

    /** @return the path to the local photo */
    public String getPhotoPath() {
        return details.getPhotoPath();
    }

    /** @return the status name */
    public String getStatus() {
        return details.getStatus();
    }
}
