package use_case;

/** Application boundary consumed independently of any UI or I/O adapter. */
public interface GameOutputBoundary {
    void present(GameOutputData data);
}
