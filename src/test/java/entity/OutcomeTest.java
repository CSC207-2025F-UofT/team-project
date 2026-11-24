package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutcomeTest {

    @Test
    void emptyBuilderGivesNeutralOutcome() {
        Outcome outcome = new Outcome.Builder().build();
        assertSame(StatEffects.NONE, outcome.getEffects());
        assertEquals(List.of(), outcome.getItemsGained());
        assertEquals(List.of(), outcome.getItemsLost());
        assertEquals(List.of(), outcome.getUnlockedLocationIds());
        assertEquals(0, outcome.getTimeBlocksUsed());
        assertNull(outcome.getNewLocationId());
        assertEquals("", outcome.getMessage());
    }

    @Test
    void builderSetsEveryField() {
        StatEffects effects = new StatEffects(-5, 0, 0, 0);
        Outcome outcome = new Outcome.Builder()
                .effects(effects)
                .gainItem("coffee")
                .loseItem("coupon")
                .unlockLocation("bahen_lab")
                .timeBlocksUsed(1)
                .moveTo("robarts")
                .message("You walk to Robarts.")
                .build();
        assertSame(effects, outcome.getEffects());
        assertEquals(List.of("coffee"), outcome.getItemsGained());
        assertEquals(List.of("coupon"), outcome.getItemsLost());
        assertEquals(List.of("bahen_lab"), outcome.getUnlockedLocationIds());
        assertEquals(1, outcome.getTimeBlocksUsed());
        assertEquals("robarts", outcome.getNewLocationId());
        assertEquals("You walk to Robarts.", outcome.getMessage());
    }

    @Test
    void listsCannotBeModified() {
        Outcome outcome = new Outcome.Builder().gainItem("coffee").build();
        assertThrows(UnsupportedOperationException.class,
                () -> outcome.getItemsGained().add("plushie"));
    }

    @Test
    void reusingBuilderDoesNotChangeBuiltOutcome() {
        Outcome.Builder builder = new Outcome.Builder().gainItem("coffee");
        Outcome first = builder.build();
        builder.gainItem("plushie");
        assertEquals(List.of("coffee"), first.getItemsGained());
    }
}
