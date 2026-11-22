package interface_adapter.create_document;

import entity.Document;
import entity.User;
import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.create_document.CreationOutputData;

import static org.junit.jupiter.api.Assertions.*;

class CreationPresenterTest {

    private ViewManagerModel viewManagerModel;
    private CreationViewModel creationViewModel;
    private NewDocumentViewModel newDocumentViewModel;
    private CreationPresenter presenter;

    @BeforeEach
    void setUp() {
        viewManagerModel = new ViewManagerModel();
        creationViewModel = new CreationViewModel();
        newDocumentViewModel = new NewDocumentViewModel();

        presenter = new CreationPresenter(
                viewManagerModel,
                creationViewModel,
                newDocumentViewModel
        );
    }

    @Test
    void prepareFailViewSetsErrorMessage() {
        String error = "Document name already exists";

        presenter.prepareFailView(error);

        assertEquals(error,
                creationViewModel.getState().getNameError());
    }

    @Test
    void prepareFailViewDoesNotChangeCurrentView() {
        viewManagerModel.setState("previous view");

        presenter.prepareFailView("Invalid name");

        assertEquals("previous view",
                viewManagerModel.getCurrentViewName());
    }

    @Test
    void prepareFailViewNotifiesCreationViewModelListeners() {
        int[] notificationCount = {0};

        creationViewModel.addListener(
                () -> notificationCount[0]++
        );

        presenter.prepareFailView("Invalid name");

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void prepareFailViewDoesNotNotifyNewDocumentViewModel() {
        int[] notificationCount = {0};

        newDocumentViewModel.addListener(
                () -> notificationCount[0]++
        );

        presenter.prepareFailView("Invalid name");

        assertEquals(0, notificationCount[0]);
    }

    @Test
    void switchToNewDocumentViewChangesViewName() {
        presenter.switchToNewDocumentView();

        assertEquals(
                CreationViewModel.VIEW_NAME,
                viewManagerModel.getCurrentViewName()
        );
    }

    @Test
    void switchToNewDocumentViewNotifiesViewManagerListeners() {
        int[] notificationCount = {0};

        viewManagerModel.addListener(
                () -> notificationCount[0]++
        );

        presenter.switchToNewDocumentView();

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void switchToNewDocumentViewDoesNotChangeViewModelStates() {
        creationViewModel.getState().setName("Creation name");
        newDocumentViewModel.getState().setName("New document name");

        presenter.switchToNewDocumentView();

        assertEquals("Creation name",
                creationViewModel.getState().getName());

        assertEquals("New document name",
                newDocumentViewModel.getState().getName());
    }

    // Success-path tests

    @Test
    void prepareSuccessViewSetsDocumentName() {
        User creator = new User("alice");
        Document document = new Document("My Document", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(
                "My Document",
                newDocumentViewModel.getState().getName()
        );
    }

    @Test
    void prepareSuccessViewSwitchesToNewDocumentView() {
        User creator = new User("alice");
        Document document = new Document("My Document", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(
                NewDocumentViewModel.VIEW_NAME,
                viewManagerModel.getCurrentViewName()
        );
    }

    @Test
    void prepareSuccessViewNotifiesNewDocumentViewModel() {
        int[] notificationCount = {0};

        newDocumentViewModel.addListener(
                () -> notificationCount[0]++
        );

        User creator = new User("alice");
        Document document = new Document("My Document", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void prepareSuccessViewNotifiesViewManager() {
        int[] notificationCount = {0};

        viewManagerModel.addListener(
                () -> notificationCount[0]++
        );

        User creator = new User("alice");
        Document document = new Document("My Document", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(1, notificationCount[0]);
    }

    @Test
    void prepareSuccessViewDoesNotNotifyCreationViewModel() {
        int[] notificationCount = {0};

        creationViewModel.addListener(
                () -> notificationCount[0]++
        );

        User creator = new User("alice");
        Document document = new Document("My Document", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(0, notificationCount[0]);
    }

    @Test
    void prepareSuccessViewUsesDocumentNameRatherThanPreviousState() {
        newDocumentViewModel.getState().setName("Old document name");

        User creator = new User("alice");
        Document document = new Document("New document name", creator);
        CreationOutputData outputData = new CreationOutputData(document);

        presenter.prepareSuccessView(outputData);

        assertEquals(
                "New document name",
                newDocumentViewModel.getState().getName()
        );
    }
}