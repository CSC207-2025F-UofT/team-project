package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.junit.jupiter.api.Test;

import interface_adapter.check_compatibility.CompatibilityState;
import interface_adapter.check_compatibility.CompatibilityViewModel;

class CompatibilityPanelTest {
    private final CompatibilityViewModel viewModel = new CompatibilityViewModel();
    private final CompatibilityPanel panel = new CompatibilityPanel(viewModel);

    private List<String> texts() {
        final List<String> texts = new ArrayList<>();
        for (java.awt.Component component : panel.getComponents()) {
            texts.add(((JLabel) component).getText());
        }
        return texts;
    }

    @Test
    void showsTheHeadlineThenOneLinePerRule() {
        final CompatibilityState state = new CompatibilityState();
        state.setHeadline("1 conflict to think about for Miso.");
        state.setLines(List.of("Match: A - a", "Conflict: B - b"), List.of(false, true));
        viewModel.setState(state);

        viewModel.notifyStateChanged();

        assertEquals(List.of("1 conflict to think about for Miso.", "Match: A - a", "Conflict: B - b"), texts());
    }

    @Test
    void showsTheMessageWhenThereAreNoResults() {
        final CompatibilityState state = new CompatibilityState();
        state.setMessage("Log in and fill in My profile to see how well this cat fits your home.");
        viewModel.setState(state);

        viewModel.notifyStateChanged();

        assertEquals(1, texts().size());
        assertTrue(texts().get(0).contains("My profile"));
    }

    @Test
    void anewStateReplacesTheOldLines() {
        final CompatibilityState first = new CompatibilityState();
        first.setLines(List.of("Match: A - a"), List.of(false));
        viewModel.setState(first);
        viewModel.notifyStateChanged();

        viewModel.setState(new CompatibilityState());
        viewModel.notifyStateChanged();

        assertTrue(texts().isEmpty());
    }
}
