package view.board;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import interface_adapter.history.HistoryViewModel;
import org.junit.jupiter.api.Test;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** If the mouse is released on Canvas instead of the dragged row, no ghost may remain. */
class HistoryCrossComponentDragReleaseTest {
    @Test void outsideReleaseDropsExactlyOnceAndClearsPreview() throws Exception {
        HistoryViewModel vm = new HistoryViewModel();
        vm.setItems(List.of(new HistoryItem(new HistoryEntry("history-1", HistoryEntry.Kind.FORMULA,
                "Formula", "x+1", "", null, 0, 0, Instant.now()))));
        AtomicInteger drops = new AtomicInteger();
        AtomicInteger cancels = new AtomicInteger();
        HistoryPanel panel = new HistoryPanel(vm, new HistoryPanel.DragHost() {
            @Override public void dragging(BufferedImage image, Point point) { }
            @Override public boolean dropped(BufferedImage image, Point point) { return false; }
            @Override public boolean dropped(HistoryItem item, BufferedImage image, Point point) {
                drops.incrementAndGet();
                return true;
            }
            @Override public void cancelDrag() { cancels.incrementAndGet(); }
        });
        JScrollPane scroll = (JScrollPane) panel.getComponent(1);
        JPanel list = (JPanel) scroll.getViewport().getView();
        JComponent row = (JComponent) list.getComponent(0);
        row.setSize(300, HistoryPanel.ROW_H);
        long now = System.currentTimeMillis();
        // Explicit absolute coordinates keep synthetic desktop events valid without a native window.
        row.dispatchEvent(new MouseEvent(row, MouseEvent.MOUSE_PRESSED, now, 0,
                55, 24, 155, 124, 1, false, MouseEvent.BUTTON1));
        row.dispatchEvent(new MouseEvent(row, MouseEvent.MOUSE_DRAGGED, now + 1, 0,
                80, 34, 180, 134, 0, false, MouseEvent.BUTTON1));
        Field watcher = row.getClass().getDeclaredField("releaseWatcher");
        watcher.setAccessible(true);
        AWTEventListener listener = (AWTEventListener) watcher.get(row);
        assertNotNull(listener, "History starts watching releases outside its own row");
        JPanel canvasTarget = new JPanel();
        listener.eventDispatched(new MouseEvent(canvasTarget, MouseEvent.MOUSE_RELEASED, now + 2, 0,
                300, 260, 400, 360, 1, false, MouseEvent.BUTTON1));
        SwingUtilities.invokeAndWait(() -> { });
        assertEquals(1, drops.get(), "A drop outside the row should still complete once");
        assertTrue(cancels.get() >= 1, "The translucent ghost must be cleared");
        assertNull(watcher.get(row), "The global mouse listener must be removed after release");
    }
}
