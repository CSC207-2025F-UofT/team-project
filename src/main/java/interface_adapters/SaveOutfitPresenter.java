package interface_adapters;

import use_case.SaveOutfitOutputBoundary;
import use_case.SaveOutfitOutputData;

/**
 * Presenter converts raw Output Data → user-friendly ViewModel.
 */
public class SaveOutfitPresenter implements SaveOutfitOutputBoundary {

    private final SaveOutfitViewModel viewModel;

    public SaveOutfitPresenter(SaveOutfitViewModel vm) {
        this.viewModel = vm;
    }

    @Override
    public void prepareSuccessView(SaveOutfitOutputData data) {
        viewModel.setOutfits(data.getOutfits());
        viewModel.setMessage(data.getMessage());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
    }
}

