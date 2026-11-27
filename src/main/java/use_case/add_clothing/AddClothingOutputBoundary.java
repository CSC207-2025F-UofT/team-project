package use_case.add_clothing;

/**
 * TODO: GINNI
 */
public interface AddClothingOutputBoundary {

    // The item was saved
    void prepareSuccessView(AddClothingOutputData outputData);

    // Something was missing or went wrong because nothing was saved
    void prepareFailView(String errorMessage);

    // Goes back to the welcome screen
    void switchToWelcomeView();
}
