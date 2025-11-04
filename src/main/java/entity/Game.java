package entity;

public class Game {
    private final long appid;
    private final String title;

    public Game(long appid, String title) {
        this.appid = appid;
        this.title = title;
    }

    public long getId() {
        return this.appid;
    }

    public String getTitle() {
        return this.title;
    }
}
