package interface_adapter.get_pet_picture;

import use_case.get_pet_picture.GetPetPictureInputBoundary;
import use_case.get_pet_picture.GetPetPictureInputData;

public class GetPetPictureController {

    private final GetPetPictureInputBoundary interactor;

    public GetPetPictureController(
            GetPetPictureInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String species) {
        GetPetPictureInputData inputData =
                new GetPetPictureInputData(species);

        interactor.execute(inputData);
    }
}