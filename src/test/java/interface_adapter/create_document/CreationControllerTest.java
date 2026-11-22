package interface_adapter.create_document;

import entity.User;
import use_case.create_document.CreationInputBoundary;
import org.junit.jupiter.api.Test;
import use_case.create_document.CreationInputBoundary;
import use_case.create_document.CreationInputData;

import static org.junit.jupiter.api.Assertions.*;

class CreationControllerTest {

    private static class RecordingCreationInteractor
            implements CreationInputBoundary {

        private CreationInputData receivedData;
        private int callCount = 0;

        @Override
        public void execute(CreationInputData inputData) {
            receivedData = inputData;
            callCount++;
        }

        @Override
        public void switchToNewDocumentView() {}
    }

    @Test
    void executePassesInputDataToInteractor() {
        RecordingCreationInteractor interactor =
                new RecordingCreationInteractor();

        CreationController controller =
                new CreationController(interactor);

        User creator = new User("alice");

        controller.execute("My Document", creator);

        assertEquals(1, interactor.callCount);
        assertNotNull(interactor.receivedData);
    }

    @Test
    void executePassesCorrectNameAndCreator() {
        RecordingCreationInteractor interactor =
                new RecordingCreationInteractor();

        CreationController controller =
                new CreationController(interactor);

        User creator = new User("alice");

        controller.execute("My Document", creator);

        assertEquals("My Document",
                interactor.receivedData.getDocumentName());

        assertEquals(creator,
                interactor.receivedData.getCreator());
    }

    @Test
    void executeCanBeCalledMultipleTimes() {
        RecordingCreationInteractor interactor =
                new RecordingCreationInteractor();

        CreationController controller =
                new CreationController(interactor);

        User creator1 = new User("alice");
        User creator2 = new User("bob");

        controller.execute("Document One", creator1);
        controller.execute("Document Two", creator2);

        assertEquals(2, interactor.callCount);
        assertEquals("Document Two",
                interactor.receivedData.getDocumentName());
        assertEquals(creator2,
                interactor.receivedData.getCreator());
    }
}