package pdf_editor;

import entity.Stroke;
import history.domain.HistoryEntry;
import history.use_case.HistoryInteractor;
import history.use_case.HistoryRepository;
import interface_adapter.AnswerKind;
import org.junit.jupiter.api.Test;
import view.DrawingCanvas;
import view.FormulaCapture;
import view.canvas.SharedCanvas;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

/** Reproduces the interleaving where Graph saved the formula before Calculate drew its answer. */
class PdfStudioResultReconciliationTest {
    private static final class MemoryRepository implements HistoryRepository {
        private final Map<String, HistoryEntry> entries = new LinkedHashMap<>();
        @Override public boolean saveIfAbsent(HistoryEntry record) {
            if (entries.values().stream().anyMatch(old -> old.contentKey().equals(record.contentKey())))
                return false;
            entries.put(record.id(), record);
            return true;
        }
        @Override public List<HistoryEntry> findAll() { return List.copyOf(entries.values()); }
        @Override public boolean deleteById(String id) { return entries.remove(id) != null; }
    }

    private static FormulaCapture oneFormula(DrawingCanvas canvas) {
        canvas.setSize(800, 600);
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 1, 0, 80, 100, 1,
                false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 2, 0, 140, 110, 1,
                false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 3, 0, 140, 110, 1,
                false, MouseEvent.BUTTON1));
        Stroke stroke = canvas.getStrokes().get(0);
        return canvas.registerNewInk(stroke, List.of(stroke));
    }

    @Test void libraryListenersNeverSeeCalculationBeforeHistoryStorage() throws Exception {
        var history = new HistoryInteractor(new MemoryRepository());
        var library = new MathLibrary();
        var collector = new pdf_editor.adapter.MathLabCollector(library, history);
        var observed = new java.util.concurrent.atomic.AtomicBoolean();
        library.onChange(() -> {
            try {
                assertEquals(1, history.list().size(),
                        "History must be durable before the Library publishes its UI event");
                observed.set(true);
            } catch (java.io.IOException failure) {
                throw new AssertionError(failure);
            }
        });
        SwingUtilities.invokeAndWait(() -> collector.acceptCalculation(13L, "2+3", "5"));
        assertTrue(observed.get());
        assertEquals("5", history.list().get(0).result());
    }

    @Test void formulaOnlyExportThenTypesetAnswerUpgradesHistoryAndLibrary() throws Exception {
        var repo = new MemoryRepository();
        var history = new HistoryInteractor(repo);
        var studio = new PdfStudioLauncher(history);
        var canvas = new DrawingCanvas();
        var shared = new SharedCanvas(canvas);
        AtomicReference<FormulaCapture> capture = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            capture.set(oneFormula(canvas));
            assertNotNull(capture.get());
            studio.setSourceCanvas(shared);
            studio.addCalculationWithSource(capture.get().getId(), "\\frac{x^3}{2}-x^2", "");
            assertEquals(1, studio.getLibrary().items().size());
            assertTrue(studio.getLibrary().items().get(0).result().isBlank());
            assertTrue(canvas.showTypesetAnswer(capture.get().getId(), capture.get().getRevision(),
                    AnswerKind.RIGHT, "3x2/2 - 2x", "\\frac{3x^{2}}{2}-2x"));
            // The original answer exporter may omit its second callback on the same revision.
            studio.synchronizeCanvas(shared);
        });
        assertEquals(1, studio.getLibrary().items().size());
        assertEquals("\\frac{3x^{2}}{2}-2x", studio.getLibrary().items().get(0).result());
        assertEquals(1, history.list().size());
        assertEquals(HistoryEntry.Kind.CALCULATION, history.list().get(0).kind());
        assertEquals("\\frac{3x^{2}}{2}-2x", history.list().get(0).result());
        SwingUtilities.invokeAndWait(() -> studio.synchronizeCanvas(shared));
        assertEquals(1, history.list().size(), "Repeated reconciliation should not add duplicates");
    }

    @Test void normalCompletedResultUsesCanvasTypesetLatexInBothDestinations() throws Exception {
        var history = new HistoryInteractor(new MemoryRepository());
        var studio = new PdfStudioLauncher(history);
        SwingUtilities.invokeAndWait(() -> {
            var canvas = new DrawingCanvas();
            var capture = oneFormula(canvas);
            studio.setSourceCanvas(new SharedCanvas(canvas));
            assertTrue(canvas.showTypesetAnswer(capture.getId(), capture.getRevision(),
                    AnswerKind.RIGHT, "3x2/2 - 2x", "\\frac{3x^{2}}{2}-2x"));
            // The teammate's public resultText can be readable but not typeset.
            studio.addCalculationWithSource(capture.getId(), "\\frac{x^3}{2}-x^2", "3x2/2 - 2x");
        });
        assertEquals("\\frac{3x^{2}}{2}-2x", studio.getLibrary().items().get(0).result());
        assertEquals("\\frac{3x^{2}}{2}-2x", history.list().get(0).result());
    }

    @Test void periodicRecoveryFindsAnswerWithoutAnotherCanvasNotification() throws Exception {
        var history = new HistoryInteractor(new MemoryRepository());
        var studio = new PdfStudioLauncher(history);
        var latch = new java.util.concurrent.CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> {
            var canvas = new DrawingCanvas();
            var capture = oneFormula(canvas);
            studio.setSourceCanvas(new SharedCanvas(canvas));
            studio.addCalculationWithSource(capture.getId(), "2+3", "");
            studio.getLibrary().onChange(() -> {
                if (studio.getLibrary().items().stream().anyMatch(m -> "5".equals(m.result())))
                    latch.countDown();
            });
            assertTrue(canvas.showTypesetAnswer(capture.getId(), capture.getRevision(),
                    AnswerKind.RIGHT, "5", "5"));
            // Do NOT invoke synchronizeCanvas: the pending-result timer must catch it.
        });
        assertTrue(latch.await(4, java.util.concurrent.TimeUnit.SECONDS),
                "Answer delivery without a second export callback must still reach Library");
        assertEquals(1, history.list().size());
        assertEquals("5", history.list().get(0).result());
    }

    @Test void formulaOnlyWithoutAnyAnswerStaysFormulaOnly() throws Exception {
        var history = new HistoryInteractor(new MemoryRepository());
        var studio = new PdfStudioLauncher(history);
        SwingUtilities.invokeAndWait(() -> {
            var canvas = new DrawingCanvas();
            var capture = oneFormula(canvas);
            var shared = new SharedCanvas(canvas);
            studio.setSourceCanvas(shared);
            studio.addCalculationWithSource(capture.getId(), "f(x)=x^2", "");
            studio.synchronizeCanvas(shared);
        });
        assertEquals(1, history.list().size());
        assertEquals(HistoryEntry.Kind.FORMULA, history.list().get(0).kind());
        assertTrue(studio.getLibrary().items().get(0).result().isEmpty());
    }
}
