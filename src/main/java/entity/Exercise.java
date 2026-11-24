package entity;

import java.util.List;

/**
 * An exercise that can be added to a workout plan or logged in a workout,
 * such as "Bench Press". It describes the movement only; sets, reps and weight
 * are kept in {@link PlannedExercise} and {@link LoggedExercise}.
 */
public class Exercise {
    private final String exerciseID;
    private final String name;
    private final String description;
    private final String mainMuscleGroup;
    private final List<String> musclesWorked;
    private final String equipment;
    private final String imageUrl; // optional, may be null

    /**
     * Creates an exercise.
     *
     * @param exerciseID      the unique ID of the exercise
     * @param name            the display name, such as "Bench Press"
     * @param description     how to perform the exercise
     * @param mainMuscleGroup the muscle group the exercise mainly targets, such as "chest"
     * @param musclesWorked   every muscle the exercise works
     * @param equipment       the equipment needed, such as "barbell"
     * @param imageUrl        a link to a picture of the exercise, or {@code null} if there is none
     */
    public Exercise(String exerciseID, String name, String description, String mainMuscleGroup,
                    List<String> musclesWorked, String equipment, String imageUrl) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.description = description;
        this.mainMuscleGroup = mainMuscleGroup;
        this.musclesWorked = musclesWorked;
        this.equipment = equipment;
        this.imageUrl = imageUrl;
    }

    public String getExerciseID() { return exerciseID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getMainMuscleGroup() { return mainMuscleGroup; }
    public List<String> getMusclesWorked() { return musclesWorked; }
    public String getEquipment() { return equipment; }
    public String getImageUrl() { return imageUrl; }
}
