package interface_adapter.create_document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreationViewModelTest {

    @Test
    void initialStateIsNotNull() {
        CreationViewModel viewModel = new CreationViewModel();

        assertNotNull(viewModel.getState());
    }

    @Test
    void setStateReplacesCurrentState() {
        CreationViewModel viewModel = new CreationViewModel();

        NewDocumentState newState = new NewDocumentState();
        newState.setName("Updated Document");

        viewModel.setState(newState);

        assertSame(newState, viewModel.getState());
        assertEquals("Updated Document",
                viewModel.getState().getName());
    }

    @Test
    void notifyStateChangedNotifiesListener() {
        CreationViewModel viewModel = new CreationViewModel();

        int[] notificationCount = {0};

        viewModel.addListener(() -> notificationCount[0]++);

        viewModel.notifyStateChanged();

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void notifyStateChangedNotifiesAllListeners() {
        CreationViewModel viewModel = new CreationViewModel();

        int[] firstCount = {0};
        int[] secondCount = {0};
        int[] thirdCount = {0};

        viewModel.addListener(() -> firstCount[0]++);
        viewModel.addListener(() -> secondCount[0]++);
        viewModel.addListener(() -> thirdCount[0]++);

        viewModel.notifyStateChanged();

        assertEquals(1, firstCount[0]);
        assertEquals(1, secondCount[0]);
        assertEquals(1, thirdCount[0]);
    }

    @Test
    void notifyStateChangedCanNotifyListenersMultipleTimes() {
        CreationViewModel viewModel = new CreationViewModel();

        int[] notificationCount = {0};

        viewModel.addListener(() -> notificationCount[0]++);

        viewModel.notifyStateChanged();
        viewModel.notifyStateChanged();
        viewModel.notifyStateChanged();

        assertEquals(3, notificationCount[0]);
    }

    @Test
    void viewNameIsCorrect() {
        assertEquals(
                "create new document",
                CreationViewModel.VIEW_NAME
        );
    }

    @Test
    void titleLabelIsCorrect() {
        assertEquals(
                "Create Document View",
                CreationViewModel.TITLE_LABEL
        );
    }
}