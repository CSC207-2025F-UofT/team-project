package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @Test
    void createNewHasStartingStats() {
        Player player = Player.createNew();
        assertEquals(100, player.getEnergy());
        assertEquals(100, player.getSanity());
        assertEquals(50, player.getAcademicSurvival());
        assertEquals(50, player.getMoney());
        assertFalse(player.isOutOfStat());
    }

    @Test
    void constructorClampsOutOfRangeValues() {
        Player player = new Player(150, -10, 101, -5);
        assertEquals(100, player.getEnergy());
        assertEquals(0, player.getSanity());
        assertEquals(100, player.getAcademicSurvival());
        assertEquals(0, player.getMoney());
    }

    @Test
    void applyEffectsChangesStats() {
        Player player = new Player(50, 50, 50, 50);
        player.applyEffects(new StatEffects(10, -20, 5, -30));
        assertEquals(60, player.getEnergy());
        assertEquals(30, player.getSanity());
        assertEquals(55, player.getAcademicSurvival());
        assertEquals(20, player.getMoney());
    }

    @Test
    void applyEffectsClampsStatsToRange() {
        Player player = new Player(95, 5, 50, 10);
        player.applyEffects(new StatEffects(20, -20, 0, -100));
        assertEquals(100, player.getEnergy());
        assertEquals(0, player.getSanity());
        assertEquals(0, player.getMoney());
    }

    @Test
    void moneyHasNoUpperLimit() {
        Player player = new Player(50, 50, 50, 90);
        player.applyEffects(new StatEffects(0, 0, 0, 500));
        assertEquals(590, player.getMoney());
    }

    @Test
    void outOfStatWhenEnergySanityOrAcademicIsZero() {
        assertTrue(new Player(0, 50, 50, 50).isOutOfStat());
        assertTrue(new Player(50, 0, 50, 50).isOutOfStat());
        assertTrue(new Player(50, 50, 0, 50).isOutOfStat());
    }

    @Test
    void zeroMoneyIsNotOutOfStat() {
        assertFalse(new Player(50, 50, 50, 0).isOutOfStat());
    }
}
