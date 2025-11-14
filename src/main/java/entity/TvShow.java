package entity;

public class TvShow extends Media{
    public final Episode[][] episodes;
    public final int seasons;

    public TvShow(int referenceNumber, int[] genreIDs, Episode[][] episodes, int seasons, Episode[][] episodes1, int seasonNumber) {
        super(referenceNumber, genreIDs);
        this.episodes = episodes1;
        this.seasons = seasonNumber;
    }
}
