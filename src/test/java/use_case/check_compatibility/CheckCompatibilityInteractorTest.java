package use_case.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class CheckCompatibilityInteractorTest {
    private final Pet pet = new Pet("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(), "", true,
            true, true, BigDecimal.TEN, "", PetStatus.AVAILABLE);
    private final List<String> events = new ArrayList<>();
    private final List<RuleResult> shown = new ArrayList<>();

    private final CheckCompatibilityOutputBoundary presenter = new CheckCompatibilityOutputBoundary() {
        @Override
        public void presentCompatibility(CheckCompatibilityOutputData outputData) {
            events.add("compat:" + outputData.getPetName());
            shown.addAll(outputData.getResults());
        }

        @Override
        public void presentNoPreferences() {
            events.add("no preferences");
        }

        @Override
        public void presentNotFound(String message) {
            events.add("not found:" + message);
        }
    };

    private CheckCompatibilityInteractor interactor(Map<String, AdopterPreferences> saved) {
        final CheckCompatibilityPetDataAccessInterface pets =
                id -> "1".equals(id) ? Optional.of(pet) : Optional.empty();
        final AdopterPreferencesDataAccessInterface preferences = new AdopterPreferencesDataAccessInterface() {
            @Override
            public Optional<AdopterPreferences> findByAdopterId(String adopterId) {
                return Optional.ofNullable(saved.get(adopterId));
            }

            @Override
            public void save(String adopterId, AdopterPreferences value) {
                throw new UnsupportedOperationException();
            }
        };
        return new CheckCompatibilityInteractor(pets, preferences,
                CompatibilityEngine.withStandardRules(), presenter);
    }

    @Test
    void unknownCatIsReported() {
        interactor(Map.of()).execute(new CheckCompatibilityInputData("nope", "a1"));

        assertEquals(List.of("not found:This cat could not be found."), events);
    }

    @Test
    void adopterWithoutPreferencesIsAskedToFillThemIn() {
        interactor(Map.of()).execute(new CheckCompatibilityInputData("1", "a1"));

        assertEquals(List.of("no preferences"), events);
    }

    @Test
    void anAdopterWhoIsNotLoggedInIsAskedToFillInTheProfile() {
        interactor(Map.of("", new AdopterPreferences(false, false, false, false, false, null)))
                .execute(new CheckCompatibilityInputData("1", ""));

        assertEquals(List.of("no preferences"), events);
    }

    @Test
    void savedPreferencesGiveOneResultPerRule() {
        final AdopterPreferences cannotTakeMedication = new AdopterPreferences(false, false, false, true, false, null);

        interactor(Map.of("a1", cannotTakeMedication)).execute(new CheckCompatibilityInputData("1", "a1"));

        assertEquals(List.of("compat:Miso"), events);
        assertEquals(4, shown.size());
        assertTrue(shown.stream().anyMatch(r -> r.getStatus() == RuleResult.Status.CONFLICT
                && "Medical needs".equals(r.getTrait())));
    }
}
