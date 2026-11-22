package entity;

/**
 * A simple entity representing a movie. Users have a name, rating and description.
 */
public class Movie {

    private final String name;
    private final String description;
    private final int rating;
    private final String releaseDate;
    private final int movieID;
    private final String posterPath;

    /**
     * Creates a new movie with the given non-empty name and non-empty rating.
     * @param name the username
     * @param rating the rating
     * @param description the description
//     * @throws IllegalArgumentException if the password or name are empty
     */
    public Movie(String name, int rating, String description, String releaseDate, int movieID, String posterPath) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.name = name;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
        this.movieID = movieID;
        this.posterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getPosterPath() {return posterPath;}

}
