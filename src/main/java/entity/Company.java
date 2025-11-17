package entity;

import java.util.List;

public class Company {
    private final String symbol;
    private final String name;
    private final String description;
    private final String sector;
    private final String industry;
    private final String country;

    private long marketCapitalization;
    private float EPS;
    private float peRatio;
    private float dividendPerShare;
    private float dividendYield;
    private float beta;

    private final List<FinancialStatement> financialStatements;
    private final List<NewsArticle> newsArticles;

    public Company(String symbol,
                   String name,
                   String description,
                   String sector,
                   String industry,
                   String country,
                   long marketCapitalization,
                   float EPS, float peRatio,
                   float dividendPerShare,
                   float dividendYield,
                   float beta,
                   List<FinancialStatement> financialStatements,
                   List<NewsArticle> newsArticles) {
        this.symbol = symbol;
        this.name = name;
        this.description = description;
        this.sector = sector;
        this.industry = industry;
        this.country = country;
        this.marketCapitalization = marketCapitalization;
        this.EPS = EPS;
        this.peRatio = peRatio;
        this.dividendPerShare = dividendPerShare;
        this.dividendYield = dividendYield;
        this.beta = beta;
        this.financialStatements = financialStatements;
        this.newsArticles = newsArticles;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSector() { return sector; }
    public String getIndustry() { return industry; }
    public String getCountry() { return country; }
    public float getMarketCapitalization() { return marketCapitalization; }
    public float getEPS() { return EPS; }
    public float getPeRatio() { return peRatio; }
    public float getDividendPerShare() { return dividendPerShare; }
    public float getDividendYield() { return dividendYield; }
    public float getBeta() { return beta; }
    public List<FinancialStatement> getFinancialStatements() { return financialStatements; }
    public List<NewsArticle> getNewsArticles() { return newsArticles; }

    public void setMarketCapitalization(long value) { this.marketCapitalization = value; }
    public void setEps(float value) { this.EPS = value; }
    public void setPeRatio(float value) { this.peRatio = value; }
    public void setDividendPerShare(float value) { this.dividendPerShare = value; }
    public void setDividendYield(float value) { this.dividendYield = value; }
    public void setBeta(float value) { this.beta = value; }
}
