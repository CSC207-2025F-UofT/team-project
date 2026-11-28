package use_case.log_input;

import entity.RoundRecord;

/**
 * Data Access Interface for the Log Input Use Case.
 */
public interface LogInputDataAccessInterface {
    RoundRecord getCurrentRoundRecord();
}
