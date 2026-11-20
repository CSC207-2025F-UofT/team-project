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

    private Pet pet(String id, Species species, PetStatus status) {
        return new Pet(id, "Pet" + id, species, "Mixed", 2, PetSex.FEMALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", status);
    }

    private FakePresenter run(List<PetFilter> filters) {
        final InMemoryPetDataAccess dataAccess = new InMemoryPetDataAccess(List.of(
                pet("1", Species.CAT, PetStatus.AVAILABLE),
                pet("2", Species.DOG, PetStatus.AVAILABLE),
                pet("3", Species.DOG, PetStatus.ADOPTED)));
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
        final FakePresenter presenter = run(List.of(new SpeciesFilter(Species.DOG)));

        assertEquals(1, presenter.shown.size());
        assertEquals("2", presenter.shown.get(0).getId());
    }

    @Test
    void allFiltersMustPass() {
        final PetFilter rejectEverything = pet -> false;

        assertEquals(0, run(List.of(new SpeciesFilter(Species.DOG), rejectEverything)).shown.size());
    }
}
