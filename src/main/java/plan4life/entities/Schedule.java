package plan4life.entities;

import java.util.*;

public class Schedule {
    private final Map<String, String> activities = new LinkedHashMap<>();
    // e.g., "Monday 9 AM" -> "Workout"

    public void addActivity(String timeSlot, String activity) {
        activities.put(timeSlot, activity);
    }

    public Map<String, String> getActivities() {
        return Collections.unmodifiableMap(activities);
    }

    public void populateRandomly() {
        activities.clear();
        String[] sampleActivities = {"Work", "Gym", "Lunch", "Relax", "Sleep"};
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        for (String day : days) {
            for (int i = 9; i <= 17; i += 2) {
                String time = day + " " + i + ":00";
                activities.put(time, sampleActivities[new Random().nextInt(sampleActivities.length)]);
            }
        }
    }
}

