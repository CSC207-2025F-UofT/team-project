package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void restoreRaisesEnergyToPercentOfMax() {
        Player player = new Player(30, 100);
        player.restoreEnergyToPercent(85);
        assertEquals(85, player.getEnergy());
    }

    @Test
    void restoreNeverLowersEnergy() {
        Player player = new Player(90, 100);
        player.restoreEnergyToPercent(80);
        assertEquals(90, player.getEnergy());
    }

    @Test
    void spendEnergyDeductsTheAmount() {
        Player player = new Player(50, 100);
        player.spendEnergy(20);
        assertEquals(30, player.getEnergy());
    }

    @Test
    void spendEnergyCanUseExactlyAllEnergy() {
        Player player = new Player(20, 100);
        player.spendEnergy(20);
        assertEquals(0, player.getEnergy());
    }

    @Test
    void spendingMoreThanAvailableChangesNothing() {
        Player player = new Player(10, 100);
        assertThrows(IllegalStateException.class, () -> player.spendEnergy(20));
        assertEquals(10, player.getEnergy());
    }

    @Test
    void spendingNegativeEnergyIsRejected() {
        Player player = new Player(10, 100);
        assertThrows(IllegalArgumentException.class, () -> player.spendEnergy(-5));
        assertEquals(10, player.getEnergy());
    }

    @Test
    void newPlayerHasNoActiveActivity() {
        assertFalse(new Player(100, 100).hasActiveActivity());
    }

    @Test
    void startActivityLocksThePlayer() {
        Player player = new Player(100, 100);
        player.startActivity("attempt-1");
        assertTrue(player.hasActiveActivity());
    }

    @Test
    void secondActivityCannotStartWhileOneIsActive() {
        Player player = new Player(100, 100);
        player.startActivity("attempt-1");
        assertThrows(IllegalStateException.class, () -> player.startActivity("attempt-2"));
    }

    @Test
    void finishActivityReleasesOnlyTheMatchingAttempt() {
        Player player = new Player(100, 100);
        player.startActivity("attempt-1");
        assertFalse(player.finishActivity("other"));
        assertTrue(player.hasActiveActivity());
        assertTrue(player.finishActivity("attempt-1"));
        assertFalse(player.hasActiveActivity());
    }

    @Test
    void finishingTwiceIsHarmless() {
        Player player = new Player(100, 100);
        player.startActivity("attempt-1");
        player.finishActivity("attempt-1");
        assertFalse(player.finishActivity("attempt-1"));
        assertFalse(player.hasActiveActivity());
    }
}
