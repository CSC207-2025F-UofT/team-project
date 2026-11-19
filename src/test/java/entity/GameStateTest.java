package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateTest {

    @Test
    void startNewIsDayOneAtResidenceWithStartingStats() {
        GameState game = GameState.startNew();
        assertEquals(1, game.getDay());
        assertEquals(GameState.START_LOCATION_ID, game.getCurrentLocationId());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(100, game.getPlayer().getEnergy());
        assertEquals(100, game.getPlayer().getSanity());
        assertEquals(50, game.getPlayer().getAcademicSurvival());
        assertEquals(50, game.getPlayer().getMoney());
    }

    @Test
    void applyChangesStatsAndKeepsPlaying() {
        GameState game = GameState.startNew();
        GameStatus status = game.apply(new Outcome.Builder()
                .effects(new StatEffects(-10, 0, 5, -20)).build());
        assertEquals(GameStatus.IN_PROGRESS, status);
        assertEquals(90, game.getPlayer().getEnergy());
        assertEquals(55, game.getPlayer().getAcademicSurvival());
        assertEquals(30, game.getPlayer().getMoney());
    }

    @Test
    void applyMovesPlayerWhenOutcomeHasNewLocation() {
        GameState game = GameState.startNew();
        game.apply(new Outcome.Builder().moveTo("robarts").build());
        assertEquals("robarts", game.getCurrentLocationId());
    }

    @Test
    void applyKeepsLocationWhenOutcomeHasNoNewLocation() {
        GameState game = GameState.startNew();
        game.apply(new Outcome.Builder().build());
        assertEquals(GameState.START_LOCATION_ID, game.getCurrentLocationId());
    }

    @Test
    void applyLosesWhenAStatHitsZero() {
        GameState game = GameState.startNew();
        GameStatus status = game.apply(new Outcome.Builder()
                .effects(new StatEffects(-200, 0, 0, 0)).build());
        assertEquals(GameStatus.LOST, status);
        assertEquals(GameStatus.LOST, game.getStatus());
        assertEquals(0, game.getPlayer().getEnergy());
    }

    @Test
    void applyDoesNothingOnceTheGameIsLost() {
        GameState game = new GameState(1, new Player(5, 50, 50, 50), "residence");
        game.apply(new Outcome.Builder().effects(new StatEffects(-5, 0, 0, 0)).build());

        GameStatus status = game.apply(new Outcome.Builder()
                .effects(new StatEffects(50, 0, 0, 0)).moveTo("robarts").build());

        assertEquals(GameStatus.LOST, status);
        assertEquals(0, game.getPlayer().getEnergy());
        assertEquals("residence", game.getCurrentLocationId());
    }
}
