package usecase.SetLearningGoal;

import java.time.LocalDate;

/**
 * The output boundary for the setLearningGoal usecase.
 */
public interface SetLearningGoalOutputBoundary {
    void setDailyTarget(int dailyTarget);
    void setStartDate(LocalDate startDate);
    void setEndDate(LocalDate endDate);
}
