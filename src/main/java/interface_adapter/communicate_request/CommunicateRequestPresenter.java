package interface_adapter.communicate_request;

import use_case.communicate_request.CommunicateRequestOutputBoundary;
import use_case.communicate_request.CommunicateRequestOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for UC1 - Build a Request.
 */
public class CommunicateRequestPresenter
        implements CommunicateRequestOutputBoundary {

    private final CommunicateRequestViewModel viewModel;

    public CommunicateRequestPresenter(
            CommunicateRequestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(
            CommunicateRequestOutputData outputData) {

        List<CommunicateRequestState.PictogramItem> items =
                new ArrayList<>();

        for (CommunicateRequestOutputData.PictogramItem item
                : outputData.getAvailablePictograms()) {

            items.add(
                    new CommunicateRequestState.PictogramItem(
                            item.getId(),
                            item.getLabel(),
                            item.getImageLocation()));
        }

        CommunicateRequestState state = viewModel.getState();

        state.setPictograms(items);
        state.setPreviewText(outputData.getPreviewText());
        state.setHasSelection(outputData.hasSelection());
        state.setErrorMessage(null);

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        CommunicateRequestState state = viewModel.getState();

        state.setErrorMessage(errorMessage);

        viewModel.firePropertyChange();
    }
}
