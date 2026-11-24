package entity;

import java.util.List;
import java.util.Objects;

/**
 * An exercise that can be added to a workout plan or logged in a workout,
 * such as "Bench Press". It describes the movement only; sets, reps and weight
 * are kept in {@link PlannedExercise} and {@link LoggedExercise}.
 *
 * <p>It holds no picture or animation: those are always looked up by the exercise's ID,
 * because the library's media links expire.
 */
public class Exercise {
    private final String exerciseID;
    private final String name;
    private final String description;
    private final BodyPart bodyPart;
    private final String target;
    private final List<String> secondaryMuscles;
    private final String equipment;
    private final String mechanic;
    private final List<String> instructions;

    /**
     * Creates an exercise. The two lists are copied, so changing the caller's lists later
     * does not change the exercise.
     *
     * @param exerciseID       the exercise library's ID for the exercise, such as "0025"
     * @param name             the display name, such as "Barbell Bench Press"
     * @param description      a short summary of the exercise
     * @param bodyPart         the main area of the body it trains
     * @param target           the main muscle it works, such as "Pectorals"
     * @param secondaryMuscles other muscles it works; empty if there are none
     * @param equipment        the equipment needed, such as "Barbell"
     * @param mechanic         whether it is a "compound" or an "isolation" movement
     * @param instructions     the steps to perform it, in order; empty if there are none
     * @throws IllegalArgumentException if the ID or name is blank
     * @throws NullPointerException     if the ID, name, body part or either list is null,
     *                                  or a list contains null
     */
    public Exercise(String exerciseID, String name, String description, BodyPart bodyPart, String target,
                    List<String> secondaryMuscles, String equipment, String mechanic, List<String> instructions) {
        this.exerciseID = requireNotBlank(exerciseID, "exerciseID");
        this.name = requireNotBlank(name, "name");
        this.description = description;
        this.bodyPart = Objects.requireNonNull(bodyPart, "bodyPart");
        this.target = target;
        this.secondaryMuscles = List.copyOf(secondaryMuscles);
        this.equipment = equipment;
        this.mechanic = mechanic;
        this.instructions = List.copyOf(instructions);
    }

    private static String requireNotBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    public String getExerciseID() { return exerciseID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BodyPart getBodyPart() { return bodyPart; }
    public String getTarget() { return target; }
    /** Returns the secondary muscles; the list cannot be modified. */
    public List<String> getSecondaryMuscles() { return secondaryMuscles; }
    public String getEquipment() { return equipment; }
    public String getMechanic() { return mechanic; }
    /** Returns the steps in order; the list cannot be modified. */
    public List<String> getInstructions() { return instructions; }
}
