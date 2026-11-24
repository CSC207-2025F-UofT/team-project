package interface_adapter.interact_with_pet;

import use_case.interact_with_pet.InteractWithPetInputBoundary;
import use_case.interact_with_pet.InteractWithPetInputData;

public class InteractWithPetController {

    private final InteractWithPetInputBoundary interactor;

    public InteractWithPetController(
            InteractWithPetInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        InteractWithPetInputData inputData =
                new InteractWithPetInputData(username);

        interactor.execute(inputData);
    }
}