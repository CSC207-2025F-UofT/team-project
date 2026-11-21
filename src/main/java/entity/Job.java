package entity;

import java.util.ArrayList;

/**
 * A simple entity representing a job. Jobs have a username and password..
 */
public class Job {

    private static int currentID = 1;
    private int id;
    private String title;
    private ArrayList<String> SkillsNeeded;
    private String company;
    private String date;
    private String description;
    private String locations;
    private String salary;
    private String url;


    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param title the title
     * @param SkillsNeeded the skills needed
     * @param company the company
     * @param date the date
     * @param description the description
     * @param locations the locations
     * @param salary the salary
     * @param url the url
     */
    public Job(String title, ArrayList<String> SkillsNeeded, String company,
               String date, String description, String locations, String salary, String url) {
        id = currentID;
        currentID++;
        this.title = title;
        this.SkillsNeeded = SkillsNeeded;
        this.company = company;
        this.date = date;
        this.description = description;
        this.locations = locations;
        this.salary = salary;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getSkillsNeeded() {
        return SkillsNeeded;
    }

    public String getCompany() {
        return company;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLocations() {
        return locations;
    }

    public String getSalary() {
        return salary;
    }

    public String getUrl() {
        return url;
    }

}
