package entity;

public class TvShow extends Media{
    public final Episode[][] episodes;
    public final int seasons;

    public TvShow(int referenceNumber, int[] genreIDs, Episode[][] episodesArray, int seasonNumber,String title) {
        super(title,referenceNumber, genreIDs);
        this.episodes = episodesArray;
        this.seasons = seasonNumber;
    }
}
