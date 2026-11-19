package view.canvas;

import interface_adapter.phase.PhaseController;
import interface_adapter.phase.PhaseExploreController;
import interface_adapter.phase.PhaseViewModel;

import java.util.Objects;

/**
 * One phase portrait's own use-case session: draw it, then pan / zoom / pin it, reported through its
 * own view model. The board asks for a fresh one per portrait so several can be live at once.
 */
public record PhaseSession(PhaseController controller, PhaseExploreController explore, PhaseViewModel viewModel) {
    public PhaseSession {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(explore);
        Objects.requireNonNull(viewModel);
    }
}
