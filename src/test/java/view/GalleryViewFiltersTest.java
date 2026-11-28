package view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.junit.jupiter.api.Test;

import data_access.InMemoryPetDataAccess;
import data_access.StarterPetCatalog;
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
        final InMemoryPetDataAccess data = new InMemoryPetDataAccess(StarterPetCatalog.createPets());
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

    private <T extends JComponent> T filter(Class<T> type, String name) {
        return all(view, type).stream().filter(field -> name.equals(field.getAccessibleContext().getAccessibleName()))
                .findFirst().orElseThrow();
    }

    @Test
    void sexBoxNarrowsTheCards() {
        view.loadAll();
        final int all = cardCount();

        filter(JComboBox.class, "Sex filter").setSelectedItem(SexFilterOption.MALE);

        assertEquals(0, cardCount());
        assertEquals(1, all);
    }

    @Test
    void breedTextNarrowsTheCards() {
        view.loadAll();

        final JTextField breed = filter(JTextField.class, "Breed filter");
        breed.setText("siam");
        assertEquals(0, cardCount());
        breed.setText("snow");
        assertEquals(1, cardCount());
    }

    @Test
    void clearFiltersBringsEveryCatBack() {
        view.loadAll();
        filter(JComboBox.class, "Sex filter").setSelectedItem(SexFilterOption.MALE);
        filter(JTextField.class, "Breed filter").setText("siam");
        filter(JTextField.class, "Color filter").setText("black");
        filter(JComboBox.class, "Hair length filter").setSelectedItem("Long");

        all(view, JButton.class).stream().filter(b -> b.getText().equals("Clear filters"))
                .findFirst().orElseThrow().doClick();

        assertEquals(1, cardCount());
        assertEquals("", filter(JTextField.class, "Breed filter").getText());
    }

    @Test
    void colorAndHairLengthWorkTogether() {
        view.loadAll();

        filter(JTextField.class, "Color filter").setText("CHOCOLATE");
        final JComboBox<?> hair = filter(JComboBox.class, "Hair length filter");
        hair.setSelectedItem("Long");
        assertEquals(0, cardCount());
        hair.setSelectedItem("Short");
        assertEquals(1, cardCount());
    }
}
