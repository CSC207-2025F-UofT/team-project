package interface_adapter.calendar;

import use_case.calendar.AddEventInputBoundary;
import use_case.calendar.AddEventInputData;
import use_case.calendar.ViewCalendarInputBoundary;
import use_case.calendar.ViewCalendarInputData;

import java.awt.Color;
import java.time.LocalDate;

public class CalendarController {

    private final AddEventInputBoundary addEventUseCase;
    private final ViewCalendarInputBoundary viewCalendarUseCase;

    public CalendarController(AddEventInputBoundary addEventUseCase,
                              ViewCalendarInputBoundary viewCalendarUseCase) {
        this.addEventUseCase = addEventUseCase;
        this.viewCalendarUseCase = viewCalendarUseCase;
    }

    public void addEvent(String name, LocalDate date, Color color) {
        AddEventInputData data = new AddEventInputData(name, date, color);
        addEventUseCase.add(data);
    }

    public void viewCalendar() {
        ViewCalendarInputData data = new ViewCalendarInputData();
        viewCalendarUseCase.viewCalendar(data);
    }
}
