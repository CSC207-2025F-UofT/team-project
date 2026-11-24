package entities;

import java.util.Optional;

public class Event {
    private String id;
    private String name;
    private String date;  // TODO: Figure out date format
    private boolean completed;
    private String courseID;  // null = personal
    private GradeInfo grade;  // null = not graded

    public Event(String id, String name, String date, boolean completed,
                 String courseID, GradeInfo grade) {
        // Personal events can't be graded
        if (courseID == null && grade != null) {
            throw new IllegalArgumentException("Personal events can't be graded");
        }

        this.id = id;
        this.name = name;
        this.date = date;
        this.completed = completed;
        this.courseID = courseID;
        this.grade = grade;
    }

    // Getters
    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getDate() { return this.date; }
    public boolean isCompleted() { return this.completed; }
    // Personal is a "Course" ?
    public String getCourseID() {
        if (this.courseID == null || this.courseID.isBlank()) {
            return "ID-Personal";
        }
        return this.courseID;
    }
    public boolean isGraded() { return this.grade != null; }
    // In progress graded event
    public boolean isIPRGraded() { return (isGraded() && !isCompleted()); }
    // Returns Optional.grade if it's not null, if null then it's Optional.empty
    public Optional<GradeInfo> getGrade() { return Optional.ofNullable(grade); }

    // Setters
    public void setGradeWeight(double newWeight) { this.grade.setWeight(newWeight); }
    public void setGradeTotal(double newTotal) { this.grade.setTotal(newTotal); }
    public void setGradeEarned(Double newEarned) {
        this.grade.setEarned(newEarned);
        // Adding a valid mark (should be valid if got to this point) marks event as completed
        if (newEarned != null) {
            this.completed = true;
        }
    }


    // Methods
    /*
    Toggle completion of this Event object.
    TODO: Decide if event with mark can be "uncompleted"
     */
    public void markCompleted(boolean done) {
        if (grade != null && grade.isMarked()) {
            throw new IllegalArgumentException("Marked events cannot be uncompleted.");
        }
        this.completed = done;
    }


}
