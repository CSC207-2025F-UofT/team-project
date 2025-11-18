package entity;

public class Movie extends Media {
    public String overview;
    //public final String title;
    public double rating;
    public final String releaseDate;
    public final int runtime;


    public Movie(String name, int referenceNumber, int[] genreIDs, String overview, double rating, String releaseDate, int runtime ) {
        super(name,referenceNumber, genreIDs);
        this.genreIDs = genreIDs;
        this.overview = "";
        //this.title = "";
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
    }

}
