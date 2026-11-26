package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationBoardTest {

    private Pictogram play;
    private Pictogram eat;
    private Pictogram ball;
    private Pictogram apple;
    private CommunicationBoard board;

    @BeforeEach
    void setUp() {
        play = new Pictogram(1, "play", "play.png", WordType.ACTION);
        eat = new Pictogram(2, "eat", "eat.png", WordType.ACTION);
        ball = new Pictogram(3, "ball", "ball.png", WordType.THING);
        apple = new Pictogram(4, "apple", "apple.png", WordType.THING);
        board = new CommunicationBoard();
    }

    private static Pictogram action(int id) {
        return new Pictogram(id, "action" + id, id + ".png", WordType.ACTION);
    }

    private static Pictogram thing(int id) {
        return new Pictogram(id, "thing" + id, id + ".png", WordType.THING);
    }

    // ----- actions -----

    @Test
    void newBoardIsEmpty() {
        assertTrue(board.isEmpty());
        assertTrue(board.getActions().isEmpty());
    }

    @Test
    void addActionKeepsCaregiverOrder() {
        board.addAction(play);
        board.addAction(eat);

        assertEquals(List.of(play, eat), board.getActions());
    }

    @Test
    void cannotAddSameActionTwice() {
        board.addAction(play);

        assertThrows(IllegalArgumentException.class, () -> board.addAction(play));
    }

    @Test
    void cannotPutThingOnMainBoard() {
        assertThrows(IllegalArgumentException.class, () -> board.addAction(ball));
    }

    @Test
    void boardHoldsAtMostSixActions() {
        for (int id = 100; id < 100 + CommunicationBoard.MAX_ACTIONS; id++) {
            board.addAction(action(id));
        }

        assertTrue(board.isFull());
        assertThrows(IllegalStateException.class, () -> board.addAction(play));
    }

    @Test
    void removeActionAlsoRemovesItsThings() {
        board.addAction(play);
        board.addThing(play, ball);

        board.removeAction(play);

        assertFalse(board.containsAction(play));
        assertThrows(IllegalArgumentException.class, () -> board.getThings(play));
    }

    @Test
    void cannotRemoveActionNotOnBoard() {
        assertThrows(IllegalArgumentException.class, () -> board.removeAction(play));
    }

    // ----- things -----

    @Test
    void addThingLinksItToAction() {
        board.addAction(play);

        board.addThing(play, ball);

        assertEquals(List.of(ball), board.getThings(play));
        assertTrue(board.hasThings(play));
    }

    @Test
    void newActionHasNoThings() {
        board.addAction(play);

        assertFalse(board.hasThings(play));
    }

    @Test
    void cannotAddThingToActionNotOnBoard() {
        assertThrows(IllegalArgumentException.class, () -> board.addThing(play, ball));
    }

    @Test
    void cannotLinkActionAsThing() {
        board.addAction(play);
        board.addAction(eat);

        assertThrows(IllegalArgumentException.class, () -> board.addThing(play, eat));
    }

    @Test
    void cannotLinkSameThingTwiceToSameAction() {
        board.addAction(play);
        board.addThing(play, ball);

        assertThrows(IllegalArgumentException.class, () -> board.addThing(play, ball));
    }

    @Test
    void sameThingCanBeLinkedToSeveralActions() {
        board.addAction(play);
        board.addAction(eat);

        board.addThing(play, apple);
        board.addThing(eat, apple);

        assertEquals(List.of(apple), board.getThings(play));
        assertEquals(List.of(apple), board.getThings(eat));
    }

    @Test
    void actionHoldsAtMostSixThings() {
        board.addAction(play);
        for (int id = 200; id < 200 + CommunicationBoard.MAX_THINGS_PER_ACTION; id++) {
            board.addThing(play, thing(id));
        }

        assertThrows(IllegalStateException.class, () -> board.addThing(play, ball));
    }

    @Test
    void removeThingOnlyAffectsThatAction() {
        board.addAction(play);
        board.addAction(eat);
        board.addThing(play, apple);
        board.addThing(eat, apple);

        board.removeThing(play, apple);

        assertTrue(board.getThings(play).isEmpty());
        assertEquals(List.of(apple), board.getThings(eat));
    }

    @Test
    void cannotRemoveThingNotLinked() {
        board.addAction(play);

        assertThrows(IllegalArgumentException.class, () -> board.removeThing(play, ball));
    }

    // ----- encapsulation -----

    @Test
    void returnedListsCannotBeModified() {
        board.addAction(play);
        board.addThing(play, ball);

        assertThrows(UnsupportedOperationException.class, () -> board.getActions().clear());
        assertThrows(UnsupportedOperationException.class, () -> board.getThings(play).clear());
        assertEquals(1, board.getActions().size());
        assertEquals(1, board.getThings(play).size());
    }


    @Test
    void lookupsWorkWithADifferentObjectWithTheSameId() {
        board.addAction(play);
        board.addThing(play, ball);
        Pictogram samePlay = new Pictogram(1, "play", "play.png", WordType.ACTION);
        Pictogram sameBall = new Pictogram(3, "ball", "ball.png", WordType.THING);

        assertTrue(board.containsAction(samePlay));
        assertEquals(List.of(ball), board.getThings(samePlay));
        assertTrue(board.getThings(samePlay).contains(sameBall));
    }
}