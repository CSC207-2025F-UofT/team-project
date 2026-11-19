package view.canvas;

import interface_adapter.graph.GraphController;
import interface_adapter.graph.GraphExploreController;
import interface_adapter.graph.GraphViewModel;

import java.util.Objects;

/**
 * One graph's own use-case session: graph it, then pan / zoom / trace it, reported through its own
 * view model. The board asks for a fresh one per graph so several graphs can be live at once.
 */
public record GraphSession(GraphController controller, GraphExploreController explore, GraphViewModel viewModel) {
    public GraphSession {
        Objects.requireNonNull(controller);
        Objects.requireNonNull(explore);
        Objects.requireNonNull(viewModel);
    }
}
