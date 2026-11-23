package interface_adapter.outfit;

import use_case.outfit.OutfitInputBoundary;
import javax.swing.SwingWorker;
import java.util.List;

/** Serialize requests and run storage work off the Swing event thread. */
public final class OutfitController {
    private final OutfitInputBoundary input;
    private final OutfitViewModel model;
    private boolean busy;
    public OutfitController(OutfitInputBoundary input, OutfitViewModel model) {
        this.input = input; this.model = model;
    }
    public void load(String tagId) { submit(() -> input.load(tagId)); }
    public void createTag(String name) { submit(() -> input.createTag(name)); }
    public void deleteOutfit(String id) { submit(() -> input.deleteOutfit(id)); }
    public void createOutfit(String name, List<String> items, List<String> tags) {
        List<String> itemSnapshot = List.copyOf(items), tagSnapshot = List.copyOf(tags);
        submit(() -> input.createOutfit(name, itemSnapshot, tagSnapshot));
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
