package entity;
import entity.Book;
public class Review{
    public static final int MIN_RATING = 0;
    public static final int MAX_RATING = 5;

    private final String openLibraryID;
    private final int rating;
    private final String review;

    public Review(String openLibraryID, int rating, String review){
        if (rating>MAX_RATING | rating< MIN_RATING){
            throw new IllegalArgumentException("Rating must be between " + MIN_RATING + "- " + MAX_RATING);
        }
        this.openLibraryID = openLibraryID;
        this.rating = rating;
        this.review = review;
    }

    public int getRating(){
        return rating;
    }
    public String getReview(){
        return review;
    }
}
