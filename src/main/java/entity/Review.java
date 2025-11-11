package entity;


public class Review {


    private final User author;
    private final String comment;
    private final Song song;
    private final int rating;
    private int upvotes;

    /**
     * Creates a review with author,comment, associated song, rating and upvote count.
     * @param author the user writing the review
     * @param comment the comment they left
     * @param rating the number rating
     * @throws IllegalArgumentException if rating is beyond 1-5
     * @param upvotes the amount of upvotes on this review
     */

    public Review(User author, String comment, Song song, int rating, int upvotes) {
        this.author = author;
        this.comment = comment;
        this.song = song;
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("rating must be between 1 and 5");
        this.rating = rating;
        this.upvotes = upvotes;
    }

    public int getRating() {
        return rating;
    }

    public User getAuthor() {
        return author;
    }
    public String getComment() {
        return comment;
    }
    public Song getSong() {
        return song;
    }
    public int getUpvotes() {
        return upvotes;
    }
    public void upvoted() {
        this.upvotes= upvotes + 1;
    }
}
