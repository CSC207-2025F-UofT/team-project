package view.board;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class HistoryTransparentGraphArtworkTest {
    @Test void historyGraphDragKeepsOriginalResolutionButDropsWhitePaper() throws Exception {
        BufferedImage chart = new BufferedImage(320, 160, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 160; y++) for (int x = 0; x < 320; x++)
            chart.setRGB(x,y,Color.WHITE.getRGB());
        chart.setRGB(130, 70, Color.BLUE.getRGB());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(chart, "png", bytes);
        HistoryItem item = new HistoryItem(new HistoryEntry("plot1", HistoryEntry.Kind.GRAPH,
                "Plot", "y=x^2", "", bytes.toByteArray(), 320,160,Instant.EPOCH));
        BufferedImage dragged = HistoryPanel.picture(item);
        assertEquals(320, dragged.getWidth());
        assertEquals(160, dragged.getHeight());
        assertEquals(0, dragged.getRGB(0,0) >>> 24);
        assertEquals(Color.BLUE.getRGB(), dragged.getRGB(130,70));
    }
}
