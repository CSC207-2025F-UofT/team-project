package use_case.search_exercises;

import entity.BodyPart;

/**
 * What the user entered on the search screen.
 *
 * @param name      the text typed in the name box; may be blank
 * @param bodyPart  the body part chosen, or {@code null} for any
 * @param equipment the equipment chosen, or {@code null} for any
 */
public record SearchExercisesInputData(String name, BodyPart bodyPart, String equipment) {
}
