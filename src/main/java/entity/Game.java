package entity;
import java.util.List;


public class Game {
    private final long appid;
    private final String name;
    private final int playtime_forever;
    private final String thumbnail;
    private final List<Integer> content_descriptorids;

    public Game(long appid, String title, int playtime_forever, String thumbnail, List<Integer> content_descriptorids) {
        this.appid = appid;
        this.name = title;
        this.playtime_forever = playtime_forever;
        this.thumbnail = thumbnail;
        this.content_descriptorids = content_descriptorids;
    }

    public long getId() {
        return this.appid;
    }

    public String getTitle() {
        return this.name;
    }

    public int getPlaytime() {
        return this.playtime_forever;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void getImage(){
        // TODO: implement, return Image
    }

    public void getDescriptors(){
        // TODO: implement, return List of strings containing categories ; List<String>
    }

    @Override
    public String toString() {
        return "Game{" +
                "appid=" + appid +
                ", name='" + name + '\'' +
                ", playtime_forever=" + playtime_forever +
                ", thumbnail='" + thumbnail + '\'' +
                ", content_descriptorids=" + content_descriptorids +
                '}';
    }
}
