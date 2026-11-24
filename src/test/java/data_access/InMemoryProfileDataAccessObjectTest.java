package data_access;

import entity.ChildProfile;
import entity.CommunicationMessage;
import entity.Pictogram;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryProfileDataAccessObjectTest {

    @Test
    void savedMessagesAppearInHistory() {
        InMemoryProfileDataAccessObject dataAccess =
                new InMemoryProfileDataAccessObject(new ChildProfile());
        CommunicationMessage message = new CommunicationMessage(
                List.of(new Pictogram(1, "water", "water.png")),
                "I want water.",
                LocalDateTime.of(2026, 11, 23, 10, 0));

        dataAccess.saveMessage(message);

        assertEquals(List.of(message), dataAccess.getHistory());
    }

    @Test
    void newProfileHasNoHistory() {
        InMemoryProfileDataAccessObject dataAccess =
                new InMemoryProfileDataAccessObject(new ChildProfile());

        assertTrue(dataAccess.getHistory().isEmpty());
    }
}
