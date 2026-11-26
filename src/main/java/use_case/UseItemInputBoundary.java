package use_case;

/** Application boundary consumed independently of any UI or I/O adapter. */
public interface UseItemInputBoundary {
    void execute(String lockId);
}
