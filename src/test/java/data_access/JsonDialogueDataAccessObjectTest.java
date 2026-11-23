package data_access;

import entity.DialogueNode;
import entity.DialogueOption;
import entity.NPC;
import entity.StatEffects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JsonDialogueDataAccessObjectTest {

    @Test
    void readsNpcAndItsStartNode() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("test_dialogues.json");

        NPC npc = dataAccess.getNpc("test_npc");

        assertNotNull(npc);
        assertEquals("test_npc", npc.getId());
        assertEquals("Test Person", npc.getName());
        assertEquals("hello", npc.getStartNodeId());
        assertEquals("Hello there.", npc.getStartNode().getText());
        assertEquals(2, npc.getStartNode().getOptions().size());
    }

    @Test
    void readsOptionTextMessageAndStatChanges() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("test_dialogues.json");

        DialogueOption option = dataAccess.getNpc("test_npc").getStartNode().getOption("all_stats");

        assertEquals("Change every stat", option.getText());
        assertEquals("Everything changed.", option.getOutcome().getMessage());
        StatEffects effects = option.getOutcome().getEffects();
        assertEquals(-5, effects.getEnergyDelta());
        assertEquals(10, effects.getSanityDelta());
        assertEquals(3, effects.getAcademicDelta());
        assertEquals(-2, effects.getMoneyDelta());
    }

    @Test
    void statChangesAndMessageLeftOutOfTheFileDefaultToNothing() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("test_dialogues.json");

        DialogueOption option = dataAccess.getNpc("test_npc").getStartNode().getOption("no_stats");

        assertEquals("", option.getOutcome().getMessage());
        StatEffects effects = option.getOutcome().getEffects();
        assertEquals(0, effects.getEnergyDelta());
        assertEquals(0, effects.getSanityDelta());
        assertEquals(0, effects.getAcademicDelta());
        assertEquals(0, effects.getMoneyDelta());
    }

    @Test
    void readsEveryNpcInTheFile() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("test_dialogues.json");

        assertEquals("Second Person", dataAccess.getNpc("second_npc").getName());
    }

    @Test
    void unknownNpcIdGivesNull() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("test_dialogues.json");

        assertNull(dataAccess.getNpc("nobody"));
    }

    @Test
    void missingFileThrowsAnExceptionThatNamesTheFile() {
        try {
            new JsonDialogueDataAccessObject("no_such_file.json");
            fail("Expected an exception because the file does not exist.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("no_such_file.json"));
        }
    }

    /**
     * Checks the real file the game ships with, so a typo in dialogues.json
     * is caught by the tests instead of by a player. Story 4 needs the
     * Shawarma Lady to have a greeting and two responses.
     */
    @Test
    void realDialogueFileHasTheShawarmaLadyWithTwoResponses() {
        JsonDialogueDataAccessObject dataAccess =
                new JsonDialogueDataAccessObject("dialogues.json");

        NPC shawarmaLady = dataAccess.getNpc("shawarma_lady");
        assertNotNull(shawarmaLady);
        assertEquals("Shawarma Lady", shawarmaLady.getName());

        DialogueNode greeting = shawarmaLady.getStartNode();
        assertNotNull(greeting);
        assertTrue(greeting.getText().length() > 0);
        assertEquals(2, greeting.getOptions().size());
    }
}
