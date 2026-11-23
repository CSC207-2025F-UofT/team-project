package user_case.MovieDetail;

public class MovieDetailOutputData {

    private final int movieID;
    private final String name;
    private final String description;
    private final int rating;
    private final String releaseDate;
    private final String posterPath;

    public MovieDetailOutputData(int movieID,
                                 String name,
                                 String description,
                                 int rating,
                                 String releaseDate,
                                 String posterPath) {
        this.movieID = movieID;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }
}