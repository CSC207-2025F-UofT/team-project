package view.canvas;

import interface_adapter.recognize.FormulaRecognizer;
import view.FormulaCapture;
import view.answer.EqualsSpot;

// reads a capture; if the model lost the trailing "=", reads the rest alone + puts = back
final class CaptureReader {
    private final FormulaRecognizer recognizer;

    CaptureReader(FormulaRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    String read(FormulaCapture capture) throws Exception {
        String whole = recognizer.recognize(capture.getImage());
        if (EqualsSpot.topLevelEquals(whole) || capture.getWithoutEquals() == null) {
            return whole;
        }
        String rest = recognizer.recognize(capture.getWithoutEquals());
        return rest == null || rest.isBlank() ? whole : rest + "=";
    }
}
