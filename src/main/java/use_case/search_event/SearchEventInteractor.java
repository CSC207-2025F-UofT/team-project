package use_case.search_event;

import entity.Event;

import java.util.List;

public class SearchEventInteractor implements SearchEventInputBoundary{
    private final SearchEventDataAccessInterface dataAccess;
    private final SearchEventOutputBoundary presenter;

    public SearchEventInteractor(SearchEventDataAccessInterface dataAccess,
                                 SearchEventOutputBoundary presenter){
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchEventInputData inputData) {
        List<Event> events = dataAccess.search(
                inputData.getKeyword(),
                inputData.getContinent(),
                inputData.getCountry(),
                inputData.getCity(),
                inputData.getGenre(),
                inputData.getStartDate(),
                inputData.getEndDate()
        );
        SearchEventOutputData outputData = new SearchEventOutputData(events);
        presenter.present(outputData);
    }
}
