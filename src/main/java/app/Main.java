package app;

import view.DashboardView;

import javax.swing.*;

import data_access.InMemoryCalendarRepository;
import interface_adapter.calendar.*;
import use_case.calendar.*;
import view.CalendarPanel;
import view.DashboardView;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LockIn!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);

            DashboardView dashboardView = new DashboardView(frame);

            InMemoryCalendarRepository repo = new InMemoryCalendarRepository();
            CalendarViewModel calendarViewModel = new CalendarViewModel(repo);

            AddEventOutputBoundary addPresenter = new AddEventPresenter(calendarViewModel);
            AddEventInputBoundary addInteractor = new AddEventInteractor(repo, addPresenter);

            ViewCalendarOutputBoundary viewPresenter = new ViewCalendarPresenter(calendarViewModel);
            ViewCalendarInputBoundary viewInteractor = new ViewCalendarInteractor(viewPresenter);

            CalendarController calendarController =
                    new CalendarController(addInteractor, viewInteractor);

            CalendarPanel.sharedViewModel = calendarViewModel;
            CalendarPanel.sharedCalendarController = calendarController;


            // Important: use BorderLayout so left panel stays visible
            frame.getContentPane().setLayout(new java.awt.BorderLayout());
            frame.getContentPane().add(dashboardView, java.awt.BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
