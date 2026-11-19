package view.canvas;

import data_access.InMemoryGraphSessionDataAccessObject;
import data_access.InMemoryPhaseSessionDataAccessObject;
import entity.LatexFunctionParser;
import entity.phase.DifferentialEquationParser;
import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphViewModel;
import interface_adapter.phase.PhaseController;
import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhasePresenter;
import interface_adapter.phase.PhaseViewModel;
import org.junit.jupiter.api.Test;
import use_case.explore_graph.ExploreGraphInteractor;
import use_case.explore_phase.ExplorePhaseInteractor;
import use_case.graph_function.GraphFunctionInteractor;
import use_case.phase_portrait.PhasePortraitInteractor;
import view.DrawingCanvas;
import view.FormulaCapture;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/** Phase Portrait's own path: real use case -> card on the canvas, and it takes turns w/ Graph for the slot. */
class PopupPhasePortraitTest {
    private final List<String> said = new ArrayList<>();
    private final Notices notices = new Notices() {
        @Override public void success(String words, String latex) { said.add(words + " " + latex); }
        @Override public void error(String title, String reason) { said.add(title + ": " + reason); }
        @Override public void clearError() { }
        @Override public Runnable progress(String words, String latex) { return () -> { }; }
    };

    private final SharedCanvas shared = new SharedCanvas(new DrawingCanvas());
    private final JPanel area = new JPanel(null);
    private final PhaseViewModel phaseModel = new PhaseViewModel();
    private final GraphViewModel graphModel = new GraphViewModel();

    private PopupPhasePortrait phase(List<String> saved, CountDownLatch done) {
        PhasePresenter presenter = new PhasePresenter(phaseModel);
        InMemoryPhaseSessionDataAccessObject session = new InMemoryPhaseSessionDataAccessObject();
        return new PopupPhasePortrait(shared,
                new PhaseController(new PhasePortraitInteractor(new DifferentialEquationParser(), session, presenter)),
                new PhaseExploreController(new ExplorePhaseInteractor(session, presenter)), phaseModel, notices, area,
                (capture, latex) -> {
                    saved.add(latex);
                    done.countDown();
                });
    }

    private static FormulaCapture capture(long id) {
        return new FormulaCapture(id, 1, new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB));
    }

    @Test
    void drawsThePortraitOntoTheCanvasAndSavesTheSourceFormula() throws Exception {
        shared.getDrawingCanvas().setSize(900, 500);
        List<String> saved = new ArrayList<>();
        CountDownLatch done = new CountDownLatch(1);
        PopupPhasePortrait tool = phase(saved, done);
        tool.draw("\\begin{matrix}x'=y\\\\y'=-x\\end{matrix}", new Rectangle(10, 10, 60, 40), capture(7));
        assertTrue(done.await(4, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> { });
        assertEquals(List.of("\\begin{matrix}x'=y\\\\y'=-x\\end{matrix}"), saved, "source latex, not the title");
        assertTrue(shared.getDrawingCanvas().hasGraph());
        assertTrue(tool.card().isVisible());
        assertTrue(said.contains("Phase portrait of x'=y,\\quad y'=-x"), said.toString());
    }

    @Test
    void failureSaysWhyAndSavesNothing() throws Exception {
        List<String> saved = new ArrayList<>();
        PopupPhasePortrait tool = phase(saved, new CountDownLatch(1));
        tool.draw("y''=x", new Rectangle(10, 10, 60, 20), capture(8));
        for (int i = 0; i < 100 && phaseModel.getState().getError() == null; i++) {
            Thread.sleep(20);
        }
        SwingUtilities.invokeAndWait(() -> { });
        assertTrue(saved.isEmpty());
        assertFalse(shared.getDrawingCanvas().hasGraph());
        assertTrue(said.get(0).startsWith("Can't draw the phase portrait"), said.toString());
    }

    @Test
    void graphAndPhaseTakeTurnsWithTheOneCardSlot() throws Exception {
        shared.getDrawingCanvas().setSize(900, 500);
        CountDownLatch phaseDone = new CountDownLatch(1);
        PopupPhasePortrait phase = phase(new ArrayList<>(), phaseDone);
        GraphPresenter gp = new GraphPresenter(graphModel);
        InMemoryGraphSessionDataAccessObject gs = new InMemoryGraphSessionDataAccessObject();
        CountDownLatch graphDone = new CountDownLatch(1);
        PopupGraphing graph = new PopupGraphing(shared,
                new GraphController(new GraphFunctionInteractor(new LatexFunctionParser(), gs, gp)),
                new GraphExploreController(new ExploreGraphInteractor(gs, gp)), graphModel, notices, area,
                (c, l) -> graphDone.countDown());
        graph.setOnShown(phase::placeRanges);
        phase.setOnShown(graph::placeRanges);

        graph.graph("y=x^2", new Rectangle(10, 10, 60, 20), capture(1));
        assertTrue(graphDone.await(4, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> { });
        assertTrue(graph.graphCard().isVisible());

        phase.draw("y'=x-y", new Rectangle(10, 200, 60, 20), capture(2));
        assertTrue(phaseDone.await(4, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> { });
        assertTrue(phase.card().isVisible());
        assertFalse(graph.graphCard().isVisible(), "graph card stepped aside");
    }
}
