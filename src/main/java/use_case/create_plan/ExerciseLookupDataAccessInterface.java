package use_case.create_plan;

import entity.Exercise;

public interface ExerciseLookupDataAccessInterface {
    /** Returns null when no exercise matches. */
    Exercise findById(String exerciseId);
}
