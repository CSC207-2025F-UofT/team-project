package use_case.get_pet_picture;

public interface GetPetPictureOutputBoundary {

    void prepareSuccessView(String imageUrl);

    void prepareFailView(String errorMessage);
}