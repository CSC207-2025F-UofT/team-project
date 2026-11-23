package use_case.manage_pet;

import java.util.ArrayList;
import java.util.List;

import entity.Pet;
import entity.PetStatus;
import entity.Temperament;

/** Snapshot returned to the presenter after a successful action. */
public final class ManagePetOutputData {
    private final List<PetDetails> pets;
    private final String message;

    ManagePetOutputData(List<Pet> pets, String message) {
        final List<PetDetails> details = new ArrayList<>();
        for (Pet pet : pets) {
            details.add(new PetDetails(pet));
        }
        this.pets = List.copyOf(details);
        this.message = message;
    }

    /** @return the current listing snapshots */
    public List<PetDetails> getPets() {
        return pets;
    }

    /** @return action feedback, or an empty string when refreshing */
    public String getMessage() {
        return message;
    }

    /** Pet profile values that can cross the output boundary. */
    public static final class PetDetails {
        private final String id;
        private final String name;
        private final String species;
        private final String breed;
        private final int ageYears;
        private final String sex;
        private final List<String> temperamentTags;
        private final String healthNotes;
        private final boolean needsMedication;
        private final boolean vaccinated;
        private final boolean spayedNeutered;
        private final String adoptionFee;
        private final String photoPath;
        private final String status;
        private final boolean available;

        private PetDetails(Pet pet) {
            this.id = pet.getId();
            this.name = pet.getName();
            this.species = pet.getSpecies().name();
            this.breed = pet.getBreed();
            this.ageYears = pet.getAgeYears();
            this.sex = pet.getSex().name();
            final List<String> tags = new ArrayList<>();
            for (Temperament temperament : pet.getTemperamentTags()) {
                tags.add(temperament.name());
            }
            this.temperamentTags = List.copyOf(tags);
            this.healthNotes = pet.getHealthNotes();
            this.needsMedication = pet.needsMedication();
            this.vaccinated = pet.isVaccinated();
            this.spayedNeutered = pet.isSpayedNeutered();
            this.adoptionFee = pet.getAdoptionFee().toPlainString();
            this.photoPath = pet.getPhotoPath();
            this.status = pet.getStatus().name();
            this.available = pet.getStatus() == PetStatus.AVAILABLE;
        }

        /** @return the listing ID */
        public String getId() {
            return id;
        }

        /** @return the pet's name */
        public String getName() {
            return name;
        }

        /** @return the species name */
        public String getSpecies() {
            return species;
        }

        /** @return the pet's breed */
        public String getBreed() {
            return breed;
        }

        /** @return age in whole years */
        public int getAgeYears() {
            return ageYears;
        }

        /** @return the pet's sex */
        public String getSex() {
            return sex;
        }

        /** @return the temperament tag names */
        public List<String> getTemperamentTags() {
            return temperamentTags;
        }

        /** @return the health notes */
        public String getHealthNotes() {
            return healthNotes;
        }

        /** @return whether the pet needs medication */
        public boolean needsMedication() {
            return needsMedication;
        }

        /** @return whether the pet is vaccinated */
        public boolean isVaccinated() {
            return vaccinated;
        }

        /** @return whether the pet is spayed or neutered */
        public boolean isSpayedNeutered() {
            return spayedNeutered;
        }

        /** @return the fee as a decimal string */
        public String getAdoptionFee() {
            return adoptionFee;
        }

        /** @return the path to the local photo */
        public String getPhotoPath() {
            return photoPath;
        }

        /** @return the status name */
        public String getStatus() {
            return status;
        }

        /** @return whether the listing is visible in the public gallery */
        public boolean isAvailable() {
            return available;
        }
    }
}
