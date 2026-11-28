package use_case.edit_planned_meal;

import java.time.LocalDate;

public class ChangePlannedPortionInputData {

    private final LocalDate date;
    private final String mealSlotId;
    private final int portionIndex;
    private final double newGrams;

    public ChangePlannedPortionInputData(
            LocalDate date, String mealSlotId,
            int portionIndex, double newGrams) {
        this.date = date;
        this.mealSlotId = mealSlotId;
        this.portionIndex = portionIndex;
        this.newGrams = newGrams;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMealSlotId() {
        return mealSlotId;
    }

    public int getPortionIndex() {
        return portionIndex;
    }

    public double getNewGrams() {
        return newGrams;
    }
}