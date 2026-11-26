package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedVocabularyTest {

    private Pictogram play;
    private Pictogram eat;
    private Pictogram ball;
    private Pictogram apple;
    private LinkedVocabulary links;

    @BeforeEach
    void setUp() {
        play = new Pictogram(1, "play", "play.png", WordType.ACTION);
        eat = new Pictogram(2, "eat", "eat.png", WordType.ACTION);
        ball = new Pictogram(3, "ball", "ball.png", WordType.THING);
        apple = new Pictogram(4, "apple", "apple.png", WordType.THING);
        links = new LinkedVocabulary();
    }

    @Test
    void actionWithoutLinksHasNoThings() {
        assertTrue(links.getThings(play).isEmpty());
        assertFalse(links.hasThings(play));
    }

    @Test
    void linkKeepsCaregiverOrder() {
        Pictogram toy = new Pictogram(5, "toy", "toy.png", WordType.THING);
        links.link(play, ball);
        links.link(play, toy);

        assertEquals(List.of(ball, toy), links.getThings(play));
        assertTrue(links.hasThings(play));
    }

    @Test
    void sameThingCanBeLinkedToSeveralActions() {
        links.link(play, apple);
        links.link(eat, apple);

        assertEquals(List.of(apple), links.getThings(play));
        assertEquals(List.of(apple), links.getThings(eat));
    }

    @Test
    void cannotLinkToAThing() {
        assertThrows(IllegalArgumentException.class, () -> links.link(ball, apple));
    }

    @Test
    void cannotLinkAnActionAsAThing() {
        assertThrows(IllegalArgumentException.class, () -> links.link(play, eat));
    }

    @Test
    void cannotLinkSameThingTwice() {
        links.link(play, ball);

        assertThrows(IllegalArgumentException.class, () -> links.link(play, ball));
        assertEquals(1, links.getThings(play).size());
    }

    @Test
    void actionHoldsAtMostSixThings() {
        for (int i = 0; i < LinkedVocabulary.MAX_THINGS_PER_ACTION; i++) {
            links.link(play, new Pictogram(100 + i, "thing" + i, "t.png", WordType.THING));
        }

        assertThrows(IllegalStateException.class, () -> links.link(play, ball));
    }

    @Test
    void unlinkOnlyAffectsThatAction() {
        links.link(play, apple);
        links.link(eat, apple);

        links.unlink(play, apple);

        assertFalse(links.hasThings(play));
        assertEquals(List.of(apple), links.getThings(eat));
        assertEquals(List.of(eat), links.getLinkedActions());
    }

    @Test
    void cannotUnlinkThingThatIsNotLinked() {
        assertThrows(IllegalArgumentException.class, () -> links.unlink(play, ball));
    }

    @Test
    void removeAllLinksClearsOneAction() {
        links.link(play, ball);
        links.link(eat, apple);

        links.removeAllLinks(play);
        links.removeAllLinks(play);   // doing it again is fine

        assertFalse(links.hasThings(play));
        assertTrue(links.hasThings(eat));
    }

    @Test
    void removeLinksNotOnBoardKeepsOnlyActionsOnTheBoard() {
        links.link(play, ball);
        links.link(eat, apple);
        CommunicationBoard board = new CommunicationBoard();
        board.addPictogram(eat);

        links.removeLinksNotOn(board);

        assertFalse(links.hasThings(play));
        assertEquals(List.of(apple), links.getThings(eat));
    }

    @Test
    void worksWithADifferentObjectWithTheSameId() {
        links.link(play, ball);
        Pictogram samePlay = new Pictogram(1, "play", "play.png", WordType.ACTION);

        assertEquals(List.of(ball), links.getThings(samePlay));
    }

    @Test
    void returnedListsCannotBeModified() {
        links.link(play, ball);

        assertThrows(UnsupportedOperationException.class, () -> links.getThings(play).clear());
        assertThrows(UnsupportedOperationException.class, () -> links.getLinkedActions().clear());
    }
}