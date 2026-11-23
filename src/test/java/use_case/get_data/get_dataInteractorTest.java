package use_case.get_data;

import entities.CognitiveData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class get_dataInteractorTest {

    /** In-memory fake DAO so the test never touches Supabase. */
    private static class FakeDao implements get_dataDataAccessInterface {
        List<CognitiveData> toReturn = new ArrayList<>();
        RuntimeException toThrow = null;
        String lastUsername = null;
        boolean fetchAllCalled = false;

        @Override
        public List<CognitiveData> fetchAll() {
            fetchAllCalled = true;
            return toReturn;
        }

        @Override
        public List<CognitiveData> fetchByUsername(String username) {
            lastUsername = username;
            if (toThrow != null) {
                throw toThrow;
            }
            return toReturn;
        }
    }

    /** Records which presenter method the interactor called. */
    private static class SpyPresenter implements get_dataOutputBoundary {
        get_dataOutputData success = null;
        String error = null;
        int calls = 0;

        @Override
        public void prepareSuccessView(get_dataOutputData outputData) {
            success = outputData;
            calls++;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
            calls++;
        }
    }

    private FakeDao dao;
    private SpyPresenter presenter;
    private get_dataInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new FakeDao();
        presenter = new SpyPresenter();
        interactor = new get_dataInteractor(dao, presenter);
    }

    @Test
    void successReturnsUsersEntries() {
        CognitiveData d1 = new CognitiveData.CogBuilder(new Date(0L))
                .mood("happy").cogTest("Attention", 8.0).productivityRating(7.0).build();
        CognitiveData d2 = new CognitiveData.CogBuilder(new Date(86_400_000L))
                .mood("tired").cogTest("Attention", 5.0).productivityRating(4.0).build();
        dao.toReturn = List.of(d1, d2);

        interactor.execute(new get_dataInputData("jonny"));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals(List.of(d1, d2), presenter.success.getEntries());
        assertEquals(1, presenter.calls);
    }

    @Test
    void usesTheInputUsernameAndNotFetchAll() {
        interactor.execute(new get_dataInputData("jonny"));

        assertEquals("jonny", dao.lastUsername);
        assertFalse(dao.fetchAllCalled);
    }

    @Test
    void userWithNoDataStillSucceedsWithEmptyList() {
        dao.toReturn = new ArrayList<>();

        interactor.execute(new get_dataInputData("newuser"));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertTrue(presenter.success.getEntries().isEmpty());
    }

    @Test
    void daoFailureGoesToFailView() {
        dao.toThrow = new RuntimeException("HTTP 500: server error");

        interactor.execute(new get_dataInputData("jonny"));

        assertNull(presenter.success);
        assertEquals("HTTP 500: server error", presenter.error);
        assertEquals(1, presenter.calls);
    }
}