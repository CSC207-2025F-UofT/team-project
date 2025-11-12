package entity;

public class Comment {
    private String username;
    private int rate;
    private String comment;

    public Comment(String username, int rate, String comment) {
        this.username = username;
        this.rate = rate;
        this.comment = comment;
    }

    public String getUsername() {return username;}

    public int getRate() {return rate;}

    public String getComment() {return comment;}
}
