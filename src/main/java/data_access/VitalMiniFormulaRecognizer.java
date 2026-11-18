package data_access;

import interface_adapter.recognize.FormulaRecognizer;
import model.vital_mini;

import java.awt.image.BufferedImage;

// handwriting -> latex w/ trained model
public class VitalMiniFormulaRecognizer implements FormulaRecognizer {
    private final ModelReader model;

    // model-ready picture -> latex, lets tests skip the real weights
    @FunctionalInterface
    interface ModelReader {
        String read(BufferedImage prepared) throws Exception;
    }

    // model is caller's, we dont close it
    public VitalMiniFormulaRecognizer(vital_mini model) {
        this(prepared -> model.generate(prepared, 3));
    }

    VitalMiniFormulaRecognizer(ModelReader model) {
        this.model = model;
    }

    // the model takes height and width in multiples of 16, so scale and pad like training did
    // whole ink = one line, band rule blew fractions up past what these weights can read
    @Override
    public String recognize(BufferedImage formulaImage) throws Exception {
        return model.read(FormulaImagePreprocessor.prepare(formulaImage, BandRule.WHOLE_INK));
    }
}
