package view.canvas;

import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerPopupGlassTest {
    @Test
    void invalidFormulaPopupRetainsTheReasonAndDisablesActions() {
        AnswerPopup popup = new AnswerPopup(g -> { });
        popup.showProblem("Invalid formula — a has no value");
        assertEquals("Invalid formula — a has no value", popup.getProblem());
        assertTrue(!popup.hasCalculate());
        assertTrue(!popup.hasGraph());
        assertTrue(popup.getPreferredSize().width > 160);
    }

    @Test
    void invalidFormulaPillCanPaintAtItsPreferredSize() {
        AnswerPopup popup = new AnswerPopup(g -> { });
        popup.showProblem("Invalid formula — a has no value");
        popup.setVisible(true);
        popup.setSize(popup.getPreferredSize());
        popup.doLayout();
        BufferedImage image = new BufferedImage(popup.getWidth(), popup.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        popup.paint(g);
        g.dispose();
        assertTrue((image.getRGB(20, 15) >>> 24) > 0);
    }
}
