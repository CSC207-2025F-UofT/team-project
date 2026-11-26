package use_case.add_clothing;

/**
 * save a new item, or leave the screen
 */
public interface AddClothingInputBoundary {
    //saves the picture and the item
    void execute(AddClothingInputData inputData);

    //Leaves the form without saving
    void switchToWelcomeView();
}
