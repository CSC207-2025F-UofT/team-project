package user_case.Search;


import entity.Movie;


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
        if ("".equals(searchInputData.getTitle())) {
            searchPresenter.prepareFailView("Please enter a valid movie title");
        }
        else {
            final Movie movie = searchDataAccessObject.getMovie(searchInputData.getTitle());

            final SearchOutputData searchOutputData = new SearchOutputData(
                    movie.getName(),
                    movie.getRating(),
                    movie.getDescription(),
                    movie.getReleaseDate());
            searchPresenter.prepareSuccessView(searchOutputData);
        }
    }
}
