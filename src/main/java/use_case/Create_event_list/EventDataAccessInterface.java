package use_case.Create_event_list;
import entity.Event;

import java.util.List;

public interface EventDataAccessInterface {

    /**
     * Returns the events associated with the given parameters
     */
    List<Event> getEvents(String keyword, String artist, String country, String city, String startDateTime, String endDateTime, String genre);
}
