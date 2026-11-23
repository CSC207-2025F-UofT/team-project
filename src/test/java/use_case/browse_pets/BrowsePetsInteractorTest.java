package use_case.browse_pets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.InMemoryPetDataAccess;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class BrowsePetsInteractorTest {

    private static class FakePresenter implements BrowsePetsOutputBoundary {
        private List<Pet> shown;

        @Override
        public void presentPets(BrowsePetsOutputData outputData) {
            shown = outputData.getPets();
        }
    }

    private Pet pet(String id, int age, PetStatus status) {
        return new Pet(id, "Pet" + id, Species.CAT, "Mixed", age, PetSex.FEMALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", status);
    }

    private FakePresenter run(List<PetFilter> filters) {
        final InMemoryPetDataAccess dataAccess = new InMemoryPetDataAccess(List.of(
                pet("1", 1, PetStatus.AVAILABLE),
                pet("2", 5, PetStatus.AVAILABLE),
                pet("3", 5, PetStatus.ADOPTED)));
        final FakePresenter presenter = new FakePresenter();
        new BrowsePetsInteractor(dataAccess, presenter).execute(new BrowsePetsInputData(filters));
        return presenter;
    }

    @Test
    void noFiltersShowsAllAvailablePets() {
        assertEquals(2, run(List.of()).shown.size());
    }

    @Test
    void adoptedPetsAreNeverShown() {
        final PetFilter olderThanThree = pet -> pet.getAgeYears() > 3;

        final FakePresenter presenter = run(List.of(olderThanThree));

        assertEquals(1, presenter.shown.size());
        assertEquals("2", presenter.shown.get(0).getId());
    }

    @Test
    void allFiltersMustPass() {
        final PetFilter olderThanThree = pet -> pet.getAgeYears() > 3;
        final PetFilter rejectEverything = pet -> false;

        assertEquals(0, run(List.of(olderThanThree, rejectEverything)).shown.size());
    }
}
