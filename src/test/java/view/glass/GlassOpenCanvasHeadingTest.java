package view.glass;

import org.junit.jupiter.api.Test;
import view.board.SavedCanvasChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The modal doesn't duplicate the chooser's own heading. */
class GlassOpenCanvasHeadingTest {
    @Test void chooserIsTheOnlyOwnerOfOpenCanvasHeading() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SavedCanvasChooser chooser = new SavedCanvasChooser(List.of());
            GlassDialogs.DialogPane pane = new GlassDialogs.DialogPane("", "", GlassDialogs.Tone.QUESTION,
                    chooser, new String[]{"Open", "Cancel"}, ignored -> { });
            assertEquals(1, headings(pane));
        });
    }

    private static int headings(Component c) {
        int count = (c instanceof JLabel label && "Open Canvas".equals(label.getText())) ? 1 : 0;
        if (c instanceof Container parent) for (Component child : parent.getComponents()) count += headings(child);
        return count;
    }
}
