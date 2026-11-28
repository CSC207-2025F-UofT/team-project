package use_case.manage_pet;

import java.util.List;

/** Form values submitted by the admin. */
public final class ManagePetInputData {
    private final String name;
    private final String species;
    private final String breed;
    private final String ageYears;
    private final String sex;
    private final List<String> temperamentTags;
    private final String healthNotes;
    private final boolean needsMedication;
    private final boolean vaccinated;
    private final boolean spayedNeutered;
    private final String adoptionFee;
    private final String photoPath;

    /**
     * Captures the submitted fields for validation in the use case.
     * @param name the pet's name
     * @param species the selected species
     * @param breed the pet's breed or mix
     * @param ageYears age entered in years
     * @param sex the selected sex
     * @param temperamentTags the selected temperament tags
     * @param healthNotes the pet's health notes
     * @param needsMedication whether the pet needs medication
     * @param vaccinated whether the pet is vaccinated
     * @param spayedNeutered whether the pet is spayed or neutered
     * @param adoptionFee the entered adoption fee
     * @param photoPath the path to an optional local photo
     */
    public ManagePetInputData(String name, String species, String breed, String ageYears, String sex,
                              List<String> temperamentTags, String healthNotes, boolean needsMedication,
                              boolean vaccinated, boolean spayedNeutered, String adoptionFee, String photoPath) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.ageYears = ageYears;
        this.sex = sex;
        this.temperamentTags = List.copyOf(temperamentTags);
        this.healthNotes = healthNotes;
        this.needsMedication = needsMedication;
        this.vaccinated = vaccinated;
        this.spayedNeutered = spayedNeutered;
        this.adoptionFee = adoptionFee;
        this.photoPath = photoPath;
    }

    String getName() {
        return name;
    }

    String getSpecies() {
        return species;
    }

    String getBreed() {
        return breed;
    }

    String getAgeYears() {
        return ageYears;
    }

    String getSex() {
        return sex;
    }

    List<String> getTemperamentTags() {
        return temperamentTags;
    }

    String getHealthNotes() {
        return healthNotes;
    }

    boolean needsMedication() {
        return needsMedication;
    }

    boolean isVaccinated() {
        return vaccinated;
    }

    boolean isSpayedNeutered() {
        return spayedNeutered;
    }

    String getAdoptionFee() {
        return adoptionFee;
    }

    String getPhotoPath() {
        return photoPath;
    }
}
