package use_case;

/** Application boundary consumed independently of any UI or I/O adapter. */
public interface InteractInputBoundary {
    void execute(String objectId, String actionId, java.util.Optional<String> itemId);
}
