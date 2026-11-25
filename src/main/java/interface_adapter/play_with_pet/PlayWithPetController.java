package interface_adapter.play_with_pet;

import use_case.play_with_pet.PlayWithPetInputBoundary;
import use_case.play_with_pet.PlayWithPetInputData;

public class PlayWithPetController {

    private final PlayWithPetInputBoundary interactor;

    public PlayWithPetController(
            PlayWithPetInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        PlayWithPetInputData inputData =
                new PlayWithPetInputData(username);

        interactor.execute(inputData);
    }
}