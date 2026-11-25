package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChildProfileTest {

    private Pictogram water;
    private CommunicationMessage message;
    private ChildProfile profile;

    @BeforeEach
    void setUp() {
        water = new Pictogram(1, "water", "water.png");
        message = new CommunicationMessage(
                List.of(water), "I want water.", LocalDateTime.of(2026, 10, 26, 10, 0));
        profile = new ChildProfile();
    }

    @Test
    void newProfileHasEmptyBoardAndNoHistory() {
        assertTrue(profile.getBoard().isEmpty());
        assertFalse(profile.hasHistory());
    }

    @Test
    void addMessageAppendsInOrder() {
        CommunicationMessage later = new CommunicationMessage(
                List.of(water), "I want water.", LocalDateTime.of(2026, 10, 26, 11, 0));

        profile.addMessage(message);
        profile.addMessage(later);

        assertEquals(List.of(message, later), profile.getHistory());
    }

    @Test
    void historyCannotBeModifiedFromOutside() {
        profile.addMessage(message);

        assertThrows(UnsupportedOperationException.class,
                () -> profile.getHistory().clear());
        assertEquals(1, profile.getHistory().size());
    }

    @Test
    void profileMakesDefensiveCopyOfHistory() {
        List<CommunicationMessage> saved = new ArrayList<>();
        saved.add(message);

        ChildProfile loaded = new ChildProfile(new CommunicationBoard(), saved);
        saved.clear();

        assertEquals(List.of(message), loaded.getHistory());
    }

    @Test
    void boardChangesAreKeptInProfile() {
        Pictogram drink = new Pictogram(10, "drink", "drink.png", WordType.ACTION);

        profile.getBoard().addAction(drink);

        assertTrue(profile.getBoard().containsAction(drink));
    }

    @Test
    void cannotAddNullMessage() {
        assertThrows(NullPointerException.class,
                () -> profile.addMessage(null));
    }

    @Test
    void boardAndHistoryCannotBeNull() {
        assertThrows(NullPointerException.class,
                () -> new ChildProfile(null, new ArrayList<>()));
        assertThrows(NullPointerException.class,
                () -> new ChildProfile(new CommunicationBoard(), null));
    }
}
