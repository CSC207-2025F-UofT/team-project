package view.graph;

import data_access.InMemoryPhaseSessionDataAccessObject;
import entity.phase.DifferentialEquationParser;
import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhasePresenter;
import interface_adapter.phase.PhaseReadout;
import interface_adapter.phase.PhaseState;
import interface_adapter.phase.PhaseViewModel;
import org.junit.jupiter.api.Test;
import use_case.explore_phase.ExplorePhaseInteractor;
import use_case.phase_portrait.PhasePortraitInputData;
import use_case.phase_portrait.PhasePortraitInteractor;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// the live phase card: same shell as the graph card, hover follows solutions, click pins them
class PhaseCardTest {
    private final PhaseViewModel viewModel = new PhaseViewModel();
    private final InMemoryPhaseSessionDataAccessObject session = new InMemoryPhaseSessionDataAccessObject();
    private final PhasePresenter presenter = new PhasePresenter(viewModel);
    private final PhaseCard card = new PhaseCard(viewModel,
            new PhaseExploreController(new ExplorePhaseInteractor(session, presenter)));
    private final List<String> host = new ArrayList<>();

    PhaseCardTest() {
        card.setSize(440, 300);
        card.setHost(new PlotCard.Host() {
            @Override
            public void moveBy(double dx, double dy) {
                host.add("move");
            }

            @Override
            public void resizeTo(double width, double height) {
                host.add("resize");
            }

            @Override
            public void close() {
                host.add("close");
            }

            @Override
            public void viewChanged() {
                host.add("view");
            }
        });
    }

    private void draw(String latex) throws Exception {
        new PhasePortraitInteractor(new DifferentialEquationParser(), session, presenter)
                .execute(new PhasePortraitInputData(latex, -5, 5, -4, 4));
        SwingUtilities.invokeAndWait(() -> { });
    }

    private void send(int id, int x, int y, int clicks) {
        card.dispatchEvent(new MouseEvent(card, id, 0, 0, x, y, x, y, clicks, false, MouseEvent.BUTTON1));
    }

    // world -> card pixel
    private int[] at(double h, double v) {
        Rectangle2D plot = GraphCardRenderer.plotArea(card.getWidth(), card.getHeight());
        PhaseState s = viewModel.getState();
        PlotFrame f = new PlotFrame(s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax(), plot);
        return new int[]{(int) Math.round(f.px(h)), (int) Math.round(f.py(v))};
    }

    @Test
    void headerDragMovesTheCard() throws Exception {
        draw("x'=y\\\\y'=-x");
        send(MouseEvent.MOUSE_PRESSED, 100, 10, 1);
        send(MouseEvent.MOUSE_DRAGGED, 140, 30, 1);
        send(MouseEvent.MOUSE_RELEASED, 140, 30, 1);
        assertTrue(host.contains("move"));
        assertEquals(-5, viewModel.getState().getXMin(), 1e-12, "window didn't pan");
    }

    @Test
    void plotDragPansAndWheelZooms() throws Exception {
        draw("x'=y\\\\y'=-x");
        send(MouseEvent.MOUSE_PRESSED, 200, 150, 1);
        send(MouseEvent.MOUSE_DRAGGED, 244, 150, 1);
        send(MouseEvent.MOUSE_RELEASED, 244, 150, 1);
        assertTrue(viewModel.getState().getXMin() < -5.5, "dragged right = looking further left");
        double span = viewModel.getState().getXMax() - viewModel.getState().getXMin();
        card.dispatchEvent(new MouseWheelEvent(card, MouseEvent.MOUSE_WHEEL, 0, 0, 200, 150, 200, 150, 0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -3));
        assertTrue(viewModel.getState().getXMax() - viewModel.getState().getXMin() < span);
    }

    @Test
    void hoverReadsTheFieldAndSnapsOntoTheCenter() throws Exception {
        draw("x'=y\\\\y'=-x");
        int[] p = at(2, 1);
        send(MouseEvent.MOUSE_MOVED, p[0], p[1], 0);
        PhaseReadout r = viewModel.getState().getTrace();
        assertNotNull(r);
        assertEquals(-r.getH(), r.getDv(), 1e-12);   // y' = -x, at the real point
        assertFalse(r.getPath().isEmpty());
        int[] o = at(0.05, 0.05);
        send(MouseEvent.MOUSE_MOVED, o[0], o[1], 0);
        assertEquals("Center", viewModel.getState().getTrace().getCaption());
        assertEquals("(0, 0)", viewModel.getState().getTrace().getText());
        send(MouseEvent.MOUSE_EXITED, 0, 0, 0);
        assertNull(viewModel.getState().getTrace());
    }

    @Test
    void clickPinsASolutionClickAgainDropsIt() throws Exception {
        draw("x'=y\\\\y'=-x");
        int[] p = at(2, 0);
        send(MouseEvent.MOUSE_PRESSED, p[0], p[1], 1);
        send(MouseEvent.MOUSE_RELEASED, p[0], p[1], 1);
        assertEquals(1, viewModel.getState().getSolutions().size());
        assertTrue(host.contains("view"), "snapshot gets redone");
        int[] q = at(0, -2);   // same circle, other side
        send(MouseEvent.MOUSE_PRESSED, q[0], q[1], 1);
        send(MouseEvent.MOUSE_RELEASED, q[0], q[1], 1);
        assertTrue(viewModel.getState().getSolutions().isEmpty());
    }

    @Test
    void closeAndResetButtons() throws Exception {
        draw("y'=y(1-y)");
        int[] p = at(1, 0.5);
        send(MouseEvent.MOUSE_PRESSED, p[0], p[1], 1);
        send(MouseEvent.MOUSE_RELEASED, p[0], p[1], 1);
        Rectangle2D reset = GraphCardRenderer.resetButton(card.getWidth());
        send(MouseEvent.MOUSE_PRESSED, (int) reset.getCenterX(), (int) reset.getCenterY(), 1);
        assertTrue(viewModel.getState().getSolutions().isEmpty());
        Rectangle2D close = GraphCardRenderer.closeButton(card.getWidth());
        send(MouseEvent.MOUSE_PRESSED, (int) close.getCenterX(), (int) close.getCenterY(), 1);
        assertTrue(host.contains("close"));
    }

    @Test
    void snapshotPaintsTheCard() throws Exception {
        draw("\\theta''=-\\sin\\theta");
        BufferedImage img = card.snapshot(440, 300);
        assertEquals(440, img.getWidth());
        int painted = 0;
        for (int x = 20; x < 420; x += 7) {
            for (int y = 60; y < 280; y += 7) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 255;
                int b = rgb & 255;
                if (b > r + 30) {
                    painted++;   // bluish arrows
                }
            }
        }
        assertTrue(painted > 30, "arrows drawn: " + painted);
    }
}
