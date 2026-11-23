package use_case.playcard;

/**
 * Input Boundary for actions which are related to playing cards.
 */
public interface PlayCardInputBoundary {

    /**
     * Executes the PlayCard use case.
     *
     * @param playCardInputData the input data
     */
    void execute(PlayCardInputData playCardInputData);

}
