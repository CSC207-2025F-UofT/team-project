package interface_adapter.rate_and_comment;

public class CommentState {
    private String username = "";
    private String medianame = "";
    private String comment = "";
    private int rate = 0;

    public String getUsername() {return username;}
    public String getMedianame() {return medianame;}
    public String getComment() {return comment;}
    public int getRate() {return rate;}

    public void setUsername(String username) {this.username = username;}
    public void setMedianame(String medianame) {this.medianame = medianame;}
    public void setComment(String comment) {this.comment = comment;}
    public void setRate(int rate) {this.rate = rate;}
}
