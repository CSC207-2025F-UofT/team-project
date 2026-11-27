package interface_adapter.wardrobe;

import use_case.add_clothing.WardrobeOutputBoundary;
import use_case.add_clothing.WardrobeOutputData;
import javax.swing.SwingUtilities;

public final class WardrobePresenter implements WardrobeOutputBoundary {
    private final WardrobeViewModel model;
    public WardrobePresenter(WardrobeViewModel model) { this.model = model; }
    public void present(WardrobeOutputData data) { onUi(() -> model.setState(data)); }
    public void presentError(String message) { onUi(() -> model.showError(message)); }
    private void onUi(Runnable action) {
        if (SwingUtilities.isEventDispatchThread()) action.run();
        else SwingUtilities.invokeLater(action);
    }
}
