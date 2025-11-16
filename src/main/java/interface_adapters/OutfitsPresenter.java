package interface_adapters;

import use_case.SaveOutfitOutputBoundary;
import use_case.SaveOutfitOutputData;

public class SaveOutfitPresenter implements SaveOutfitOutputBoundary {

    private final SaveOutfitViewModel viewModel;

    public SaveOutfitPresenter(SaveOutfitViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveOutfitOutputData outputData) {
        viewModel.setSavedOutfits(outputData.getAllOutfits());
        viewModel.setMessage(outputData.getMessage());
        // In a more advanced setup, you'd notify observers/listeners here.
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}
