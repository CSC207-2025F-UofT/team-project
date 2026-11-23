package use_case.monitoring;

import entity.StudentTask;

import java.util.List;

public interface MonitoringDataAccessInterface {
    List<StudentTask> getTasksByUserId(int uniqueUserId);

    String getCountryCodeByUserId(int uniqueUserId);
}
