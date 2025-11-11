package entity;

public class Sportbet {
    private String id;
    private String sport;
    private String selection;
    private String status;
    private String title;
    private double odds;
    private double stake;

    public Sportbet(String id, String sport, String selection, String status,
                    String title, double odds, double stake){
        this.id = id;
        this.sport = sport;
        this.selection = selection;
        this.status = status;
        this.title = title;
        this.odds = odds;
        this.stake = stake;
    }
    public String getId() { return id; }
    public String getSport() { return sport; }
    public String getTitle() { return title; }
    public String getSelection() { return selection; }
    public double getOdds() { return odds; }
    public double getStake() { return stake; }
    public String getStatus() { return status; }
}
