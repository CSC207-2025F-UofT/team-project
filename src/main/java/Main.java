import data_access.NagerDateHolidayService;
import use_case.monitoring.HolidayService;
import use_case.monitoring.MonitoringInteractor;

public class Main {
    public static void main(String[] args) {
        HolidayService holidayService = new NagerDateHolidayService();

        MonitoringInteractor monitoringInteractor = new MonitoringInteractor(holidayService);

    }
}