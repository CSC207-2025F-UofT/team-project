package use_case.monitoring;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MonitoringOutputData {
    private final List<MonitoringTaskData> tasks;

    public MonitoringOutputData(List<MonitoringTaskData> tasks) {
        this.tasks = tasks;
    }

    public List<MonitoringTaskData> getTasks() {
        return tasks;
    }

    public static class MonitoringTaskData {
        private final int uniqueTaskId;
        private final String title;
        private final LocalDate deadline;
        private final byte completionProgress;
        private final String difficultyLevel;
        private final boolean overdue;
        private final Optional<String> holidayName;

        public MonitoringTaskData(int uniqueTaskId, String title, LocalDate deadline, byte completionProgress,
                                  String difficultyLevel, boolean overdue, Optional<String> holidayName) {
            this.uniqueTaskId = uniqueTaskId;
            this.title = title;
            this.deadline = deadline;
            this.completionProgress = completionProgress;
            this.difficultyLevel = difficultyLevel;
            this.overdue = overdue;
            this.holidayName = holidayName;
        }

        public int getUniqueTaskId() {
            return uniqueTaskId;
        }

        public String getTitle() {
            return title;
        }

        public LocalDate getDeadline() {
            return deadline;
        }

        public byte getCompletionProgress() {
            return completionProgress;
        }

        public String getDifficultyLevel() {
            return difficultyLevel;
        }

        public boolean isOverdue() {
            return overdue;
        }

        public Optional<String> getHolidayName() {
            return holidayName;
        }
    }

}
