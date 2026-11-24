package use_case.search_exercises;

import entity.BodyPart;

/**
 * What to search the exercise library for. Every part is optional: a blank name, or a
 * {@code null} body part or equipment, means "don't filter on this".
 *
 * <p>It is a record, so two queries with the same parts are equal and can be used as a map key.
 *
 * @param name      part of the exercise name, such as "press"; may be blank
 * @param bodyPart  the body part to keep, or {@code null} for any
 * @param equipment the equipment to keep, such as "barbell", or {@code null} for any
 */
public record ExerciseSearchQuery(String name, BodyPart bodyPart, String equipment) {

    /**
     * Tells whether the query filters on nothing at all.
     *
     * @return {@code true} if the name is blank and no body part or equipment is chosen
     */
    public boolean isEmpty() {
        return isBlank(name) && bodyPart == null && isBlank(equipment);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
