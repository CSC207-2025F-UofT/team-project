package interface_adapter.view_history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.view_history.ViewHistoryOutputData;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewHistoryPresenterTest {

    private ViewHistoryViewModel viewModel;
    private ViewHistoryPresenter presenter;
    private int changeCount;

    @BeforeEach
    void setUp() {
        ZoneId zone = ZoneId.of("America/Toronto");
        Clock fixedToday = Clock.fixed(
                ZonedDateTime.of(2026, 11, 23, 18, 0, 0, 0, zone).toInstant(), zone);

        viewModel = new ViewHistoryViewModel();
        viewModel.addPropertyChangeListener(event -> changeCount++);
        presenter = new ViewHistoryPresenter(viewModel, fixedToday);
    }

    @Test
    void successViewFormatsDaysAndTimes() {
        ViewHistoryOutputData data = new ViewHistoryOutputData(List.of(
                new ViewHistoryOutputData.Entry("I want cookie.",
                        LocalDateTime.of(2026, 11, 20, 9, 5)),
                new ViewHistoryOutputData.Entry("I need help.",
                        LocalDateTime.of(2026, 11, 22, 14, 30)),
                new ViewHistoryOutputData.Entry("I want water.",
                        LocalDateTime.of(2026, 11, 23, 10, 0))));

        presenter.prepareSuccessView(data);

        List<ViewHistoryState.HistoryRow> rows = viewModel.getState().getRows();
        assertEquals(3, rows.size());

        assertEquals("Fri, Nov 20", rows.get(0).getDayLabel());
        assertEquals("9:05 AM", rows.get(0).getTimeLabel());
        assertEquals("I want cookie.", rows.get(0).getText());

        assertEquals("Yesterday", rows.get(1).getDayLabel());
        assertEquals("2:30 PM", rows.get(1).getTimeLabel());

        assertEquals("Today", rows.get(2).getDayLabel());
        assertEquals("10:00 AM", rows.get(2).getTimeLabel());

        assertNull(viewModel.getState().getErrorMessage());
        assertEquals(1, changeCount);
    }

    @Test
    void failViewShowsMessageAndClearsRows() {
        presenter.prepareSuccessView(new ViewHistoryOutputData(List.of(
                new ViewHistoryOutputData.Entry("I want water.",
                        LocalDateTime.of(2026, 11, 23, 10, 0)))));

        presenter.prepareFailView("No messages yet.");

        assertTrue(viewModel.getState().getRows().isEmpty());
        assertEquals("No messages yet.", viewModel.getState().getErrorMessage());
        assertEquals(2, changeCount);
    }
}