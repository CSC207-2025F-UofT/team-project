package interface_adapter.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import use_case.check_compatibility.CheckCompatibilityOutputData;
import use_case.check_compatibility.RuleResult;

class CheckCompatibilityPresenterTest {
    private final CompatibilityViewModel viewModel = new CompatibilityViewModel();
    private final CheckCompatibilityPresenter presenter = new CheckCompatibilityPresenter(viewModel);

    @Test
    void everyLineStartsWithAWord() {
        presenter.presentCompatibility(new CheckCompatibilityOutputData("Miso", List.of(
                new RuleResult("Children at home", RuleResult.Status.MATCH, "Miso is good with kids."),
                new RuleResult("Medical needs", RuleResult.Status.CONFLICT, "Needs daily medication."),
                new RuleResult("Kitten care", RuleResult.Status.NOT_APPLICABLE, "Miso is not a kitten."))));

        final CompatibilityState state = viewModel.getState();
        assertEquals("Match: Children at home - Miso is good with kids.", state.getLines().get(0));
        assertEquals("Conflict: Medical needs - Needs daily medication.", state.getLines().get(1));
        assertEquals("Not relevant: Kitten care - Miso is not a kitten.", state.getLines().get(2));
        assertEquals(List.of(false, true, false), state.getConflicts());
    }

    @Test
    void headlineCountsConflicts() {
        presenter.presentCompatibility(new CheckCompatibilityOutputData("Miso", List.of(
                new RuleResult("A", RuleResult.Status.CONFLICT, "a"),
                new RuleResult("B", RuleResult.Status.CONFLICT, "b"))));

        assertEquals("2 conflicts to think about for Miso.", viewModel.getState().getHeadline());
    }

    @Test
    void singleConflictIsSingular() {
        presenter.presentCompatibility(new CheckCompatibilityOutputData("Miso", List.of(
                new RuleResult("A", RuleResult.Status.CONFLICT, "a"))));

        assertEquals("1 conflict to think about for Miso.", viewModel.getState().getHeadline());
    }

    @Test
    void noConflictsSaysItFits() {
        presenter.presentCompatibility(new CheckCompatibilityOutputData("Miso", List.of(
                new RuleResult("A", RuleResult.Status.MATCH, "a"))));

        assertEquals("No conflicts: Miso fits your home.", viewModel.getState().getHeadline());
    }

    @Test
    void missingPreferencesAsksForTheProfile() {
        presenter.presentNoPreferences();

        assertTrue(viewModel.getState().needsPreferences());
        assertTrue(viewModel.getState().getMessage().contains("My profile"));
        assertTrue(viewModel.getState().getLines().isEmpty());
    }

    @Test
    void notFoundShowsTheMessage() {
        presenter.presentNotFound("This cat could not be found.");

        assertEquals("This cat could not be found.", viewModel.getState().getMessage());
        assertFalse(viewModel.getState().needsPreferences());
    }

    @Test
    void listenersAreToldAfterEachResult() {
        final int[] calls = {0};
        viewModel.addListener(() -> calls[0]++);

        presenter.presentNoPreferences();
        presenter.presentNotFound("x");

        assertEquals(2, calls[0]);
    }
}
