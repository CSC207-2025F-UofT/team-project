package use_case.search;

import use_case.search.SearchUserDataAccessInterface;
import use_case.search.SearchOutputBoundary;

import java.io.IOException;


/**
 * TODO: Implements search & details use case.
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchUserDataAccessInterface gateway;
    private final SearchOutputBoundary presenter;

    public SearchInteractor(SearchUserDataAccessInterface gateway, SearchOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    public void execute(SearchInputData searchInputData){
        final String movieTitle = searchInputData.getMovieTitle();

        String apiKey = System.getenv("TMDB_API_KEY");
        if (apiKey != null && !apiKey.isBlank()) {
            try {
                data_access.tmdb.TmdbMovieGateway gw = new data_access.tmdb.TmdbMovieGateway(apiKey, null, null);
                java.util.List<entity.Movie> movies = gw.search(movieTitle, null);
                // take up to 5 movies
                if (movies.size() > 5)
                    movies = movies.subList(0, 5);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // TODO: Implement search/details operations
        }
    }
}