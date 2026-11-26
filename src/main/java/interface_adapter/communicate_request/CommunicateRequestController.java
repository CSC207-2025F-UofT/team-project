package interface_adapter.communicate_request;

import use_case.communicate_request.CommunicateRequestInputBoundary;

/**
 * Controller for UC1 - Build a Request.
 */
public class CommunicateRequestController {

    private final CommunicateRequestInputBoundary interactor;

    public CommunicateRequestController(
            CommunicateRequestInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadBoard() {
        interactor.loadBoard();
    }

    public void selectPictogram(int pictogramId) {
        interactor.selectPictogram(pictogramId);
    }

    public void removeLastSelection() {
        interactor.removeLastSelection();
    }

    public void clearDraft() {
        interactor.clearDraft();
    }
}