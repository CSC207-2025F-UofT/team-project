package view.graph;

import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphViewModel;
import org.junit.jupiter.api.Test;
import use_case.explore_graph.ExploreGraphInteractor;
import use_case.graph_function.GraphFunctionInputData;
import use_case.graph_function.GraphFunctionInteractor;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// the live card: mouse in, use cases + host calls out
class GraphCardTest {
    private final GraphViewModel viewModel = new GraphViewModel();
    private final InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
    private final GraphPresenter presenter = new GraphPresenter(viewModel);
    private final GraphCard card = new GraphCard(viewModel,
            new GraphExploreController(new ExploreGraphInteractor(session, presenter)));
    private final List<String> host = new ArrayList<>();
    private double movedX;
    private double movedY;

    GraphCardTest() {
        card.setSize(440, 300);
        card.setHost(new GraphCard.Host() {
            @Override
            public void moveBy(double dx, double dy) {
                movedX += dx;
                movedY += dy;
                host.add("move");
            }

            @Override
            public void resizeTo(double width, double height) {
                host.add("resize " + (int) width + "x" + (int) height);
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

    private void graph(String latex) throws Exception {
        new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter)
                .execute(new GraphFunctionInputData(latex, -10, 10, -10, 10));
        SwingUtilities.invokeAndWait(() -> { });   // card hops to the edt for STATE
    }

    // presses at the first point, drags through the rest; screen = component coords here
    private void drag(int... xy) {
        send(MouseEvent.MOUSE_PRESSED, xy[0], xy[1], 1);
        for (int i = 2; i < xy.length; i += 2) {
            send(MouseEvent.MOUSE_DRAGGED, xy[i], xy[i + 1], 1);
        }
        send(MouseEvent.MOUSE_RELEASED, xy[xy.length - 2], xy[xy.length - 1], 1);
    }

    private void send(int id, int x, int y, int clicks) {
        card.dispatchEvent(new MouseEvent(card, id, 0, 0, x, y, x, y, clicks, false, MouseEvent.BUTTON1));
    }

    @Test
    void draggingTheHeaderMovesTheCard() throws Exception {
        graph("y=x");
        drag(100, 15, 130, 25, 160, 55);
        assertEquals(60, movedX, 1e-9);
        assertEquals(40, movedY, 1e-9);
        assertTrue(host.contains("view"));
    }

    @Test
    void draggingThePlotPansTheGraph() throws Exception {
        graph("y=x");
        GraphState before = viewModel.getState();
        drag(200, 150, 244, 150);   // 44 px right = 2 units on a 20 wide / 440 px window
        GraphState after = viewModel.getState();
        assertEquals(before.getXMin() - 2, after.getXMin(), 1e-9);
        assertEquals(before.getYMin(), after.getYMin(), 1e-9);
        assertTrue(movedX == 0 && movedY == 0, "the card itself stays put");
    }

    @Test
    void wheelZoomsAroundTheCursor() throws Exception {
        graph("y=x");
        double width = viewModel.getState().getXMax() - viewModel.getState().getXMin();
        card.dispatchEvent(new MouseWheelEvent(card, MouseEvent.MOUSE_WHEEL, 0, 0, 220, 150, 220, 150, 0, false,
                MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -3));
        assertTrue(viewModel.getState().getXMax() - viewModel.getState().getXMin() < width);
    }

    @Test
    void hoverReadsTheCurveAndClickPinsIt() throws Exception {
        graph("y=x^2");
        send(MouseEvent.MOUSE_MOVED, 330, 200, 0);
        assertNotNull(viewModel.getState().getTrace());
        double x = viewModel.getState().getTrace().getX();
        assertEquals(x * x, viewModel.getState().getTrace().getY(), 1e-9, "the real f(x), not the mouse y");
        drag(330, 200, 330, 200);
        assertNotNull(card.pinned());
        send(MouseEvent.MOUSE_EXITED, 500, 500, 0);
        assertNull(viewModel.getState().getTrace());
        assertNotNull(card.pinned(), "pin stays when the cursor leaves");
    }

    @Test
    void cornerResizesAndXCloses() throws Exception {
        graph("y=x");
        drag(434, 294, 474, 334);
        assertTrue(host.stream().anyMatch(s -> s.startsWith("resize 480x340")), host.toString());
        java.awt.geom.Rectangle2D close = GraphCardRenderer.closeButton(440);
        drag((int) close.getCenterX(), (int) close.getCenterY());
        assertTrue(host.contains("close"));
    }

    @Test
    void surfaceDragSpinsTheCameraNotTheWindow() throws Exception {
        graph("z=x^2+y^2");
        double yaw = card.camera().yaw();
        GraphState before = viewModel.getState();
        drag(200, 150, 260, 170);
        assertNotEquals(yaw, card.camera().yaw());
        GraphState after = viewModel.getState();
        assertSame(before.getZ(), after.getZ(), "rotating is view-only, no resample");
        assertEquals(before.getXMin(), after.getXMin(), 1e-12);
    }

    @Test
    void snapshotDrawsSomething() throws Exception {
        graph("y=\\sin x");
        BufferedImage img = card.snapshot(440, 300);
        int orange = 0;
        for (int x = 0; x < img.getWidth(); x += 2) {
            for (int y = 40; y < img.getHeight(); y += 2) {
                int rgb = img.getRGB(x, y);
                if (((rgb >> 16) & 0xff) > 230 && ((rgb >> 8) & 0xff) > 120 && ((rgb >> 8) & 0xff) < 180
                        && (rgb & 0xff) < 60) {
                    orange++;
                }
            }
        }
        assertTrue(orange > 50, "curve pixels drawn: " + orange);
    }
}
