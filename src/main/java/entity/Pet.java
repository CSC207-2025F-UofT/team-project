package entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * An immutable pet profile maintained by the shelter.
 */
public final class Pet {

    private final String id;
    private final String name;
    private final Species species;
    private final String breed;
    private final int ageYears;
    private final PetSex sex;
    private final List<Temperament> temperamentTags;
    private final String healthNotes;
    private final boolean needsMedication;
    private final boolean vaccinated;
    private final boolean spayedNeutered;
    private final BigDecimal adoptionFee;
    private final String photoPath;
    private final PetStatus status;

    /**
     * Creates a pet with a validated profile and an adoption status.
     * @param id the stable pet identifier
     * @param name the pet's name
     * @param species the kind of animal
     * @param breed the pet's breed
     * @param ageYears the pet's age in whole years
     * @param sex the pet's recorded sex
     * @param temperamentTags the pet's applicable temperament tags
     * @param healthNotes optional health notes
     * @param needsMedication whether the pet needs medication
     * @param vaccinated whether the pet is vaccinated
     * @param spayedNeutered whether the pet has been spayed or neutered
     * @param adoptionFee the nonnegative adoption fee, to the nearest cent
     * @param photoPath the optional path to the pet's photo
     * @param status the pet's current adoption status
     * @throws IllegalArgumentException if any required value is missing or invalid
     */
    public Pet(String id, String name, Species species, String breed, int ageYears, PetSex sex,
               List<Temperament> temperamentTags, String healthNotes, boolean needsMedication, boolean vaccinated,
               boolean spayedNeutered, BigDecimal adoptionFee, String photoPath, PetStatus status) {
        this.id = requireText(id, "Pet ID");
        this.name = requireText(name, "Name");
        this.breed = requireText(breed, "Breed");
        if (species == null) {
            throw new IllegalArgumentException("Species is required.");
        }
        if (ageYears < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        if (sex == null) {
            throw new IllegalArgumentException("Sex is required.");
        }
        if (temperamentTags != null && temperamentTags.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Each temperament tag must be valid.");
        }
        if (adoptionFee == null || adoptionFee.signum() < 0) {
            throw new IllegalArgumentException("Adoption fee must be zero or more.");
        }
        if (adoptionFee.scale() > 2 && adoptionFee.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Adoption fee must be in whole cents.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Adoption status is required.");
        }

        this.species = species;
        this.ageYears = ageYears;
        this.sex = sex;
        this.temperamentTags = temperamentTags == null ? List.of() : List.copyOf(temperamentTags);
        this.healthNotes = healthNotes == null ? "" : healthNotes.strip();
        this.needsMedication = needsMedication;
        this.vaccinated = vaccinated;
        this.spayedNeutered = spayedNeutered;
        this.adoptionFee = adoptionFee;
        this.photoPath = photoPath == null || photoPath.isBlank() ? "" : photoPath;
        this.status = status;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.strip();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public int getAgeYears() {
        return ageYears;
    }

    public PetSex getSex() {
        return sex;
    }

    public List<Temperament> getTemperamentTags() {
        return temperamentTags;
    }

    public String getHealthNotes() {
        return healthNotes;
    }

    /**
     * Reports whether the pet needs medication.
     * @return whether the pet needs medication
     */
    public boolean needsMedication() {
        return needsMedication;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public boolean isSpayedNeutered() {
        return spayedNeutered;
    }

    public BigDecimal getAdoptionFee() {
        return adoptionFee;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public PetStatus getStatus() {
        return status;
    }
}
