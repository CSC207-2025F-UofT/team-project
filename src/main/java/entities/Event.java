package entities;

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
    // TODO: how to get the info from GradeInfo? should it come here or not?

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

    // TODO: Adding mark, also automatically marks task as complete


}
