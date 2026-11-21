package entities;

import java.time.LocalDateTime;
import java.util.List;

/**
 * An immutable record of a completed workout: which template it came from,
 * when it was performed, and the sets done for each exercise.
 */
public class LogEntry {
    private final String templateName;
    private final LocalDateTime time;
    private final List<LogExerciseSet> exercises;

    public LogEntry(String templateName, LocalDateTime time, List<LogExerciseSet> exercises){
        this.templateName = templateName;
        this.time = time;
        if (exercises == null) {
            this.exercises = List.of();
        } else {
            this.exercises = List.copyOf(exercises);
        }
    }

    public String getTemplateName(){
        return templateName;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public List<LogExerciseSet> getExercises(){
        return exercises;
    }
}
