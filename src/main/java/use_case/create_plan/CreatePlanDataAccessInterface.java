package use_case.create_plan;

import entity.WorkoutPlan;
import java.util.UUID;

public interface CreatePlanDataAccessInterface {
    /** Returns true if this owner already has a plan with this name, compared ignoring case. */
    boolean existsByName(UUID ownerId, String planName);
    void save(WorkoutPlan workoutPlan);
}
