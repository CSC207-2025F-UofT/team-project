package interface_adapter.Loggedin;

import entities.CognitiveData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.get_data.get_dataOutputData;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class get_dataPresenterTest {

    private LoggedinViewModel viewModel;
    private get_dataPresenter presenter;
    private int notifications;

    @BeforeEach
    void setUp() {
        viewModel = new LoggedinViewModel();
        presenter = new get_dataPresenter(viewModel);
        notifications = 0;
        viewModel.addListener(() -> notifications++);
    }

    @Test
    void successStoresEntriesAndMarksDataLoaded() {
        CognitiveData d1 = new CognitiveData.CogBuilder(new Date(0L)).mood("happy").build();
        CognitiveData d2 = new CognitiveData.CogBuilder(new Date(1L)).mood("tired").build();

        presenter.prepareSuccessView(new get_dataOutputData(List.of(d1, d2)));

        assertEquals(List.of(d1, d2), viewModel.getState().getEntries());
        assertTrue(viewModel.getState().isDataLoaded());
        assertEquals(1, notifications);
    }

    @Test
    void successWithNoEntriesStillMarksDataLoaded() {
        presenter.prepareSuccessView(new get_dataOutputData(List.of()));

        assertTrue(viewModel.getState().getEntries().isEmpty());
        assertTrue(viewModel.getState().isDataLoaded());
    }

    @Test
    void successClearsPreviousError() {
        viewModel.getState().setDataError("old error");

        presenter.prepareSuccessView(new get_dataOutputData(List.of()));

        assertEquals("", viewModel.getState().getDataError());
    }

    @Test
    void successKeepsUsernameAndRole() {
        viewModel.getState().setUsername("jonny");
        viewModel.getState().setRole("Regular User");

        presenter.prepareSuccessView(new get_dataOutputData(List.of()));

        assertEquals("jonny", viewModel.getState().getUsername());
        assertEquals("Regular User", viewModel.getState().getRole());
    }

    @Test
    void failStoresErrorMessage() {
        presenter.prepareFailView("HTTP 500: server error");

        assertEquals("HTTP 500: server error", viewModel.getState().getDataError());
        assertEquals(1, notifications);
    }

    @Test
    void failWithNullMessageUsesDefaultMessage() {
        presenter.prepareFailView(null);

        assertEquals("Failed to load data.", viewModel.getState().getDataError());
    }

    @Test
    void failDoesNotMarkDataLoaded() {
        presenter.prepareFailView("boom");

        assertFalse(viewModel.getState().isDataLoaded());
    }

    @Test
    void switchToUserViewGraphViewDoesNothingYet() {
        assertDoesNotThrow(() -> presenter.switchToUserViewGraphView());
        assertEquals(0, notifications);
    }
}
