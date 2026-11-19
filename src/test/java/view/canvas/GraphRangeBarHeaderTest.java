package view.canvas;

import data_access.InMemoryGraphSessionDataAccessObject;
import entity.LatexFunctionParser;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphPresenter;
import interface_adapter.graph.GraphViewModel;
import org.junit.jupiter.api.Test;
import use_case.explore_graph.ExploreGraphInteractor;
import use_case.graph_function.GraphFunctionInputData;
import use_case.graph_function.GraphFunctionInteractor;
import view.graph.GraphCard;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

// the x / y boxes live in the card header beside the function, not under the card
class GraphRangeBarHeaderTest {
    private final GraphViewModel viewModel = new GraphViewModel();
    private final InMemoryGraphSessionDataAccessObject session = new InMemoryGraphSessionDataAccessObject();
    private final GraphPresenter presenter = new GraphPresenter(viewModel);
    private final GraphCard card = new GraphCard(viewModel,
            new GraphExploreController(new ExploreGraphInteractor(session, presenter)));
    private final GraphRangeBar bar = new GraphRangeBar(() -> { });

    private void graph(String latex) throws Exception {
        new GraphFunctionInteractor(new LatexFunctionParser(), session, presenter)
                .execute(new GraphFunctionInputData(latex, -10, 10, -10, 10));
        SwingUtilities.invokeAndWait(() -> { });
    }

    private Rectangle slotFor(int width, int height) {
        card.setSize(width, height);
        return bar.placeIn(card);
    }

    @Test
    void boxesSitInsideTheHeaderAfterTheFunction() throws Exception {
        graph("x^2");
        Rectangle slot = slotFor(440, 300);
        assertNotNull(slot, "a normal card has room in its header");
        double header = 34 * card.headerScale();
        assertTrue(slot.y >= 0 && slot.y + slot.height <= header + 0.5, "vertically inside the header: " + slot);
        assertTrue(slot.x > 30, "after the dot and the function, not over them: " + slot);
        assertTrue(slot.x + slot.width < card.getWidth() - 60, "clear of the badge and buttons: " + slot);
    }

    @Test
    void boxesScaleWithTheCard() throws Exception {
        graph("x^2");
        Dimension small = new Dimension(slotFor(440, 300).getSize());
        Dimension big = new Dimension(slotFor(880, 600).getSize());
        assertTrue(big.height > small.height && big.width > small.width, small + " vs " + big);
    }

    @Test
    void narrowCardGivesTheHeaderBackToTheTitle() throws Exception {
        graph("\\sin(x)+\\cos(x)+x^3-2x");
        assertNull(slotFor(240, 180), "no room: the boxes go under the card instead");
    }

    @Test
    void longFunctionStillShowsBesideTheBoxes() throws Exception {
        graph("\\sin(x)+\\cos(x)+x^3-2x+\\frac{1}{x^2+1}+\\sqrt{x^2+4}");
        Rectangle slot = slotFor(440, 300);
        if (slot != null) {
            assertTrue(slot.x - 30 >= 90 * card.headerScale() - 1, "title keeps a readable width: " + slot);
        }
    }
}
