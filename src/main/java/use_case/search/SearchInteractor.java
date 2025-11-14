package use_case.search;

import entity.Song;

import java.util.List;

public class SearchInteractor  implements SearchInputDataBoundary {
    private final SearchUserDataAccessInterface dataAccess;
    private final SearchOutputDataBoundary presenter;

    public SearchInteractor(SearchUserDataAccessInterface dataAccess,
                            SearchOutputDataBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchInputData inputData) {
        try {
            List<Song> songs = dataAccess.search(inputData.getQuery());

            List<SearchOutputData.SongResult> results = songs.stream()
                    .map(song -> new SearchOutputData.SongResult(
                            song.getId(),
                            song.getName(),
                            song.getArtist()
                    ))
                    .toList();

            presenter.prepareSuccessView(new SearchOutputData(results));

        } catch (Exception e) {
            presenter.prepareFailView("Search failed: " + e.getMessage());
        }
    }

}
