package use_case.view_history;

import entity.CommunicationMessage;
import entity.Pictogram;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewHistoryInteractorTest {

    private final Pictogram water = new Pictogram(1, "water", "water.png");
    private final Pictogram apple = new Pictogram(2, "apple", "apple.png");

    /** A fake presenter that just remembers what the interactor gave it. */
    private static class RecordingPresenter implements ViewHistoryOutputBoundary {
        ViewHistoryOutputData successData;
        String errorMessage;

        @Override
        public void prepareSuccessView(ViewHistoryOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @Test
    void successViewReceivesMessagesInChronologicalOrder() {
        LocalDateTime ten = LocalDateTime.of(2026, 11, 22, 10, 0);
        LocalDateTime eleven = LocalDateTime.of(2026, 11, 22, 11, 0);
        List<CommunicationMessage> saved = List.of(
                new CommunicationMessage(List.of(water), "I want water.", ten),
                new CommunicationMessage(List.of(apple), "I want apple.", eleven));

        ViewHistoryDataAccessInterface dataAccess = () -> saved;
        RecordingPresenter presenter = new RecordingPresenter();

        new ViewHistoryInteractor(dataAccess, presenter).execute();

        assertNull(presenter.errorMessage);
        List<ViewHistoryOutputData.Entry> entries = presenter.successData.getEntries();
        assertEquals(2, entries.size());
        assertEquals("I want water.", entries.get(0).getText());
        assertEquals(ten, entries.get(0).getTimestamp());
        assertEquals("I want apple.", entries.get(1).getText());
        assertEquals(eleven, entries.get(1).getTimestamp());
    }

    @Test
    void sortsMessagesEvenIfDataAccessReturnsThemOutOfOrder() {
        LocalDateTime nine = LocalDateTime.of(2026, 11, 22, 9, 0);
        LocalDateTime ten = LocalDateTime.of(2026, 11, 22, 10, 0);
        LocalDateTime yesterday = LocalDateTime.of(2026, 11, 21, 15, 0);
        ViewHistoryDataAccessInterface dataAccess = () -> List.of(
                new CommunicationMessage(List.of(water), "ten", ten),
                new CommunicationMessage(List.of(apple), "yesterday", yesterday),
                new CommunicationMessage(List.of(water), "nine", nine));
        RecordingPresenter presenter = new RecordingPresenter();

        new ViewHistoryInteractor(dataAccess, presenter).execute();

        List<ViewHistoryOutputData.Entry> entries = presenter.successData.getEntries();
        assertEquals("yesterday", entries.get(0).getText());
        assertEquals("nine", entries.get(1).getText());
        assertEquals("ten", entries.get(2).getText());
    }

    @Test
    void emptyHistoryIsANormalEmptyResult() {
        ViewHistoryDataAccessInterface dataAccess = ArrayList::new;
        RecordingPresenter presenter = new RecordingPresenter();

        new ViewHistoryInteractor(dataAccess, presenter).execute();

        assertNull(presenter.errorMessage);
        assertTrue(presenter.successData.getEntries().isEmpty());
    }

    @Test
    void outputDataCannotBeModified() {
        LocalDateTime time = LocalDateTime.of(2026, 11, 22, 10, 0);
        ViewHistoryDataAccessInterface dataAccess = () -> List.of(
                new CommunicationMessage(List.of(water), "I want water.", time));
        RecordingPresenter presenter = new RecordingPresenter();

        new ViewHistoryInteractor(dataAccess, presenter).execute();

        assertThrows(UnsupportedOperationException.class,
                () -> presenter.successData.getEntries().clear());
    }
}