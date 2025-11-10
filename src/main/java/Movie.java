public class Movie {
    public String title;
    public String year;
    public String imdbID;
    public String type;
    public String poster;

    public Movie(String title, String year, String imdbID, String type, String poster) {
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }
}
