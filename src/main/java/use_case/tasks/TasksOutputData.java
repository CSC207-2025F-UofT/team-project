package use_case.tasks;

import interface_adapter.tasks.dto.CourseDTO;
import java.util.List;

public class TasksOutputData {
    private final List<CourseDTO> courses;

    public TasksOutputData(List<CourseDTO> courses) {
        this.courses = courses;
    }

    public List<CourseDTO> getCourses() {
        return courses;
    }
}
