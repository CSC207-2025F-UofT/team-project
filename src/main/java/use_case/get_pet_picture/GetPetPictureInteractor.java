package use_case.get_pet_picture;

import java.io.IOException;

public class GetPetPictureInteractor
        implements GetPetPictureInputBoundary {

    private final PetImageDataAccessInterface imageDataAccess;
    private final GetPetPictureOutputBoundary presenter;

    public GetPetPictureInteractor(
            PetImageDataAccessInterface imageDataAccess,
            GetPetPictureOutputBoundary presenter) {
        this.imageDataAccess = imageDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetPetPictureInputData inputData) {
        String species = inputData.getSpecies();

        if (!"DOG".equals(species) && !"CAT".equals(species)) {
            presenter.prepareFailView("Please choose DOG or CAT.");
            return;
        }

        final String imageUrl;

        try {
            imageUrl = imageDataAccess.fetchImageUrl(species);
        } catch (IOException e) {
            presenter.prepareFailView(
                    "Could not load a pet picture. Please try again."
            );
            return;
        }

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            presenter.prepareFailView(
                    "No picture was returned. Please try again."
            );
            return;
        }

        presenter.prepareSuccessView(imageUrl);
    }
}