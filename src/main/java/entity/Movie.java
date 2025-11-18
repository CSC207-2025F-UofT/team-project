package entity;

public class Movie extends Media {
    public String overview;
    public final String title;
    public double rating;
    public final String releaseDate;
    public final int runtime;


    public Movie(int referenceNumber, int[] genreIDs, String overview, String title, double rating, String releaseDate, int runtime ) {
        super(title,referenceNumber, genreIDs);
        this.genreIDs = genreIDs;
        this.overview = "";
        this.title = "";
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.runtime = runtime;

    }

}
