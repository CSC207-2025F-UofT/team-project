package interface_adapter.outfit;

import use_case.outfit.*;
import javax.swing.SwingUtilities;

public final class OutfitPresenter implements OutfitOutputBoundary {
    private final OutfitViewModel model;
    public OutfitPresenter(OutfitViewModel model) { this.model = model; }
    public void present(OutfitOutputData data) { onUi(() -> model.setState(data)); }
    public void presentError(String message) { onUi(() -> model.showError(message)); }
    private void onUi(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) action.run();
        else SwingUtilities.invokeLater(action);
    }
}
