package data_access;

import entity.ChildProfile;
import entity.CommunicationMessage;
import entity.Pictogram;
import entity.WordType;
import org.junit.jupiter.api.Test;
import entity.CommunicationBoard;

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
    void savingADifferentBoardReplacesItAndKeepsHistory() {
        InMemoryProfileDataAccessObject dataAccess =
                new InMemoryProfileDataAccessObject(new ChildProfile());
        CommunicationMessage message = new CommunicationMessage(
                List.of(new Pictogram(1, "water", "water.png")),
                "I want water.",
                LocalDateTime.of(2026, 11, 23, 10, 0));
        dataAccess.saveMessage(message);

        CommunicationBoard newBoard = new CommunicationBoard();
        dataAccess.saveBoard(newBoard);

        assertSame(newBoard, dataAccess.getBoard());
        assertEquals(List.of(message), dataAccess.getHistory());
    }

    @Test
    void savingABoardWithoutAnActionRemovesItsLinks() {
        Pictogram play = new Pictogram(5, "play", "play.png", WordType.ACTION);
        Pictogram eat = new Pictogram(6, "eat", "eat.png", WordType.ACTION);
        Pictogram ball = new Pictogram(7, "ball", "ball.png", WordType.THING);
        Pictogram apple = new Pictogram(8, "apple", "apple.png", WordType.THING);
        ChildProfile profile = new ChildProfile();
        profile.getBoard().addPictogram(play);
        profile.getBoard().addPictogram(eat);
        profile.getLinks().link(play, ball);
        profile.getLinks().link(eat, apple);
        InMemoryProfileDataAccessObject dataAccess = new InMemoryProfileDataAccessObject(profile);

        // The caregiver removes "play" from the board.
        CommunicationBoard newBoard = new CommunicationBoard();
        newBoard.addPictogram(eat);
        dataAccess.saveBoard(newBoard);

        assertFalse(profile.getLinks().hasThings(play));
        assertEquals(List.of(apple), profile.getLinks().getThings(eat));
    }

    @Test
    void newProfileHasNoHistory() {
        InMemoryProfileDataAccessObject dataAccess =
                new InMemoryProfileDataAccessObject(new ChildProfile());

        assertTrue(dataAccess.getHistory().isEmpty());
    }
}
