package interface_adapter;

/** Schedules a synchronous use case without coupling controllers to executors. */
@FunctionalInterface
public interface CommandDispatcher {
    void dispatch(Runnable command);
}
