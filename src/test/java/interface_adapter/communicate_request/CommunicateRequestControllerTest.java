package interface_adapter.communicate_request;

import org.junit.jupiter.api.Test;
import use_case.communicate_request.CommunicateRequestInputBoundary;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommunicateRequestControllerTest {

    @Test
    void controllerForwardsActionsToInteractor() {
        RecordingInteractor interactor =
                new RecordingInteractor();

        CommunicateRequestController controller =
                new CommunicateRequestController(interactor);

        controller.loadBoard();
        controller.selectPictogram(7);
        controller.removeLastSelection();
        controller.clearDraft();

        assertEquals(1, interactor.loadCount);
        assertEquals(7, interactor.selectedId);
        assertEquals(1, interactor.removeCount);
        assertEquals(1, interactor.clearCount);
    }

    private static class RecordingInteractor
            implements CommunicateRequestInputBoundary {

        private int loadCount;
        private int selectedId = -1;
        private int removeCount;
        private int clearCount;

        @Override
        public void loadBoard() {
            loadCount++;
        }

        @Override
        public void selectPictogram(int pictogramId) {
            selectedId = pictogramId;
        }

        @Override
        public void removeLastSelection() {
            removeCount++;
        }

        @Override
        public void clearDraft() {
            clearCount++;
        }
    }
}
