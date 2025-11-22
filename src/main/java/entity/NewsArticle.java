package entity;

import java.time.ZonedDateTime;
import java.util.List;

public class NewsArticle {

    private final String headline;
    private final String source;
    private final ZonedDateTime publishedAt;
    private final String summary;
    private final List<String> tickersMentioned;

    public NewsArticle(String headline, String source, ZonedDateTime publishedAt, String summary, List<String> tickersMentioned) {
        this.headline = headline;
        this.source = source;
        this.publishedAt = publishedAt;
        this.summary = summary;
        this.tickersMentioned = tickersMentioned;
    }

    public String getHeadline() { return headline; }
    public String getSource() { return source; }
    public ZonedDateTime getPublishedAt() { return publishedAt; }
    public String getSummary() { return summary; }
    public List<String> getTickersMentioned() { return tickersMentioned; }
}