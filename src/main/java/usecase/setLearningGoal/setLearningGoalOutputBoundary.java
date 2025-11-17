package usecase.setLearningGoal;

import java.time.LocalDate;

/**
 * The output boundary for the setLearningGoal usecase.
 */
public interface setLearningGoalOutputBoundary {
    void setDailyTarget(int dailyTarget);
    void setStartDate(LocalDate startDate);
    void setEndDate(LocalDate endDate);
}
