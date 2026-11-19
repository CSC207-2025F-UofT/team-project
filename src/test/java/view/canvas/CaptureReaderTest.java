package view.canvas;

import interface_adapter.recognize.FormulaRecognizer;
import org.junit.jupiter.api.Test;
import view.FormulaCapture;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

// model lost the trailing "=", read the ink w/o it and put = back
class CaptureReaderTest {
    private final BufferedImage whole = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
    private final BufferedImage rest = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
    private final AtomicInteger calls = new AtomicInteger();

    private String read(FormulaRecognizer model, BufferedImage withoutEquals) throws Exception {
        FormulaRecognizer counted = image -> {
            calls.incrementAndGet();
            return model.recognize(image);
        };
        return new CaptureReader(counted).read(new FormulaCapture(1, 1, whole, withoutEquals));
    }

    @Test
    void readWithEqualsIsKept() throws Exception {
        assertEquals("1+1=", read(image -> "1+1=", rest));
        assertEquals(1, calls.get(), "rest never read");
    }

    @Test
    void lostEqualsIsPutBack() throws Exception {
        assertEquals("1+1=", read(image -> image == whole ? "1+1-" : "1+1", rest));
    }

    @Test
    void noPairKeepsTheRead() throws Exception {
        assertEquals("1+1-", read(image -> "1+1-", null));
        assertEquals(1, calls.get());
    }

    @Test
    void blankRestKeepsTheWholeRead() throws Exception {
        assertEquals("1+1-", read(image -> image == whole ? "1+1-" : " ", rest));
    }
}
