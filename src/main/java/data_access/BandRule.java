package data_access;

// which ink height gets sized to one line (FormulaLayout.LINE_PX)
@FunctionalInterface
interface BandRule {
    // training's rule now, tallest band of rows
    BandRule TALLEST_BAND = (tallestBand, inkHeight) -> tallestBand;

    // whole formula = one line, the old rule; shipped weights only read fractions/limits right at this size
    BandRule WHOLE_INK = (tallestBand, inkHeight) -> inkHeight;

    double lineHeight(double tallestBand, double inkHeight);
}
