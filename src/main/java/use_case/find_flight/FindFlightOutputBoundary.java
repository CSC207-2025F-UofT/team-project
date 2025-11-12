package use_case.find_flight;

import use_case.signup.SignupOutputData;

/**
 * The output boundary for the Find Flight Use case
 */

public interface FindFlightOutputBoundary {

    /**
     * Prepares the success view for the Find Flight Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(FindFlightOutputData outputData);

    /**
     * Prepares the failure view for the Find Flight Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
