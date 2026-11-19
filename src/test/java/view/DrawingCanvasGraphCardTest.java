package view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The graph card: shown beside the formula, never over the whole canvas, closable. */
class DrawingCanvasGraphCardTest {
    private static final BufferedImage CHART = new BufferedImage(1600, 1000, BufferedImage.TYPE_INT_RGB);

    private final DrawingCanvas canvas = new DrawingCanvas();

    @BeforeEach
    void sizeTheCanvas() {
        canvas.setSize(900, 500);
    }

    private void drag(int... xy) {
        send(MouseEvent.MOUSE_PRESSED, xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) {
            send(MouseEvent.MOUSE_DRAGGED, xy[i], xy[i + 1]);
        }
        send(MouseEvent.MOUSE_RELEASED, xy[xy.length - 2], xy[xy.length - 1]);
    }

    private void send(int id, int x, int y) {
        canvas.dispatchEvent(new MouseEvent(canvas, id, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }

    @Test
    void cardSitsRightOfTheFormulaAndDoesNotFillTheCanvas() {
        drag(100, 200, 200, 250);
        canvas.showGraph(CHART);

        assertTrue(canvas.hasGraph());
        Rectangle2D card = canvas.getGraphCardOnScreen();
        assertTrue(card.getMinX() >= 200 + 40, "to the right of the ink, with a gap");
        assertEquals(0.4 * 900 + 20, card.getWidth(), 1, "40% of the canvas width plus its white border");
        assertTrue(card.getWidth() < 900 / 2.0 && card.getHeight() < 500, "never the whole canvas");
        assertEquals(1000.0 / 1600, (card.getHeight() - 20) / (card.getWidth() - 20), 0.01, "the chart keeps its shape");
        assertTrue(card.getMinY() >= 0 && card.getMaxY() <= 500, "the card is in view");
    }

    @Test
    void cardGoesBesideTheFormulaSelectedWithTheLasso() {
        drag(100, 100, 200, 130);   // formula A
        drag(100, 300, 250, 330);   // formula B
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        drag(80, 280, 270, 280, 270, 350, 80, 350);   // circle B

        canvas.showGraph(CHART);

        Rectangle2D card = canvas.getGraphCardOnScreen();
        assertTrue(card.getMinX() >= 250 + 40, "right of B");
        assertEquals(315, card.getCenterY(), 2, "level with B, not with all the ink");
    }

    @Test
    void cardGoesBesideTheFormulaItIsGiven() {
        drag(100, 100, 200, 130);
        canvas.showGraph(CHART, new java.awt.Rectangle(400, 200, 50, 50));
        Rectangle2D card = canvas.getGraphCardOnScreen();
        assertTrue(card.getMinX() >= 450 + 40);
        assertEquals(225, card.getCenterY(), 2);
    }

    @Test
    void viewMovesSoACardNearTheTopIsFullyVisible() {
        drag(100, 20, 200, 40);   // formula at the very top: a centred card would stick out above
        canvas.showGraph(CHART);
        Rectangle2D card = canvas.getGraphCardOnScreen();
        assertTrue(card.getMinY() >= 59, "moved down, clear of the floating controls");
    }

    @Test
    void cardMovesWithTheView() {
        drag(100, 200, 200, 250);
        canvas.showGraph(CHART);
        double before = canvas.getGraphCardOnScreen().getMinX();
        canvas.panBy(30, 0);
        assertEquals(before + 30, canvas.getGraphCardOnScreen().getMinX(), 1e-9);
    }

    @Test
    void cardIsNotPartOfTheFormulaImage() {
        drag(100, 200, 200, 250);
        BufferedImage before = canvas.toFormulaImage();
        canvas.showGraph(CHART);
        BufferedImage after = canvas.toFormulaImage();
        assertEquals(before.getWidth(), after.getWidth());
        assertEquals(before.getHeight(), after.getHeight());
    }

    @Test
    void closeButtonRemovesTheCardWithoutDrawing() {
        drag(100, 200, 200, 250);
        canvas.showGraph(CHART);
        Point2D close = canvas.getGraphCloseOnScreen();

        drag((int) close.getX(), (int) close.getY(), (int) close.getX() + 5, (int) close.getY() + 5);

        assertFalse(canvas.hasGraph());
        assertNull(canvas.getGraphCardOnScreen());
        assertEquals(1, canvas.getStrokes().size(), "clicking × drew nothing");
    }

    @Test
    void withNoInkTheCardIsCentred() {
        canvas.showGraph(CHART);
        Rectangle2D card = canvas.getGraphCardOnScreen();
        assertEquals(450, card.getCenterX(), 1);
        assertEquals(250, card.getCenterY(), 1);
    }

    @Test
    void cardCanBeMovedAndResizedOnScreen() {
        canvas.showGraph(CHART, new java.awt.Rectangle(100, 100, 50, 50), "live");
        Rectangle2D before = canvas.getGraphCardOnScreen();
        canvas.moveGraphBy(30, -20);
        Rectangle2D after = canvas.getGraphCardOnScreen();
        assertEquals(before.getX() + 30, after.getX(), 1e-6);
        assertEquals(before.getY() - 20, after.getY(), 1e-6);
        canvas.resizeGraphTo(500, 320);
        assertEquals(500, canvas.getGraphCardOnScreen().getWidth(), 1e-6);
        assertEquals(after.getX(), canvas.getGraphCardOnScreen().getX(), 1e-6, "top-left stays");
        assertEquals("live", canvas.getGraphTag());
    }

    @Test
    void tagGoesAwayWithTheCard() {
        canvas.showGraph(CHART, new java.awt.Rectangle(100, 100, 50, 50), "live");
        canvas.removeGraph();
        assertNull(canvas.getGraphTag());
        canvas.showGraph(CHART);
        assertNull(canvas.getGraphTag(), "plain picture has no owner");
    }

    // ---- several graphs at once

    @Test
    void severalGraphsStayOnTheCanvasTogether() {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        assertEquals(2, canvas.graphCount());
        assertTrue(canvas.hasGraph("first") && canvas.hasGraph("second"));
        assertEquals("second", canvas.getGraphTag(), "newest on top");
        assertFalse(canvas.getGraphCardOnScreen("first").intersects(canvas.getGraphCardOnScreen("second")));
    }

    @Test
    void showingAgainWithTheSameTagReplacesOnlyThatCard() {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        assertEquals(2, canvas.graphCount());
    }

    @Test
    void secondGraphOfTheSameFormulaDoesNotCoverTheFirst() {
        java.awt.Rectangle formula = new java.awt.Rectangle(60, 100, 50, 30);
        canvas.showGraph(CHART, formula, "first");
        canvas.showGraph(CHART, formula, "second");
        Rectangle2D first = canvas.getGraphCardOnScreen("first");
        Rectangle2D second = canvas.getGraphCardOnScreen("second");
        assertFalse(first.intersects(second), first + " vs " + second);
        assertEquals(first.getX(), second.getX(), 1e-6, "stacked under it, same column");
    }

    @Test
    void eachCardMovesResizesAndClosesOnItsOwn() {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        Rectangle2D secondBefore = canvas.getGraphCardOnScreen("second");
        Rectangle2D firstBefore = canvas.getGraphCardOnScreen("first");
        canvas.moveGraphBy("first", 25, 10);
        canvas.resizeGraphTo("first", 300, 200);
        assertEquals(firstBefore.getX() + 25, canvas.getGraphCardOnScreen("first").getX(), 1e-6);
        assertEquals(300, canvas.getGraphCardOnScreen("first").getWidth(), 1e-6);
        assertEquals(secondBefore, canvas.getGraphCardOnScreen("second"), "the other card did not move");

        canvas.removeGraph("first");
        assertFalse(canvas.hasGraph("first"));
        assertTrue(canvas.hasGraph("second"));
    }

    @Test
    void closeButtonClosesOnlyTheCardItBelongsTo() {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        Rectangle2D first = canvas.getGraphCardOnScreen("first");
        int x = (int) (first.getMaxX() - 16);
        int y = (int) (first.getMinY() + 16);
        drag(x, y, x + 2, y + 2);
        assertFalse(canvas.hasGraph("first"));
        assertTrue(canvas.hasGraph("second"));
    }

    @Test
    void undoAndClearCoverEveryGraph() {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        canvas.clear();
        assertEquals(0, canvas.graphCount());
        canvas.undo();
        assertEquals(2, canvas.graphCount(), "undo brings both back");
        assertTrue(canvas.hasGraph("first") && canvas.hasGraph("second"));
    }

    @Test
    void saveAndOpenKeepEveryGraph() throws Exception {
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 60, 50, 30), "first");
        canvas.showGraph(CHART, new java.awt.Rectangle(60, 300, 50, 30), "second");
        var state = canvas.exportCanvasState();
        assertEquals(2, state.assets().stream().filter(a -> a.graph()).count());

        DrawingCanvas reopened = new DrawingCanvas();
        reopened.setSize(900, 500);
        reopened.restoreCanvasState(state);
        assertEquals(2, reopened.graphCount());
        assertNull(reopened.getGraphTag(), "reopened graphs are plain pictures");
    }
}
