package view.board;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import interface_adapter.history.HistoryViewModel;
import org.junit.jupiter.api.Test;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class HistoryGlassMenuAndDropTest {
    private static HistoryItem calc() {
        return new HistoryItem(new HistoryEntry("h1", HistoryEntry.Kind.CALCULATION,
                "Calculation", "2+3", "5", null, 0, 0, Instant.EPOCH));
    }

    @Test void historyUsesGlassMenuAndRoutesLibraryPdfAndDeleteActions() {
        AtomicInteger library = new AtomicInteger();
        AtomicInteger pdf = new AtomicInteger();
        AtomicInteger deleted = new AtomicInteger();
        HistoryPanel panel = new HistoryPanel(new HistoryViewModel(), new HistoryPanel.DragHost() {
            @Override public void dragging(BufferedImage i, Point p) { }
            @Override public boolean dropped(BufferedImage i, Point p) { return false; }
            @Override public void importIntoLibrary(HistoryItem item) { library.incrementAndGet(); }
            @Override public void importIntoPdf(HistoryItem item) { pdf.incrementAndGet(); }
            @Override public void deleteHistory(HistoryItem item) { deleted.incrementAndGet(); }
        });
        var menu = panel.contextMenu(calc());
        assertTrue(menu instanceof GlassContextMenu);
        assertEquals(3, menu.getComponentCount());
        assertEquals("Add to Library", ((JMenuItem) menu.getComponent(0)).getText());
        assertEquals("Add to PDF", ((JMenuItem) menu.getComponent(1)).getText());
        assertEquals("Delete from History", ((JMenuItem) menu.getComponent(2)).getText());
        ((JMenuItem) menu.getComponent(0)).doClick();
        ((JMenuItem) menu.getComponent(1)).doClick();
        ((JMenuItem) menu.getComponent(2)).doClick();
        assertEquals(1, library.get());
        assertEquals(1, pdf.get());
        assertEquals(1, deleted.get());
    }

    @Test void historyDragRoutesActualPointerSeparatelyFromPreviewTopLeft() {
        HistoryViewModel vm = new HistoryViewModel();
        vm.setItems(List.of(calc()));
        AtomicReference<Point> preview = new AtomicReference<>();
        AtomicReference<Point> cursor = new AtomicReference<>();
        HistoryPanel panel = new HistoryPanel(vm, new HistoryPanel.DragHost() {
            @Override public void dragging(BufferedImage i, Point p) { }
            @Override public boolean dropped(BufferedImage i, Point p) { return false; }
            @Override public boolean droppedAt(HistoryItem item, BufferedImage i, Point start, Point at) {
                preview.set(start); cursor.set(at); return true;
            }
        });
        JPanel list = (JPanel) ((JScrollPane) panel.getComponent(1)).getViewport().getView();
        var row = (javax.swing.JComponent) list.getComponent(0);
        row.setSize(300, HistoryPanel.ROW_H);
        long now = System.currentTimeMillis();
        row.dispatchEvent(new MouseEvent(row, MouseEvent.MOUSE_PRESSED, now, 0,
                75, 24, 275, 124, 1, false, MouseEvent.BUTTON1));
        row.dispatchEvent(new MouseEvent(row, MouseEvent.MOUSE_DRAGGED, now+1, 0,
                84, 28, 284, 128, 0, false, MouseEvent.BUTTON1));
        row.dispatchEvent(new MouseEvent(row, MouseEvent.MOUSE_RELEASED, now+2, 0,
                310, 80, 510, 180, 1, false, MouseEvent.BUTTON1));
        assertEquals(new Point(510, 180), cursor.get());
        assertNotNull(preview.get());
        assertFalse(preview.get().equals(cursor.get()),
                "Artwork top-left is offset from cursor; drop detection must use cursor");
    }
}
