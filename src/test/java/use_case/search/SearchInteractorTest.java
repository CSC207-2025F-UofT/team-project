package use_case.search;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchInteractorTest {
    @Test
    void forwardsQueryToDaoAndResultToPresenter() {
        String expectedResult = "{\"docs\":[{\"title\":\"Harry Potter\"}]}";
        List<String> receivedQueries = new ArrayList<>();
        List<String> presentedResults = new ArrayList<>();

        SearchDataAccessInterface fakeDao = query -> {
            receivedQueries.add(query);
            return expectedResult;
        };

        SearchOutputBoundary fakePresenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(String result) {
                presentedResults.add(result);
            }
            @Override
            public void prepareFailView(String error) {
                throw new AssertionError("Expected search to succeed: " + error);
            }
        };

        SearchInteractor interactor = new SearchInteractor(fakeDao, fakePresenter);
        interactor.execute(new SearchInputData("Harry Potter"));

        assertEquals(List.of("Harry Potter"), receivedQueries);
        assertEquals(List.of(expectedResult), presentedResults);
    }
}
