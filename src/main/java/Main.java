import data_access.NagerDateHolidayService;
import entity.StudentTask;
import use_case.monitoring.HolidayService;
import use_case.monitoring.MonitoringDataAccessInterface;
import use_case.monitoring.MonitoringInteractor;
import use_case.monitoring.MonitoringOutputBoundary;
import use_case.monitoring.MonitoringOutputData;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MonitoringDataAccessInterface monitoringDataAccessObject = new MonitoringDataAccessInterface() {
                    @Override
                    public List<StudentTask> getTasksByUserId(int uniqueUserId) {
                        return new ArrayList<StudentTask>();
                    }

                    @Override
                    public String getCountryCodeByUserId(int uniqueUserId) {
                        return "CA";
                    }
                };

        HolidayService holidayService = new NagerDateHolidayService();

        MonitoringOutputBoundary monitoringPresenter = new MonitoringOutputBoundary() {
                    @Override
                    public void prepareSuccessView(MonitoringOutputData outputData) {
                        System.out.println("Monitoring loaded.");
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        System.out.println(errorMessage);
                    }
                };

        MonitoringInteractor monitoringInteractor = new MonitoringInteractor(monitoringDataAccessObject, holidayService,
                monitoringPresenter);

    }
}