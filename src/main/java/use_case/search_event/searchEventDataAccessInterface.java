package use_case.search_event;

import entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface SearchEventDataAccessInterface {
    List<Event> search(String keyword,
                       String continent,
                       String country,
                       String city,
                       String genre,
                       LocalDate startDate,
                       LocalDate endDate);
}
