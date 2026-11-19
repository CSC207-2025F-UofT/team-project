package view.canvas;

import history.data_access.FileHistoryRepository;
import history.domain.HistoryEntry;
import history.use_case.HistoryInteractor;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.MathLabCollector;
import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import interface_adapter.graph.GraphExploreController;
import use_case.explore_graph.ExploreGraphInteractor;
import use_case.graph_function.GraphFunctionInputBoundary;
import use_case.graph_function.GraphFunctionInteractor;
import view.DrawingCanvas;
import view.FormulaCapture;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

/** Tests Graph's real interactor/presenter/callback path, all local java, no ML model. */
class PopupGraphPublicationTest {
    // no cards in these tests
    private static final Notices QUIET = new Notices() {
        @Override public void success(String words, String latex) { }
        @Override public void error(String title, String reason) { }
        @Override public void clearError() { }
        @Override public Runnable progress(String words, String latex) { return () -> { }; }
    };

    @TempDir Path storage;

    @Test void successfulGraphPublishesItsSourceFormulaAsEditableLibraryAndHistoryMaterial() throws Exception {
        var library = new MathLibrary();
        var history = new HistoryInteractor(new FileHistoryRepository(storage));
        var collector = new MathLabCollector(library, history);
        var viewModel = new GraphViewModel();
        var presenter = new GraphPresenter(viewModel);
        var session = new InMemoryGraphSessionDataAccessObject();
        GraphFunctionInputBoundary graph = new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter);
        var shared = new SharedCanvas(new DrawingCanvas());
        shared.getDrawingCanvas().setSize(900, 500);
        CountDownLatch saved = new CountDownLatch(1);
        var popup = new PopupGraphing(shared, new GraphController(graph),
                new GraphExploreController(new ExploreGraphInteractor(session, presenter)), viewModel,
                QUIET, new JPanel(), (capture, latex) -> {
                    collector.acceptCalculation(capture.getId(), latex, "");
                    saved.countDown();
                });
        popup.graph("y=x", new Rectangle(10, 10, 60, 20),
                new FormulaCapture(42, 1, new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB)));
        assertTrue(saved.await(4, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> { });
        assertEquals(1, library.items().size());
        assertEquals("y=x", library.items().get(0).formula());
        assertFalse(library.items().get(0).isGraph());
        assertEquals(1, history.list().size());
        assertEquals(HistoryEntry.Kind.FORMULA, history.list().get(0).kind());
        assertTrue(shared.getDrawingCanvas().hasGraph(), "card is on the canvas");
        assertNotNull(shared.getDrawingCanvas().getGraphTag(), "and it's the live one");
    }

    @Test void graphFailureMustNotPersistTheFormula() throws Exception {
        var library = new MathLibrary();
        var history = new HistoryInteractor(new FileHistoryRepository(storage));
        var collector = new MathLabCollector(library, history);
        var viewModel = new GraphViewModel();
        var presenter = new GraphPresenter(viewModel);
        GraphFunctionInputBoundary graph = input -> presenter.prepareFailView("Invalid graph");
        AtomicInteger callbacks = new AtomicInteger();
        var popup = new PopupGraphing(new SharedCanvas(new DrawingCanvas()), new GraphController(graph),
                new GraphExploreController(new ExploreGraphInteractor(new InMemoryGraphSessionDataAccessObject(),
                        presenter)), viewModel, QUIET, new JPanel(), (capture, latex) -> {
                    callbacks.incrementAndGet();
                    collector.acceptCalculation(capture.getId(), latex, "");
                });
        popup.graph("y=1/0", new Rectangle(10, 10, 60, 20),
                new FormulaCapture(3, 1, new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB)));
        // Let the background graph worker and the EDT complete the failure event.
        for (int attempt = 0; attempt < 60 && viewModel.getState().getError() == null; attempt++)
            Thread.sleep(20);
        SwingUtilities.invokeAndWait(() -> { });
        assertEquals(0, callbacks.get());
        assertTrue(library.items().isEmpty());
        assertTrue(history.list().isEmpty());
    }
}
