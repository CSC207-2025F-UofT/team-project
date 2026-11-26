package entity;

public class MovieReview {
    private final String author;
    private final String content;
    private final Double rating;

    /**
     * Creates a new movie review
     *
     * @param author the name of the reviewer
     * @param content the review comment
     * @param rating the individual reviewer rating, or null if no rating exists.
     */
    public MovieReview(String author, String content, Double rating){
        this.author = author;
        this.content = content;
        this.rating = rating;
    }
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        return content;
    }
    public Double getRating() {
        return rating;
    }
}

