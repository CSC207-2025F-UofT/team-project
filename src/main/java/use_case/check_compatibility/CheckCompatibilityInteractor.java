package use_case.check_compatibility;

import java.util.Optional;

import entity.AdopterPreferences;
import entity.Pet;

/**
 * Checks a cat against the adopter's saved preferences and hands every rule's outcome to the presenter.
 */
public class CheckCompatibilityInteractor implements CheckCompatibilityInputBoundary {
    private final CheckCompatibilityPetDataAccessInterface petData;
    private final AdopterPreferencesDataAccessInterface preferencesData;
    private final CompatibilityEngine engine;
    private final CheckCompatibilityOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param petData where the cat is found
     * @param preferencesData where the adopter's preferences are kept
     * @param engine runs the compatibility rules
     * @param presenter where the result goes
     */
    public CheckCompatibilityInteractor(CheckCompatibilityPetDataAccessInterface petData,
                                        AdopterPreferencesDataAccessInterface preferencesData,
                                        CompatibilityEngine engine,
                                        CheckCompatibilityOutputBoundary presenter) {
        this.petData = petData;
        this.preferencesData = preferencesData;
        this.engine = engine;
        this.presenter = presenter;
    }

    @Override
    public void execute(CheckCompatibilityInputData inputData) {
        final Optional<Pet> pet = petData.findById(inputData.getPetId());
        if (pet.isEmpty()) {
            presenter.presentNotFound("This cat could not be found.");
            return;
        }
        final String adopterId = inputData.getAdopterId();
        final Optional<AdopterPreferences> preferences = adopterId == null || adopterId.isBlank()
                ? Optional.empty() : preferencesData.findByAdopterId(adopterId);
        if (preferences.isEmpty()) {
            presenter.presentNoPreferences();
            return;
        }
        presenter.presentCompatibility(new CheckCompatibilityOutputData(pet.get().getName(),
                engine.evaluate(pet.get(), preferences.get())));
    }
}
