package interface_adapter.get_pet_picture;

import interface_adapter.adopt_pet.AdoptPetState;
import interface_adapter.adopt_pet.AdoptPetViewModel;
import use_case.get_pet_picture.GetPetPictureOutputBoundary;

public class GetPetPicturePresenter
        implements GetPetPictureOutputBoundary {

    private final AdoptPetViewModel adoptPetViewModel;

    public GetPetPicturePresenter(
            AdoptPetViewModel adoptPetViewModel) {
        this.adoptPetViewModel = adoptPetViewModel;
    }

    @Override
    public void prepareSuccessView(String imageUrl) {
        AdoptPetState state = adoptPetViewModel.getState();

        state.setImageUrl(imageUrl);
        state.setErrorMessage("");
        state.setLoading(false);

        adoptPetViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AdoptPetState state = adoptPetViewModel.getState();

        state.setImageUrl("");
        state.setErrorMessage(errorMessage);
        state.setLoading(false);

        adoptPetViewModel.notifyStateChanged();
    }
}