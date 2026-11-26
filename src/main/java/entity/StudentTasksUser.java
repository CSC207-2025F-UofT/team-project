package entity;

import java.util.List;

public class StudentTasksUser extends TasksUser {
    public static final String DEFAULT_COUNTRY_CODE = "CA";

    private String countryCode = DEFAULT_COUNTRY_CODE; // ISO 3166-1 alpha-2, drives holiday warnings

    public StudentTasksUser(String username, String password, int uniqueUserId) {
        super(username, password, uniqueUserId);
    }

    public StudentTasksUser(String username, String password, int uniqueUserId, List<Task> tasks) {
        super(username, password, uniqueUserId, tasks);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        if (countryCode == null || !countryCode.matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("Country code must be ISO 3166-1 alpha-2.");
        }
        this.countryCode = countryCode.toUpperCase();
    }
}