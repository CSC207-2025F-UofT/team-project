package view.board;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/** A History graph's drag payload must never be replaced by its tiny row thumbnail. */
class HistoryGraphResolutionRegressionTest {
    @Test
    void graphDragUsesOriginalFullResolutionPixels() throws Exception {
        BufferedImage source = new BufferedImage(900, 600, BufferedImage.TYPE_INT_ARGB);
        source.setRGB(800, 500, 0xff1976d2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(source, "png", out);
        HistoryEntry entry = new HistoryEntry("graph-1", HistoryEntry.Kind.GRAPH,
                "Graph", "y=x", "", out.toByteArray(), 900, 600, Instant.now());
        BufferedImage dragPayload = HistoryPanel.picture(new HistoryItem(entry));
        assertEquals(900, dragPayload.getWidth());
        assertEquals(600, dragPayload.getHeight());
        assertEquals(0xff1976d2, dragPayload.getRGB(800, 500));
    }

    @Test
    void historyPanelAndToolPopupHaveTransparentBackgrounds() {
        HistoryPanel panel = new HistoryPanel(new interface_adapter.history.HistoryViewModel(),
                new HistoryPanel.DragHost() {
                    @Override public void dragging(BufferedImage picture, java.awt.Point p) { }
                    @Override public boolean dropped(BufferedImage picture, java.awt.Point p) { return false; }
                });
        assertTrue(!panel.isOpaque(), "History background should not be a solid white panel");
        ToolOptionsPopup popup = new ToolOptionsPopup(new view.DrawingCanvas(), c -> { });
        assertTrue(!popup.isOpaque(), "macOS must not see a native rectangular popup background");
        assertEquals(0, popup.getBackground().getAlpha());
        assertTrue(popup.isLightWeightPopupEnabled());
    }

    @Test
    void toolbarMarginsDoNotPaintDarkDropShadow() {
        GlassBar bar = new GlassBar(BoardTheme.font(14), () -> new java.awt.Point(), g -> {}, () -> 1L);
        bar.action("Save", Icons::save, () -> {}, () -> true);
        bar.finish();
        BufferedImage surface = new BufferedImage(bar.getWidth(), bar.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = surface.createGraphics();
        try { bar.paint(g); } finally { g.dispose(); }
        assertEquals(0, surface.getRGB(4, 4) >>> 24,
                "The toolbar's outer margin must be fully transparent on Windows and macOS");
    }
}
