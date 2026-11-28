package interface_adapter.save_preferences;

import use_case.save_preferences.SavePreferencesInputBoundary;
import use_case.save_preferences.SavePreferencesInputData;

/**
 * Passes what the adopter ticked on My profile to the use case.
 */
public class SavePreferencesController {
    private final SavePreferencesInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the use case to run
     */
    public SavePreferencesController(SavePreferencesInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Saves the adopter's answers.
     *
     * @param adopterId the adopter
     * @param hasChildren whether there are children at home
     * @param hasOtherPets whether there are other pets at home
     * @param canHandleMedicalNeeds whether the adopter can care for a cat with medical needs
     * @param canHandleKitten whether the adopter can take on a kitten
     * @param canHandlePregnantAnimal whether the adopter can take on a pregnant cat
     */
    public void save(String adopterId, boolean hasChildren, boolean hasOtherPets, boolean canHandleMedicalNeeds,
                     boolean canHandleKitten, boolean canHandlePregnantAnimal) {
        interactor.execute(new SavePreferencesInputData(adopterId, hasChildren, hasOtherPets,
                canHandleMedicalNeeds, canHandleKitten, canHandlePregnantAnimal));
    }

    /**
     * Fills the page with what the adopter saved before.
     *
     * @param adopterId the adopter
     */
    public void load(String adopterId) {
        interactor.load(adopterId);
    }
}
