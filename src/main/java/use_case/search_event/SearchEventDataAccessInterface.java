package use_case.search_event;

public interface SearchEventDataAccessInterface {
    String search(String keyword,
                  String countryCode, String city,
                  String startDateTime, String endDateTime,
                  String genreIds);
}
