package interface_adapter.submit_application;

import use_case.submit_application.SubmitApplicationInputBoundary;
import use_case.submit_application.SubmitApplicationInputData;

/**
 * The controller for the Submit Application Use Case.
 */
public class SubmitApplicationController {

    private final SubmitApplicationInputBoundary submitApplicationInteractor;

    public SubmitApplicationController(SubmitApplicationInputBoundary submitApplicationInteractor) {
        this.submitApplicationInteractor = submitApplicationInteractor;
    }

    /**
     * Executes the Submit Application Use Case.
     * @param adopterId the adopter who is applying
     * @param petId the pet being applied for
     * @param housingType the adopter's housing type
     * @param petExperience the adopter's experience with pets
     * @param householdMembers who lives in the adopter's home
     * @param contactInfo how the shelter can reach the adopter
     * @param note an optional note
     */
    public void execute(String adopterId, String petId, String housingType, String petExperience,
                        String householdMembers, String contactInfo, String note) {
        submitApplicationInteractor.execute(new SubmitApplicationInputData(adopterId, petId, housingType,
                petExperience, householdMembers, contactInfo, note));
    }
}
