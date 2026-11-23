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
}
