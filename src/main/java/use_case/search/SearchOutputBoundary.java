package use_case.search;

import java.util.List;

public interface SearchOutputBoundary {
    void execute(SearchOutputData searchOutputData);
    void prepareSuccessView(List<String> results);
}
