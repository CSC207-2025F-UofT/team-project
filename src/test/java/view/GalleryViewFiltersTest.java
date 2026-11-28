package view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.junit.jupiter.api.Test;

import data_access.InMemoryPetDataAccess;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.BrowsePetsPresenter;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.browse_pets.SexFilterOption;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsPresenter;
import interface_adapter.view_pet_details.PetDetailViewModel;
import use_case.browse_pets.BrowsePetsInteractor;
import use_case.view_pet_details.ViewPetDetailsInteractor;

class GalleryViewFiltersTest {
    private final GalleryView view = buildView();

    private GalleryView buildView() {
        final InMemoryPetDataAccess data = InMemoryPetDataAccess.withSampleData();
        final GalleryViewModel viewModel = new GalleryViewModel();
        final BrowsePetsController controller = new BrowsePetsController(
                new BrowsePetsInteractor(data, new BrowsePetsPresenter(viewModel)));
        final ViewPetDetailsController details = new ViewPetDetailsController(new ViewPetDetailsInteractor(data,
                new ViewPetDetailsPresenter(new PetDetailViewModel(), new ViewManagerModel())));
        return new GalleryView(controller, viewModel, details);
    }

    private <T extends java.awt.Component> List<T> all(java.awt.Container container, Class<T> type) {
        final List<T> found = new ArrayList<>();
        for (java.awt.Component child : container.getComponents()) {
            if (type.isInstance(child)) {
                found.add(type.cast(child));
            }
            if (child instanceof java.awt.Container) {
                found.addAll(all((java.awt.Container) child, type));
            }
        }
        return found;
    }

    private int cardCount() {
        return (int) all(view, JButton.class).stream().filter(b -> b.getText().equals("View details")).count();
    }

    @SuppressWarnings("unchecked")
    private JComboBox<SexFilterOption> sexBox() {
        for (JComboBox<?> box : all(view, JComboBox.class)) {
            if ("Sex filter".equals(box.getAccessibleContext().getAccessibleName())) {
                return (JComboBox<SexFilterOption>) box;
            }
        }
        throw new AssertionError("No sex box");
    }

    private JCheckBox box(String text) {
        return all(view, JCheckBox.class).stream().filter(b -> b.getText().equals(text)).findFirst().orElseThrow();
    }

    @Test
    void sexBoxNarrowsTheCards() {
        view.loadAll();
        final int all = cardCount();

        sexBox().setSelectedItem(SexFilterOption.MALE);

        assertEquals(2, cardCount());
        assertEquals(4, all);
    }

    @Test
    void breedTextNarrowsTheCards() {
        view.loadAll();

        all(view, JTextField.class).get(0).setText("siam");

        assertEquals(1, cardCount());
    }

    @Test
    void clearFiltersBringsEveryCatBack() {
        view.loadAll();
        sexBox().setSelectedItem(SexFilterOption.MALE);
        all(view, JTextField.class).get(0).setText("siam");
        box("Only vaccinated cats").setSelected(true);

        all(view, JButton.class).stream().filter(b -> b.getText().equals("Clear filters"))
                .findFirst().orElseThrow().doClick();

        assertEquals(4, cardCount());
        assertEquals("", all(view, JTextField.class).get(0).getText());
    }

    @Test
    void vaccinatedBoxKeepsOnlyVaccinatedCats() {
        // Every sample cat is vaccinated, so ticking the box keeps all of them.
        view.loadAll();

        box("Only vaccinated cats").doClick();

        assertEquals(4, cardCount());
    }
}
