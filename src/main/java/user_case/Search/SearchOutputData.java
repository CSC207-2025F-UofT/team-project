package user_case.Search;


/**
 * Output Data for the Search Use Case.
 */
public class SearchOutputData {


    private final String name;
    private final String description;
    private final int rating;
    private final String releaseDate;

    public SearchOutputData(String name, int rating, String description, String releaseDate) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }
}

