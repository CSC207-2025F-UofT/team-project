package use_case.post_review;

public class PostInputData {

    private final String comment;
    private final int rating;
    private final String username;
    private final String song;

    public PostInputData(String comment, int rating, String username, String song) {
        this.comment = comment;
        this.rating = rating;
        this.username = username;
        this.song = song;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }

    public String getSong() {
        return song;
    }

}
