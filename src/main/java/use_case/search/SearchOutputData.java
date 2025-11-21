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
        private final int id;
        private final String name;
        private final String artist;

        public SongResult(int id, String name, String artist) {
            this.id = id;
            this.name = name;
            this.artist = artist;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getArtist() { return artist; }
    }
}
