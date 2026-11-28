package interface_adapter.save_preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;

class SavePreferencesAdapterTest {
    private final PreferencesViewModel viewModel = new PreferencesViewModel();
    private final SavePreferencesPresenter presenter = new SavePreferencesPresenter(viewModel);

    @Test
    void savedFillsTheAnswersAndSaysSo() {
        presenter.presentSaved(new AdopterPreferences(true, false, true, false, true, null));

        final PreferencesState state = viewModel.getState();
        assertTrue(state.hasChildren());
        assertFalse(state.hasOtherPets());
        assertTrue(state.canHandleMedicalNeeds());
        assertFalse(state.canHandleKitten());
        assertTrue(state.canHandlePregnantAnimal());
        assertEquals("Your profile is saved.", state.getMessage());
    }

    @Test
    void loadedNothingGivesAnEmptyForm() {
        presenter.presentLoaded(null);

        assertFalse(viewModel.getState().hasChildren());
        assertEquals("", viewModel.getState().getMessage());
    }

    @Test
    void errorIsShown() {
        presenter.presentError("Log in to save your profile.");

        assertEquals("Log in to save your profile.", viewModel.getState().getError());
    }

    @Test
    void listenersAreTold() {
        final int[] calls = {0};
        viewModel.addListener(() -> calls[0]++);

        presenter.presentLoaded(null);

        assertEquals(1, calls[0]);
    }

    @Test
    void controllerPassesAnswersAndLoadsToTheUseCase() {
        final List<String> seen = new ArrayList<>();
        final SavePreferencesController controller = new SavePreferencesController(
                new use_case.save_preferences.SavePreferencesInputBoundary() {
                    @Override
                    public void execute(use_case.save_preferences.SavePreferencesInputData input) {
                        seen.add("save:" + input.getAdopterId() + input.hasChildren() + input.canHandleKitten());
                    }

                    @Override
                    public void load(String adopterId) {
                        seen.add("load:" + adopterId);
                    }
                });

        controller.save("a1", true, false, false, true, false);
        controller.load("a1");

        assertEquals(List.of("save:a1truetrue", "load:a1"), seen);
    }
}
