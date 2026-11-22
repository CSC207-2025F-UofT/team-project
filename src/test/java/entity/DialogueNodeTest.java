package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DialogueNodeTest {

    @Test
    void optionGettersReturnWhatWasPassedIn() {
        Outcome outcome = new Outcome.Builder().message("She smiles.").build();
        DialogueOption option = new DialogueOption("be_nice", "Say something nice", outcome);

        assertEquals("be_nice", option.getId());
        assertEquals("Say something nice", option.getText());
        assertSame(outcome, option.getOutcome());
    }

    @Test
    void nodeGettersReturnWhatWasPassedIn() {
        List<DialogueOption> options = new ArrayList<DialogueOption>();
        options.add(new DialogueOption("be_nice", "Say something nice", new Outcome.Builder().build()));
        options.add(new DialogueOption("be_rude", "Say something rude", new Outcome.Builder().build()));
        DialogueNode node = new DialogueNode("greeting", "What do you want?", options);

        assertEquals("greeting", node.getId());
        assertEquals("What do you want?", node.getText());
        assertEquals(2, node.getOptions().size());
        assertEquals("be_nice", node.getOptions().get(0).getId());
        assertEquals("be_rude", node.getOptions().get(1).getId());
    }

    @Test
    void getOptionFindsOptionById() {
        List<DialogueOption> options = new ArrayList<DialogueOption>();
        options.add(new DialogueOption("be_nice", "Say something nice", new Outcome.Builder().build()));
        options.add(new DialogueOption("be_rude", "Say something rude", new Outcome.Builder().build()));
        DialogueNode node = new DialogueNode("greeting", "What do you want?", options);

        assertEquals("Say something rude", node.getOption("be_rude").getText());
    }

    @Test
    void getOptionReturnsNullForUnknownId() {
        DialogueNode node = new DialogueNode("greeting", "What do you want?",
                new ArrayList<DialogueOption>());
        assertNull(node.getOption("does_not_exist"));
    }
}
