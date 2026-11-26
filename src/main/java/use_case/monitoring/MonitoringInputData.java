package use_case.monitoring;

public class MonitoringInputData {
    private final int uniqueUserId;

    public MonitoringInputData(int uniqueUserId) {
        this.uniqueUserId = uniqueUserId;
    }

    public int getUniqueUserId() {
        return uniqueUserId;
    }
}
