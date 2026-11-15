package view.glass;

import org.junit.jupiter.api.Test;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Headless tests: verify the real glass panel without opening a native modal window. */
class GlassDialogsTest {
    @Test
    void saveOptionsPreserveOriginalIndexesAndOrder() {
        AtomicInteger answer = new AtomicInteger(-1);
        var pane = new GlassDialogs.DialogPane("Save canvas", "Save your canvas?",
                GlassDialogs.Tone.QUESTION, null, new String[]{"Save", "Save As…", "Cancel"}, answer::set);
        assertEquals(3, pane.buttons.size());
        assertEquals("Save", pane.buttons.get(0).getText());
        assertEquals("Save As…", pane.buttons.get(1).getText());
        pane.buttons.get(1).doClick();
        assertEquals(1, answer.get());
        pane.buttons.get(2).doClick();
        assertEquals(2, answer.get());
    }

    @Test
    void confirmationCancelReportsSecondChoice() {
        AtomicInteger answer = new AtomicInteger(-1);
        var pane = new GlassDialogs.DialogPane("Confirm export", "Replace existing file?",
                GlassDialogs.Tone.QUESTION, null, new String[]{"Replace", "Cancel"}, answer::set);
        pane.buttons.get(1).doClick();
        assertEquals(1, answer.get());
    }

    @Test
    void inputFieldRemainsEditableAndIsNotModifiedByPresentation() {
        GlassTextField field = new GlassTextField(18, "");
        field.setText("Calculus Notes");
        var pane = new GlassDialogs.DialogPane("Save canvas as", "Canvas name:",
                GlassDialogs.Tone.QUESTION, field, new String[]{"Save", "Cancel"}, ignored -> { });
        assertEquals("Calculus Notes", field.getText());
        assertTrue(field.isEditable());
        assertTrue(isDescendantOf(pane, field));
    }

    @Test
    void longErrorMessageWrapsWithoutMakingTextEditable() {
        var pane = new GlassDialogs.DialogPane("Error", "Unable to export PDF because the target directory is not writable",
                GlassDialogs.Tone.ERROR, null, new String[]{"OK"}, ignored -> { });
        JTextArea message = findTextArea(pane);
        assertTrue(message != null);
        assertTrue(message.getLineWrap());
        assertTrue(!message.isEditable());
    }

    @Test
    void glassDialogPaintsOffscreenWithNoNativeWindow() {
        var pane = new GlassDialogs.DialogPane("Notice", "Select a formula first",
                GlassDialogs.Tone.WARNING, null, new String[]{"OK"}, ignored -> { });
        pane.setSize(460, 190);
        BufferedImage image = new BufferedImage(460, 190, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        pane.paint(graphics);
        graphics.dispose();
        assertNotEquals(0, image.getRGB(30, 30) >>> 24);
    }

    @Test
    void optionsRejectMissingChoices() {
        boolean threw = false;
        try { GlassDialogs.options(null, "Bad", "No options"); }
        catch (IllegalArgumentException expected) { threw = true; }
        assertTrue(threw);
    }

    private static boolean isDescendantOf(Container parent, Component target) {
        for (Component child : parent.getComponents()) {
            if (child == target || child instanceof Container c && isDescendantOf(c, target)) return true;
        }
        return false;
    }

    private static JTextArea findTextArea(Container parent) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JTextArea t) return t;
            if (child instanceof Container c) {
                JTextArea result = findTextArea(c);
                if (result != null) return result;
            }
        }
        return null;
    }
}
