package use_case.tasks;

import interface_adapter.tasks.dto.CourseDTO;
import java.util.List;

public interface TasksDataAccessInterface {

    /**
     * Returns all courses with their associated tasks for the current user.
     */
    List<CourseDTO> getCourses();
}
