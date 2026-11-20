package entity;

import java.time.Duration;
import java.util.*;
/** Anything the user tracks time for (ex: sleep, note-taking, etc)*/
public class Activity {
    private String name;
    private Duration duration;
    private List<String> participants;

    public Activity(String name, Duration duration, List<String> participants) {
        this.name = name;
        this.duration = duration;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public List<String> getParticipants() {
        return participants;
    }
}

// getters 
public String getName() { return name; }
public ActivityCategory getCategory() { return category; }

public List<TimeEntry> getTimeEntries() {
    retur
}