package interface_adapter.wardrobe;

import use_case.add_clothing.AddClothingInputBoundary;
import javax.swing.SwingWorker;

/** Serialize requests and run storage/file work off the Swing event thread. */
public final class WardrobeController {
    private final AddClothingInputBoundary input;
    private final WardrobeViewModel model;
    private boolean busy;

    public WardrobeController(AddClothingInputBoundary input, WardrobeViewModel model) {
        this.input = input; this.model = model;
    }
    public void load() { submit(input::load); }
    public void addClothing(String name, String type, String imagePath) {
        submit(() -> input.addClothing(name, type, imagePath));
    }
    private void submit(Runnable action) {
        if (busy) return;
        busy = true;
        model.setBusy(true);
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() { action.run(); return null; }
            protected void done() {
                try { get(); }
                catch (Exception ex) { model.showError("Unexpected error. Please retry."); }
                finally { busy = false; model.setBusy(false); }
            }
        }.execute();
    }
}
