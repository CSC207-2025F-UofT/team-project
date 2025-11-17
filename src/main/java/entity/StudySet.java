package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StudySet {
    private final String id;
    private String courseCode;
    private String title;
    private String creator;
    private final List<Question> questions;

    public StudySet(String courseCode, String title, String creator) {
        this.id = UUID.randomUUID().toString();
        this.courseCode = courseCode;
        this.title = title;
        this.creator = creator;
        this.questions = new ArrayList<>();
    }

    // Constructor for loading existing sets (e.g., from JSON)
    public StudySet(String id, String courseCode, String title, String creator,
                    List<Question> questions) {
        this.id = id;
        this.courseCode = courseCode;
        this.title = title;
        this.creator = creator;
        this.questions = new ArrayList<>(questions);
    }

    public String getId() { return id; }
    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getCreator() { return creator; }
    public List<Question> getQuestions() { return Collections.unmodifiableList(questions); }

    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setTitle(String title) { this.title = title; }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int size() {
        return questions.size();
    }
}
