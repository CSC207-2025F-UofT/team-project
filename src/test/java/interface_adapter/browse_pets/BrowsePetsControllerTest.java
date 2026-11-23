package interface_adapter.browse_pets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;
import use_case.browse_pets.BrowsePetsInputBoundary;
import use_case.browse_pets.BrowsePetsInputData;

class BrowsePetsControllerTest {

    private static class CapturingInteractor implements BrowsePetsInputBoundary {
        private BrowsePetsInputData received;

        @Override
        public void execute(BrowsePetsInputData inputData) {
            received = inputData;
        }
    }

    private Pet pet(int age, boolean needsMedication, Temperament... tags) {
        return new Pet("1", "Miso", Species.CAT, "Tabby", age, PetSex.FEMALE, List.of(tags), "", needsMedication,
                true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    private long matchCount(BrowsePetsInputData data, Pet pet) {
        return data.getFilters().stream().filter(filter -> filter.matches(pet)).count();
    }

    @Test
    void noChoicesSendsNoFilters() {
        final CapturingInteractor interactor = new CapturingInteractor();

        new BrowsePetsController(interactor).browse(AgeFilterOption.ANY, Set.of(), false);

        assertTrue(interactor.received.getFilters().isEmpty());
    }

    @Test
    void ageChoiceBecomesAnAgeFilter() {
        final CapturingInteractor interactor = new CapturingInteractor();

        new BrowsePetsController(interactor).browse(AgeFilterOption.ONE_TO_THREE, Set.of(), false);

        assertEquals(1, interactor.received.getFilters().size());
        assertTrue(interactor.received.getFilters().get(0).matches(pet(2, false)));
        assertEquals(0, matchCount(interactor.received, pet(6, false)));
    }

    @Test
    void temperamentChoiceBecomesATemperamentFilter() {
        final CapturingInteractor interactor = new CapturingInteractor();

        new BrowsePetsController(interactor).browse(AgeFilterOption.ANY, Set.of(Temperament.CALM), false);

        assertEquals(1, matchCount(interactor.received, pet(2, false, Temperament.CALM)));
        assertEquals(0, matchCount(interactor.received, pet(2, false, Temperament.SHY)));
    }

    @Test
    void noMedicalNeedsBecomesAHealthFilter() {
        final CapturingInteractor interactor = new CapturingInteractor();

        new BrowsePetsController(interactor).browse(AgeFilterOption.ANY, Set.of(), true);

        assertEquals(1, matchCount(interactor.received, pet(2, false)));
        assertEquals(0, matchCount(interactor.received, pet(2, true)));
    }

    @Test
    void severalChoicesSendSeveralFilters() {
        final CapturingInteractor interactor = new CapturingInteractor();

        new BrowsePetsController(interactor).browse(
                AgeFilterOption.UNDER_ONE, Set.of(Temperament.PLAYFUL), true);

        assertEquals(3, interactor.received.getFilters().size());
    }
}
