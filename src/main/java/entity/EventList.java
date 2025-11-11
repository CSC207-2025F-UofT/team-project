package entity;

import java.util.ArrayList;
import java.util.List;

public class EventList {
    private String name; // The name of the list (Allow users to change)
    private final List<Event> events; // List of events inside the list
    /*
    Use Final as we don't want to modify the whole list, only the modify its content
     */
    public EventList(String name) {
        this.name = name;
        this.events = new ArrayList<>();}

    public String getName() {
        return name;
    }
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Please insert list name!");
        }
        this.name = newName;
    }
    public boolean contains(Event event) {
        return events.contains(event); // To be implemented
    }

    // User able to Add an event if it is not already in the list
    public void addEvent(Event event) {
        if (!contains(event)) {
            events.add(event);
        }
    }
    public void removeEvent(Event event) {
        events.remove(event);
    }
    public List<Event> getEvents() {
        return events;
    }
}