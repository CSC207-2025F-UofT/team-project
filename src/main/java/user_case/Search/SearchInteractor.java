package user_case.Search;


import entity.Movie;

import java.util.List;


/**
 * The Search Interactor.
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchMovieDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary searchPresenter;
//    private final UserFactory userFactory;

    public SearchInteractor(SearchMovieDataAccessInterface SearchDataAccessInterface,
                            SearchOutputBoundary searchOutputBoundary) {
        this.searchDataAccessObject = SearchDataAccessInterface;
        this.searchPresenter = searchOutputBoundary;

    }

    @Override
    public void execute(SearchInputData searchInputData) {
        if ("".equals(searchInputData.getTitle()) || searchInputData.getTitle().isBlank()) {
            searchPresenter.prepareFailView("Please enter a valid movie title");
        }
        else {
            final List<Movie> movies = searchDataAccessObject.getMovie(searchInputData.getTitle());


            final SearchOutputData searchOutputData = new SearchOutputData(movies);
            //results returned in the form of {name, release date, poster path, movie id}
            searchPresenter.prepareSuccessView(searchOutputData);
        }
    }
}
