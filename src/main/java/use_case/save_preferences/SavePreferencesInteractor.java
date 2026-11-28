package use_case.save_preferences;

import entity.AdopterPreferences;
import use_case.check_compatibility.AdopterPreferencesDataAccessInterface;

/**
 * Saves the answers from My profile so every compatibility check can use them.
 */
public class SavePreferencesInteractor implements SavePreferencesInputBoundary {
    private final AdopterPreferencesDataAccessInterface dataAccess;
    private final SavePreferencesOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where preferences are kept
     * @param presenter where the result goes
     */
    public SavePreferencesInteractor(AdopterPreferencesDataAccessInterface dataAccess,
                                     SavePreferencesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SavePreferencesInputData inputData) {
        if (isBlank(inputData.getAdopterId())) {
            presenter.presentError("Log in to save your profile.");
            return;
        }
        final AdopterPreferences preferences = new AdopterPreferences(inputData.hasChildren(),
                inputData.hasOtherPets(), inputData.canHandleMedicalNeeds(), inputData.canHandleKitten(),
                inputData.canHandlePregnantAnimal(), null);
        dataAccess.save(inputData.getAdopterId(), preferences);
        presenter.presentSaved(preferences);
    }

    @Override
    public void load(String adopterId) {
        if (isBlank(adopterId)) {
            presenter.presentError("Log in to save your profile.");
            return;
        }
        presenter.presentLoaded(dataAccess.findByAdopterId(adopterId).orElse(null));
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }
}
