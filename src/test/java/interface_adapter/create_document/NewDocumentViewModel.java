package interface_adapter.create_document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewDocumentViewModelTest {

    @Test
    void initialStateIsNotNull() {
        NewDocumentViewModel viewModel = new NewDocumentViewModel();

        assertNotNull(viewModel.getState());
    }

    @Test
    void setStateReplacesCurrentState() {
        NewDocumentViewModel viewModel = new NewDocumentViewModel();

        NewDocumentState newState = new NewDocumentState();
        newState.setName("Updated Document");

        viewModel.setState(newState);

        assertSame(newState, viewModel.getState());
        assertEquals("Updated Document",
                viewModel.getState().getName());
    }

    @Test
    void notifyStateChangedNotifiesListener() {
        NewDocumentViewModel viewModel = new NewDocumentViewModel();

        int[] notificationCount = {0};

        viewModel.addListener(() -> notificationCount[0]++);

        viewModel.notifyStateChanged();

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void notifyStateChangedNotifiesAllListeners() {
        NewDocumentViewModel viewModel = new NewDocumentViewModel();

        int[] firstCount = {0};
        int[] secondCount = {0};

        viewModel.addListener(() -> firstCount[0]++);
        viewModel.addListener(() -> secondCount[0]++);

        viewModel.notifyStateChanged();

        assertEquals(1, firstCount[0]);
        assertEquals(1, secondCount[0]);
    }

    @Test
    void notifyStateChangedCanNotifyListenersMultipleTimes() {
        NewDocumentViewModel viewModel = new NewDocumentViewModel();

        int[] notificationCount = {0};

        viewModel.addListener(() -> notificationCount[0]++);

        viewModel.notifyStateChanged();
        viewModel.notifyStateChanged();

        assertEquals(2, notificationCount[0]);
    }

    @Test
    void viewNameIsCorrect() {
        assertEquals(
                "new document",
                NewDocumentViewModel.VIEW_NAME
        );
    }

    @Test
    void titleLabelIsCorrect() {
        assertEquals(
                "New Document View",
                NewDocumentViewModel.TITLE_LABEL
        );
    }
}