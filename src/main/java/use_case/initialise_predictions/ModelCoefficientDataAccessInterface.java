package use_case.initialise_predictions;

import java.io.IOException;
import java.util.Map;

/**
 * Interface for accessing model coefficients.
 * Loads the trained Ridge Regression coefficients for each position.
 */
public interface ModelCoefficientDataAccessInterface {
    /**
     * Load coefficients for a specific position.
     *
     * @param position Position type (1=GK, 2=DEF, 3=MID, 4=FWD)
     * @return Map of feature names to coefficient values (includes "intercept")
     * @throws IOException if coefficients cannot be loaded
     */
    Map<String, Double> getCoefficients(int position) throws IOException;

    /**
     * Load all coefficients for all positions at once.
     *
     * @return Map of position (1-4) to coefficient maps
     * @throws IOException if coefficients cannot be loaded
     */
    Map<Integer, Map<String, Double>> getAllCoefficients() throws IOException;
}