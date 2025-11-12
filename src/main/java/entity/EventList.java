package entity;

import java.util.ArrayList;
import java.util.List;

public class EventList {
    // Fields
    private final String id; // Unique ID for this list
    private String name; // The name of the list (Allow users to change)
    private String category; // e.g "Upcoming", "Past", "Today"?
    private final List<Event> events; // List of events inside the list
    /*
    Use Final as we don't want to modify the whole list, only the modify its content
     */

    public EventList(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.events = new ArrayList<>(); // Add events in use_case
    }

    // getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public List<Event> getEvents() {
        return events;
    }

    // Methods
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Please insert list name!");
        }
        this.name = newName;
    }

    public void changeCategory(String newCategory) {
        if (newCategory == null || newCategory.isBlank()) {
            throw new IllegalArgumentException("Please insert list category!");
        }
        this.category = newCategory;
    }

    // Check whether a list contains the given event already
    public boolean contains(Event event) {
        return events.contains(event);
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
}