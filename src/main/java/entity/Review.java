package entity;
import entity.Book;

import java.net.Inet4Address;

public class Review{
    public static final int MIN_RATING = 0;
    public static final int MAX_RATING = 5;

    private final String openLibraryID;
    private final Integer rating;
    private final String review;

    public Review(String openLibraryID, Integer rating, String review){
        if (rating != null && (rating>MAX_RATING || rating< MIN_RATING)){
            throw new IllegalArgumentException("Rating must be between "
                    + MIN_RATING + " - " + MAX_RATING);
        }
        this.openLibraryID = openLibraryID;
        this.rating = rating;
        this.review = review == null ? "" : review;
    }

    public Integer getRating(){
        return rating;
    }
    public String getReview(){
        return review;
    }
}
