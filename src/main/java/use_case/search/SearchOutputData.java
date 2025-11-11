package use_case.search;
import java.util.List;

public class SearchOutputData {
    private final List<SongResult> results;

    public SearchOutputData(List<SongResult> results) {
        this.results = results;
    }

    public List<SongResult> getResults() {
        return results;
    }

    public static class SongResult {
        private final String id;
        private final String title;
        private final String artist;
        private final String album;

        public SongResult(String id, String title, String artist, String album) {
            this.id = id;
            this.title = title;
            this.artist = artist;
            this.album = album;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getArtist() { return artist; }
        public String getAlbum() { return album; }
    }
}
