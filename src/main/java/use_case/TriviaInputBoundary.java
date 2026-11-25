package use_case;

/** Application boundary consumed independently of any UI or I/O adapter. */
public interface TriviaInputBoundary {
    void open(String lockId);
    void submit(String lockId, String answer);
}
