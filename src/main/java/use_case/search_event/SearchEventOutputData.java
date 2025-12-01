package use_case.search_event;

import entity.Event;

import java.util.List;

public class SearchEventOutputData {
    private final List<Event> events;

    public SearchEventOutputData(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
