package data_access;

import entity.Event;
import use_case.calendar.CalendarRepository;

import java.time.LocalDate;
import java.util.*;

public class InMemoryCalendarRepository implements CalendarRepository {
    private final Map<LocalDate, List<Event>> byDay = new HashMap<>();

    @Override
    public void add(Event e) {
        byDay.computeIfAbsent(e.getDate(), k -> new ArrayList<>()).add(e);
    }

    @Override
    public List<Event> eventsOn(LocalDate date) {
        return Collections.unmodifiableList(byDay.getOrDefault(date, Collections.emptyList()));
    }
}
