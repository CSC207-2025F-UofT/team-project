package use_case.search_event;

import java.time.LocalDate;

public class SearchEventInputData {
    private final String keyword;
    private final String continent;
    private final String country;
    private final String city;
    private final String genre;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public SearchEventInputData(String keyword, String continent, String country,
                                String city, String genre, LocalDate startDate, LocalDate endDate){
        this.keyword = keyword;
        this.continent = continent;
        this.country = country;
        this.city = city;
        this.genre = genre;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getContinent() {
        return continent;
    }

    public String getCountry() {
        return country;
    }

    public String getGenre() {
        return genre;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
