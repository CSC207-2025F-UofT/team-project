package plan4life;

import plan4life.data_access.*;
import plan4life.presenter.CalendarPresenter;
import plan4life.use_case.block_off_time.*;
import plan4life.view.CalendarFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScheduleDataAccessInterface scheduleDAO = new InMemoryScheduleDAO();
            CalendarFrame view = new CalendarFrame(); // temp
            BlockOffTimeOutputBoundary presenter = new CalendarPresenter(view);
            BlockOffTimeInputBoundary interactor = new BlockOffTimeInteractor(scheduleDAO, presenter);
            BlockOffTimeController controller = new BlockOffTimeController(interactor);

            view.setBlockOffTimeController(controller); // We'll add this method
        });
    }
}