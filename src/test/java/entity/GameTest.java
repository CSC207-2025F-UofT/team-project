package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    @Test
    void finishingAnEmptyMatchReportsADraw() {
        Player first = new Player("Player 1");
        Player second = new Player("Player 2");
        Game game = new Game(new ArrayList<>(List.of(first, second)));

        GameResult result = game.finish();

        assertTrue(result.isDraw());
        assertEquals(0, result.getPlayer1Score());
        assertEquals(0, result.getPlayer2Score());
        assertEquals(Set.of("Player 1", "Player 2"), Set.of(result.getPlayer1Name(), result.getPlayer2Name()));
    }
}
