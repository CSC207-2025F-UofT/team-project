package interface_adapter.recognize;

import java.awt.image.BufferedImage;

/**
 * Handwriting recognition (US1): turns a picture of a handwritten formula into LaTeX.
 *
 * <p>The image comes from {@code DrawingCanvas.toFormulaImage()}: drawn from the strokes at a fixed
 * scale (not the screen zoom), gray dark ink on white, cropped to the ink plus pen width, at least
 * 8 px of margin, sides padded to multiples of 16. The implementation fits it to the model.
 */
public interface FormulaRecognizer {
    /**
     * May be slow (runs the model), so call it off the Swing event thread.
     *
     * @param formulaImage the handwritten formula, dark ink on a white background
     * @return the recognised LaTeX, e.g. {@code y=x^{2}+1}
     * @throws Exception if recognition fails; its message is suitable for the user
     */
    String recognize(BufferedImage formulaImage) throws Exception;
}
