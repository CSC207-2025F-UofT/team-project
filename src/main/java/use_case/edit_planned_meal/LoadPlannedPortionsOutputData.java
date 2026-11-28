package use_case.edit_planned_meal;


import java.time.LocalDate;
import java.util.List;

public class LoadPlannedPortionsOutputData {

    private final LocalDate date;
    private final String mealSlotId;
    private final List<PlannedPortionOption> portions;

    public LoadPlannedPortionsOutputData(
            LocalDate date, String mealSlotId,
            List<PlannedPortionOption> portions) {
        this.date = date;
        this.mealSlotId = mealSlotId;
        this.portions = List.copyOf(portions);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMealSlotId() {
        return mealSlotId;
    }

    public List<PlannedPortionOption> getPortions() {
        return portions;
    }
}
