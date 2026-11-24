package use_case.search_exercises;

import java.util.List;

/**
 * The exercises found, as plain data for the presenter, so outer layers never handle entities.
 *
 * @param results the matches in the order the library returned them; empty if nothing matched
 */
public record SearchExercisesOutputData(List<ExerciseResult> results) {

    /**
     * Copies the list, so the output can't be changed after it is created.
     *
     * @param results the matches
     */
    public SearchExercisesOutputData {
        results = List.copyOf(results);
    }

    /**
     * One exercise found, with everything the search screen shows about it.
     *
     * @param exerciseID       the library's ID, needed to look up its picture and animation
     * @param name             the display name
     * @param bodyPart         the body part's name, such as "CHEST"
     * @param target           the main muscle it works
     * @param secondaryMuscles other muscles it works
     * @param equipment        the equipment needed
     * @param mechanic         "compound" or "isolation"
     * @param instructions     the steps to perform it, in order
     */
    public record ExerciseResult(String exerciseID, String name, String bodyPart, String target,
                                 List<String> secondaryMuscles, String equipment, String mechanic,
                                 List<String> instructions) {
    }
}
