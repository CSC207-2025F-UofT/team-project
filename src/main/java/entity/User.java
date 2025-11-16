package entity;

import java.util.List;

public class User {
    private final String username;
    private final String firstName;
    private final String lastName;
    private String password;
    private final List<EventList> eventLists;
    private final EventList masterList = new EventList("master_list", "Master List");

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

    public EventList getMasterList() {
        return masterList;
    }

    public String getPassword() {
        return password;
    }

    // Mutators

    public void setPassword(String password) {
        this.password = password;
    }





}
