package entity;

public class Company {

    private final String ticker;
    private final String name;
    private final String sector;
    private final double marketCap;
    private final double peRatio;

    public Company(String ticker, String name, String sector, double marketCap, double peRatio) {
        this.ticker = ticker;
        this.name = name;
        this.sector = sector;
        this.marketCap = marketCap;
        this.peRatio = peRatio;
    }

    public String getTicker() { return ticker; }
    public String getName() { return name; }
    public String getSector() { return sector; }
    public double getMarketCap() { return marketCap; }
    public double getPeRatio() { return peRatio; }

}
