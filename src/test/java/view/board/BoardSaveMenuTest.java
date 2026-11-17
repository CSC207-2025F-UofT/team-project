package view.board;

import interface_adapter.history.HistoryViewModel;
import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Verify the split Save menu delegates to two independent composition-root commands. */
class BoardSaveMenuTest {
    @Test void dirtyCanvasMenuCallsSaveAndSaveAsSeparately() {
        AtomicInteger saved = new AtomicInteger();
        AtomicInteger savedAs = new AtomicInteger();
        BoardWindow board = new BoardWindow(new JPanel(), new DrawingCanvas(),
                host -> new HistoryPanel(new HistoryViewModel(), host), new JPanel(),
                id -> false, saved::incrementAndGet, savedAs::incrementAndGet, () -> { });
        board.setSaveStateSupplier(() -> new SaveMenuState("123", SaveMenuState.Phase.MODIFIED));
        var rows = (JPanel) board.createSaveMenu().getComponent(0);
        ((JButton) rows.getComponent(0)).doClick();
        assertEquals(1, saved.get());
        assertEquals(0, savedAs.get());
        ((JButton) rows.getComponent(1)).doClick();
        assertEquals(1, saved.get());
        assertEquals(1, savedAs.get());
    }

    @Test void cleanCanvasMenuDoesNotOfferRedundantOverwrite() {
        BoardWindow board = new BoardWindow(new JPanel(), new DrawingCanvas(),
                host -> new HistoryPanel(new HistoryViewModel(), host), new JPanel(),
                id -> false, () -> {}, () -> {}, () -> {});
        board.setSaveStateSupplier(() -> new SaveMenuState("123", SaveMenuState.Phase.SAVED));
        var rows = (JPanel) board.createSaveMenu().getComponent(0);
        assertEquals(1, rows.getComponentCount());
        assertEquals("save-as-new-canvas", rows.getComponent(0).getName());
    }
}
