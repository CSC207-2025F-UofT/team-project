package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.jupiter.api.Test;

import interface_adapter.SwitchViewController;
import interface_adapter.ViewManagerModel;
import interface_adapter.view_pet_details.PetDetailState;
import interface_adapter.view_pet_details.PetDetailViewModel;

class PetDetailViewTest {
    private final PetDetailViewModel viewModel = new PetDetailViewModel();
    private final PetDetailView view =
            new PetDetailView(viewModel, new SwitchViewController(new ViewManagerModel()), "gallery");

    private JButton applyButton() {
        for (java.awt.Component component : allComponents(view)) {
            if (component instanceof JButton && ((JButton) component).getText().startsWith("Apply")) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Apply button not found");
    }

    private List<java.awt.Component> allComponents(java.awt.Container container) {
        final List<java.awt.Component> found = new ArrayList<>();
        for (java.awt.Component child : container.getComponents()) {
            found.add(child);
            if (child instanceof java.awt.Container) {
                found.addAll(allComponents((java.awt.Container) child));
            }
        }
        return found;
    }

    private void show(String id, String name, String error) {
        final PetDetailState state = new PetDetailState();
        state.setPetId(id);
        state.setName(name);
        state.setErrorMessage(error);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Test
    void applyButtonIsHiddenUntilAHandlerIsSet() {
        show("7", "Miso", "");

        assertFalse(applyButton().isVisible());
    }

    @Test
    void applyButtonPassesTheCatToTheHandler() {
        final List<String> received = new ArrayList<>();
        view.setApplyHandler((id, name) -> received.add(id + ":" + name));
        show("7", "Miso", "");

        assertTrue(applyButton().isVisible());
        applyButton().doClick();

        assertEquals(List.of("7:Miso"), received);
    }

    @Test
    void applyButtonIsHiddenWhenTheCatCannotBeShown() {
        view.setApplyHandler((id, name) -> { });
        show("", "", "This cat is no longer available.");

        assertFalse(applyButton().isVisible());
    }
}
