package use_case.clicking;

public class ClickingOutputData {
    public String title;
    public int releaseYear;
    public String language;
    public double rating;
    public String genres;
    public String overview;
    public String posterUrl;
    public String errorMessage;


    public ClickingOutputData(String title, int releaseYear, String language, double rating, String genres,
                              String overview, String posterUrl) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.language = language;
        this.rating = rating;
        this.genres = genres;
        this.overview = overview;
        this.posterUrl = posterUrl;
    }
}
