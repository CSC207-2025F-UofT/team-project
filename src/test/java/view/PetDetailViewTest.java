package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.jupiter.api.Test;

import entity.ExternalPetListing;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import interface_adapter.ExternalListingViewData;
import interface_adapter.SwitchViewController;
import interface_adapter.ViewManagerModel;
import interface_adapter.view_pet_details.PetDetailState;
import interface_adapter.view_pet_details.PetDetailViewModel;

class PetDetailViewTest {
    private final PetDetailViewModel viewModel = new PetDetailViewModel();
    private final PetDetailView view =
            new PetDetailView(viewModel, new SwitchViewController(new ViewManagerModel()), "gallery");

    private JButton applyButton() {
        return buttonStartingWith("Apply");
    }

    private JButton buttonStartingWith(String label) {
        for (java.awt.Component component : allComponents(view)) {
            if (component instanceof JButton && ((JButton) component).getText().startsWith(label)) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Button not found: " + label);
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

    @Test
    void externalListingsShowTheRescueActionAndLocalCatsStillUseApply() {
        view.setApplyHandler((id, name) -> { });
        final Pet cat = new Pet("rescuegroups:7", "Maple", Species.CAT, "Unknown", 0, PetSex.UNKNOWN,
                List.of(), "", false, false, false, BigDecimal.ZERO, "", PetStatus.AVAILABLE,
                new ExternalPetListing("Example Rescue", "https://example.org/", "https://example.org/maple",
                        "Adult", false));
        final PetDetailState state = new PetDetailState();
        state.setPetId(cat.getId());
        state.setName(cat.getName());
        state.setExternalListing(new ExternalListingViewData(cat));
        viewModel.setState(state);
        viewModel.notifyStateChanged();

        assertTrue(buttonStartingWith("Example Rescue").isVisible());
        assertTrue(buttonStartingWith("Visit rescue listing").isVisible());
        show("7", "Miso", "");
        assertTrue(applyButton().isVisible());
    }

    @Test
    void petShownHandlerIsCalledWithTheCatId() {
        final List<String> shown = new ArrayList<>();
        view.setPetShownHandler(shown::add);

        show("p1", "Miso", "");

        assertEquals(List.of("p1"), shown);
    }

    @Test
    void petShownHandlerIsNotCalledWhenTheCatCannotBeShown() {
        final List<String> shown = new ArrayList<>();
        view.setPetShownHandler(shown::add);

        show("", "", "This cat is no longer available.");

        assertTrue(shown.isEmpty());
    }
}
