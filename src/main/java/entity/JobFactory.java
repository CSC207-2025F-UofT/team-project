package entity;

import java.util.ArrayList;

/**
 * Factory for creating Job objects.
 */
public class JobFactory {

    public Job create(String title, ArrayList<String> skillsNeeded, String company,
                      String date, String description, String locations, String salary, String url) {
        return new Job(title, skillsNeeded, company, date, description, locations, salary, url);
    }
}
