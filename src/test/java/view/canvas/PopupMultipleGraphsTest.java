package view.canvas;

import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphViewModel;
import org.junit.jupiter.api.Test;
import use_case.explore_graph.ExploreGraphInteractor;
import use_case.graph_function.GraphFunctionInteractor;
import view.DrawingCanvas;
import view.FormulaCapture;
import view.graph.PlotCard;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Each graph gets its own session, live card and range boxes, so several stay on the canvas. */
class PopupMultipleGraphsTest {
    private static final Notices QUIET = new Notices() {
        @Override public void success(String words, String latex) { }
        @Override public void error(String title, String reason) { }
        @Override public void clearError() { }
        @Override public Runnable progress(String words, String latex) { return () -> { }; }
    };

    private final SharedCanvas shared = new SharedCanvas(new DrawingCanvas());
    private final JPanel area = new JPanel(null);
    private final AtomicInteger sessions = new AtomicInteger();
    private final List<String> shown = new ArrayList<>();
    private CountDownLatch done;

    private PopupGraphing tool() {
        shared.getDrawingCanvas().setSize(900, 600);
        area.setSize(900, 600);
        return new PopupGraphing(shared, () -> {
            sessions.incrementAndGet();
            GraphViewModel viewModel = new GraphViewModel();
            InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
            GraphPresenter presenter = new GraphPresenter(viewModel);
            return new GraphSession(
                    new GraphController(new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter)),
                    new GraphExploreController(new ExploreGraphInteractor(session, presenter)), viewModel);
        }, QUIET, area, (capture, latex) -> {
            shown.add(latex);
            done.countDown();
        });
    }

    private void graph(PopupGraphing tool, String latex, int y, long id) throws Exception {
        done = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> tool.graph(latex, new Rectangle(40, y, 80, 30),
                new FormulaCapture(id, 1, new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB))));
        assertTrue(done.await(5, TimeUnit.SECONDS), "graph of " + latex + " came back");
        SwingUtilities.invokeAndWait(() -> { });
    }

    private List<PlotCard> visibleCards() {
        List<PlotCard> cards = new ArrayList<>();
        for (Component c : area.getComponents()) {
            if (c instanceof PlotCard card && card.isVisible()) {
                cards.add(card);
            }
        }
        return cards;
    }

    @Test
    void twoGraphsAreLiveAtTheSameTime() throws Exception {
        PopupGraphing tool = tool();
        graph(tool, "x^2", 40, 1);
        graph(tool, "\\sin(x)", 360, 2);

        assertEquals(List.of("x^2", "\\sin(x)"), shown);
        assertEquals(2, shared.getDrawingCanvas().graphCount(), "both graphs are on the canvas");
        assertEquals(2, sessions.get(), "each graph has its own session");
        List<PlotCard> cards = visibleCards();
        assertEquals(2, cards.size(), "both cards are live, not pictures");
        assertFalse(cards.get(0).getBounds().intersects(cards.get(1).getBounds()));
    }

    @Test
    void closingOneGraphKeepsTheOtherAndItsCardIsReused() throws Exception {
        PopupGraphing tool = tool();
        graph(tool, "x^2", 40, 1);
        graph(tool, "\\sin(x)", 360, 2);
        Object second = shared.getDrawingCanvas().getGraphTag();

        SwingUtilities.invokeAndWait(() -> {
            shared.getDrawingCanvas().removeGraph(second);
            tool.placeRanges();
        });
        assertEquals(1, shared.getDrawingCanvas().graphCount());
        assertEquals(1, visibleCards().size());

        graph(tool, "x^3", 360, 3);
        assertEquals(2, shared.getDrawingCanvas().graphCount());
        assertEquals(2, tool.liveCount(), "the closed card was reused, no new one");
        assertEquals(2, sessions.get(), "and so was its session");
    }

    @Test
    void oneSharedSessionStillMeansOneCardThatIsReplaced() throws Exception {
        shared.getDrawingCanvas().setSize(900, 600);
        GraphViewModel viewModel = new GraphViewModel();
        InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
        GraphPresenter presenter = new GraphPresenter(viewModel);
        PopupGraphing tool = new PopupGraphing(shared,
                new GraphController(new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter)),
                new GraphExploreController(new ExploreGraphInteractor(session, presenter)), viewModel, QUIET, area,
                (capture, latex) -> done.countDown());
        graph(tool, "x^2", 40, 1);
        graph(tool, "x^3", 360, 2);
        assertEquals(1, shared.getDrawingCanvas().graphCount());
        assertEquals(1, tool.liveCount());
    }
}
