package interface_adapter.adopt_pet;

import use_case.adopt_pet.AdoptPetInputBoundary;
import use_case.adopt_pet.AdoptPetInputData;

public class AdoptPetController {

    private final AdoptPetInputBoundary interactor;

    public AdoptPetController(AdoptPetInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(
            String username, String species,
            String petName, String imageUrl) {

        AdoptPetInputData inputData = new AdoptPetInputData(
                username,
                species,
                petName,
                imageUrl
        );

        interactor.execute(inputData);
    }
}