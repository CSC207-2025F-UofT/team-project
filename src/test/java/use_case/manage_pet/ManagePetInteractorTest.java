package use_case.manage_pet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class ManagePetInteractorTest {
    private final InMemoryPetStore pets = new InMemoryPetStore();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final ManagePetInteractor interactor = new ManagePetInteractor(pets, presenter);

    @Test
    void addsAnAvailablePetWithTheEnteredDetails() {
        interactor.add(form(" Mochi ", "0", "85.50"));

        final Pet saved = pets.getPets().get(0);
        assertFalse(saved.getId().isBlank());
        assertEquals("Mochi", saved.getName());
        assertEquals(PetStatus.AVAILABLE, saved.getStatus());
        assertEquals(0, saved.getAgeYears());
        assertEquals(new BigDecimal("85.50"), saved.getAdoptionFee());
        assertEquals(saved.getId(), presenter.output.getPets().get(0).getId());
        assertTrue(presenter.output.getPets().get(0).isAvailable());
        assertEquals(List.of("SHY", "PLAYFUL"), presenter.output.getPets().get(0).getTemperamentTags());
        assertNull(presenter.error);
    }

    @Test
    void editsTheExistingProfileAndPreservesItsIdAndStatus() {
        pets.add(pendingPet());

        interactor.edit("pet-1", form("Mochi", "5", "0"));

        final Pet saved = pets.findById("pet-1");
        assertEquals("Mochi", saved.getName());
        assertEquals(5, saved.getAgeYears());
        assertEquals(PetStatus.PENDING, saved.getStatus());
        assertFalse(presenter.output.getPets().get(0).isAvailable());
        assertEquals(1, pets.getPets().size());
    }

    @Test
    void removesTheListingAndReturnsAnEmptySnapshot() {
        interactor.add(form("Mochi", "4", "85"));
        final String id = pets.getPets().get(0).getId();

        interactor.remove(id);

        assertNull(pets.findById(id));
        assertTrue(presenter.output.getPets().isEmpty());
    }

    @Test
    void rejectsInvalidFormValuesBeforeWriting() {
        interactor.add(form(" ", "1", "5"));
        assertTrue(presenter.error.toLowerCase().contains("name"));

        interactor.add(form("Mochi", "1.5", "5"));
        assertTrue(presenter.error.contains("whole years"));

        interactor.add(form("Mochi", "1", "3.456"));
        assertTrue(presenter.error.toLowerCase().contains("fee"));

        assertTrue(pets.getPets().isEmpty());
        assertNull(presenter.output);
    }

    @Test
    void leavesOriginalProfileIntactWhenEditFailsValidation() {
        final Pet original = pendingPet();
        pets.add(original);

        interactor.edit(original.getId(), form("Mochi", "-1", "5"));

        assertEquals(original, pets.findById(original.getId()));
        assertTrue(presenter.error.toLowerCase().contains("age"));
    }

    @Test
    void reportsAnEditOrRemovalOfAMissingPet() {
        interactor.edit("missing", form("Mochi", "1", "5"));
        assertTrue(presenter.error.contains("no longer exists"));

        interactor.remove("missing");
        assertTrue(presenter.error.contains("no longer exists"));
        assertNull(presenter.output);
    }

    @Test
    void reportsStorageFailureWithoutPresentingSuccess() {
        pets.failWrites = true;

        interactor.add(form("Mochi", "4", "85"));

        assertTrue(presenter.error.contains("could not be added"));
        assertNull(presenter.output);
        assertTrue(pets.getPets().isEmpty());
    }

    @Test
    void rejectsMissingOrUnknownChoicesAndMissingNumbers() {
        interactor.add(formWith(null, "FEMALE", List.of(), "1", "5"));
        assertEquals("Choose a species.", presenter.error);

        interactor.add(formWith("CAT", "DRAGON", List.of(), "1", "5"));
        assertEquals("Choose the pet's sex.", presenter.error);

        interactor.add(formWith("CAT", "FEMALE", List.of("GRUMPY"), "1", "5"));
        assertEquals("Choose valid temperament tags.", presenter.error);

        interactor.add(formWith("CAT", "FEMALE", List.of(), null, "5"));
        assertEquals("Enter the age in whole years.", presenter.error);

        interactor.add(formWith("CAT", "FEMALE", List.of(), "1", null));
        assertTrue(presenter.error.contains("adoption fee"));

        interactor.add(formWith("CAT", "FEMALE", List.of(), "1", "free"));
        assertTrue(presenter.error.contains("adoption fee"));

        assertTrue(pets.getPets().isEmpty());
    }

    @Test
    void reportsAPetThatDisappearsDuringAnEdit() {
        pets.add(pendingPet());
        pets.updateFindsNothing = true;

        interactor.edit("pet-1", form("Mochi", "5", "0"));

        assertEquals("This pet listing no longer exists.", presenter.error);
        assertNull(presenter.output);
    }

    @Test
    void reportsStorageFailuresForEditAndRemove() {
        pets.add(pendingPet());
        pets.failWrites = true;

        interactor.edit("pet-1", form("Mochi", "5", "0"));
        assertEquals("The pet could not be updated. Please try again.", presenter.error);

        interactor.remove("pet-1");
        assertEquals("The pet could not be removed. Please try again.", presenter.error);

        assertEquals("Original", pets.findById("pet-1").getName());
        assertNull(presenter.output);
    }

    @Test
    void reportsAListThatCannotBeLoaded() {
        pets.failReads = true;

        interactor.refresh();
        assertEquals("The pet list could not be loaded. Please refresh.", presenter.error);

        interactor.add(form("Mochi", "4", "85"));
        assertEquals("Added Mochi. The pet list could not be loaded. Please refresh.", presenter.error);
        assertNull(presenter.output);
    }

    private static ManagePetInputData formWith(String species, String sex, List<String> tags, String age,
                                               String fee) {
        return new ManagePetInputData("Mochi", species, "Domestic shorthair", age, sex, tags, "", false, true,
                false, fee, "");
    }

    private static ManagePetInputData form(String name, String age, String fee) {
        return new ManagePetInputData(name, "CAT", "Domestic shorthair", age, "FEMALE", List.of("SHY", "PLAYFUL"),
                "Requires eye drops", true, true, false, fee, "");
    }

    private static Pet pendingPet() {
        return new Pet("pet-1", "Original", Species.CAT, "Domestic shorthair", 2, PetSex.FEMALE, List.of(),
                "", false, false, false, BigDecimal.ZERO, "", PetStatus.PENDING);
    }

    private static final class RecordingPresenter implements ManagePetOutputBoundary {
        private ManagePetOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(ManagePetOutputData outputData) {
            output = outputData;
            error = null;
        }

        @Override
        public void prepareFailView(String message) {
            output = null;
            error = message;
        }
    }

    private static final class InMemoryPetStore implements ManagePetDataAccessInterface {
        private final Map<String, Pet> listings = new LinkedHashMap<>();
        private boolean failWrites;
        private boolean failReads;
        private boolean updateFindsNothing;

        @Override
        public List<Pet> getPets() {
            if (failReads) {
                throw new IllegalStateException("File read failed");
            }
            return List.copyOf(listings.values());
        }

        @Override
        public Pet findById(String id) {
            return listings.get(id);
        }

        @Override
        public void add(Pet pet) {
            if (failWrites) {
                throw new IllegalStateException("File write failed");
            }
            listings.put(pet.getId(), pet);
        }

        @Override
        public boolean update(Pet pet) {
            if (failWrites) {
                throw new IllegalStateException("File write failed");
            }
            if (updateFindsNothing) {
                return false;
            }
            return listings.replace(pet.getId(), pet) != null;
        }

        @Override
        public boolean remove(String id) {
            if (failWrites) {
                throw new IllegalStateException("File write failed");
            }
            return listings.remove(id) != null;
        }
    }
}
