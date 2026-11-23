package interface_adapter.simulation;

/** Holds the display state. The presenter receives updates and refreshes the passive view. */
public final class SimulationViewModel {
    public interface Listener {
        void displayChanged(SimulationState state, CameraPose camera);
    }

    private SimulationState state;
    private CameraPose camera = new CameraPose();
    private Listener listener;

    public SimulationViewModel(SimulationState initialState) {
        state = initialState;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        notifyPresenter();
    }

    public SimulationState getState() {
        return state;
    }

    public CameraPose getCamera() {
        return camera;
    }

    public void setState(SimulationState newState) {
        if (!newState.hasSameTerrain(state)) {
            camera = camera.centered();
        }
        state = newState;
        notifyPresenter();
    }

    public void setFileStatus(String text) {
        setState(state.withFileStatus(text));
    }

    public void setCamera(CameraPose newCamera) {
        camera = newCamera;
        notifyPresenter();
    }

    private void notifyPresenter() {
        if (listener != null) {
            listener.displayChanged(state, camera);
        }
    }
}
