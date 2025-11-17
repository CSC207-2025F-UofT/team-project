package use_case.task;

import java.time.LocalDate;

public class AddTaskInputData {
    private final String title;
    private final String description;
    private final LocalDate date;
    private final String type;

    public AddTaskInputData(String title, String description, LocalDate date, String type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public String getType() { return type; }
}
