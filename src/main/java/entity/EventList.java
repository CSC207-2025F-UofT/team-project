package entity;

import java.util.ArrayList;
import java.util.List;

public class EventList {
    // Fields
    private final String id;
    private final String name;
    private final List<Event> events;
    /*
    Use Final as we don't want to modify the whole list, only the modify its content
     */

    public EventList(String id, String name) {
        this.id = id;
        this.name = name;
        this.events = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    // Mutators
    public void addEvent(Event event) {
        if (!events.contains(event)){
            events.add(event);
        }

    }
    public void removeEvent(Event event) {
        events.remove(event);
    }
}
