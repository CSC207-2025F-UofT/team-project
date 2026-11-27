package interface_adapter.save_result;

import use_case.save_result.SaveResultOutputBoundary;
import use_case.save_result.SaveResultOutputData;

public class SaveResultPresenter implements SaveResultOutputBoundary {
    private final SaveResultViewModel viewModel;

    public SaveResultPresenter(SaveResultViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // Thank you page
    @Override
    public void prepareSuccessView(SaveResultOutputData outputData) {
        viewModel.setError(null);
        viewModel.setMessage("Thanks, " + outputData.getUserName() + "! Your "
                + outputData.getMatchedLibrary().getDisplayName() + " result was saved.");
        viewModel.firePropertyChanged();
    }

    // Throw warning
    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
        viewModel.firePropertyChanged();
    }
}