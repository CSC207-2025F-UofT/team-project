package view.dashboard;

/**
 * Handle UI events and delegate to a controller.
 */
public interface EventHandler<T extends EventPacket> {
    void handleEvent(String actionId, T eventPacket);
}
