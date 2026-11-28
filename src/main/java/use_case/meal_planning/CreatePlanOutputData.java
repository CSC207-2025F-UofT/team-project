package use_case.meal_planning;

import java.time.LocalDate;

/**
 * Output data for a successfully created daily meal plan.
 */
public class CreatePlanOutputData {

    private final String planId;
    private final LocalDate date;

    public CreatePlanOutputData(String planId, LocalDate date) {
        this.planId = planId;
        this.date = date;
    }

    public String getPlanId() {
        return planId;
    }

    public LocalDate getDate() {
        return date;
    }
}
