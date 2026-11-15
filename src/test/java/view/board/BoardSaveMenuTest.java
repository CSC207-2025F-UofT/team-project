package view.board;

import interface_adapter.history.HistoryViewModel;
import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Save and Save As must be distinct commands in the visible Save menu. */
class BoardSaveMenuTest {
    @Test
    void saveMenuOffersBothActionsWithoutConfusingDestinations() {
        AtomicInteger saved = new AtomicInteger();
        AtomicInteger savedAs = new AtomicInteger();
        BoardWindow board = new BoardWindow(new JPanel(), new DrawingCanvas(),
                host -> new HistoryPanel(new HistoryViewModel(), host), new JPanel(),
                id -> false, saved::incrementAndGet, savedAs::incrementAndGet, () -> { });
        JPopupMenu menu = board.createSaveMenu();
        assertEquals(2, menu.getComponentCount());
        JMenuItem first = (JMenuItem) menu.getComponent(0);
        JMenuItem second = (JMenuItem) menu.getComponent(1);
        assertEquals("save-current-canvas", first.getName());
        assertEquals("save-as-new-canvas", second.getName());
        first.doClick();
        assertEquals(1, saved.get());
        assertEquals(0, savedAs.get());
        second.doClick();
        assertEquals(1, saved.get());
        assertEquals(1, savedAs.get());
    }
}
