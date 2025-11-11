package entity;

import java.util.List;

public class User {
    private final String username;
    private final String firstName;
    private final String lastName;
    private String password;
    private final List<EventList> eventLists;

    public User(String username, String firstName, String lastName, String password, List<EventList> eventLists) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.eventLists = eventLists;
    }

    // Getters

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public List<EventList> getEventLists() {
        return eventLists;
    }


}
