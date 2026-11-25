package org.example.cocktailcraft.search;

import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.entity.Tags.Flavor;
import org.example.cocktailcraft.entity.Tags.AlcoholRange;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.view.search.SearchView;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchViewTest {
    @Test
    void supportsMultipleSelectionsAndIndependentDeselection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SearchView view = new SearchView(new SearchViewModel());
            JButton selector = null;
            for (Component component : view.getComponents()) {
                if (component instanceof JButton && ((JButton) component).getText().equals("Base spirits")) {
                    selector = (JButton) component;
                }
            }
            JPopupMenu dropdown = selector.getComponentPopupMenu();
            assertEquals(7, dropdown.getComponentCount());
            assertEquals(Set.of(), view.getSelectedBaseSpirits());
            option(dropdown, "Gin").doClick();
            option(dropdown, "Rum").doClick();
            assertEquals(Set.of(BaseSpirit.GIN, BaseSpirit.RUM), view.getSelectedBaseSpirits());
            assertEquals("Base spirits (2)", selector.getText());
            option(dropdown, "Gin").doClick();
            assertEquals(Set.of(BaseSpirit.RUM), view.getSelectedBaseSpirits());
            option(dropdown, "Rum").doClick();
            assertEquals(Set.of(), view.getSelectedBaseSpirits());
            assertEquals("Base spirits", selector.getText());
        });
    }

    @Test
    void flavorDropdownSupportsIndependentMultipleSelectionAndCount() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SearchView view = new SearchView(new SearchViewModel());
            JButton selector = selector(view, "Flavors");
            JPopupMenu dropdown = selector.getComponentPopupMenu();
            assertEquals(9, dropdown.getComponentCount());
            option(dropdown, "Sweet").doClick();
            option(dropdown, "Bitter").doClick();
            assertEquals(Set.of(Flavor.SWEET, Flavor.BITTER), view.getSelectedFlavors());
            assertEquals("Flavors (2)", selector.getText());
            option(dropdown, "Sweet").doClick();
            assertEquals(Set.of(Flavor.BITTER), view.getSelectedFlavors());
            assertEquals("Flavors (1)", selector.getText());
            option(dropdown, "Bitter").doClick();
            assertEquals(Set.of(), view.getSelectedFlavors());
            assertEquals("Flavors", selector.getText());
        });
    }

    @Test
    void alcoholDropdownDistinguishesUnknownFromZeroAndSupportsMultipleRanges() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SearchView view = new SearchView(new SearchViewModel());
            JButton selector = selector(view, "Alcohol content");
            JPopupMenu dropdown = selector.getComponentPopupMenu();
            assertEquals(5, dropdown.getComponentCount());
            option(dropdown, "0% ABV").doClick();
            option(dropdown, "ABV not specified").doClick();
            assertEquals(Set.of(AlcoholRange.ZERO, AlcoholRange.UNKNOWN), view.getSelectedAlcoholRanges());
            assertEquals("Alcohol content (2)", selector.getText());
            option(dropdown, "0% ABV").doClick();
            assertEquals(Set.of(AlcoholRange.UNKNOWN), view.getSelectedAlcoholRanges());
            assertEquals("Alcohol content (1)", selector.getText());
            option(dropdown, "ABV not specified").doClick();
            assertEquals(Set.of(), view.getSelectedAlcoholRanges());
            assertEquals("Alcohol content", selector.getText());
        });
    }

    private static JButton selector(SearchView view, String text) {
        for (Component component : view.getComponents()) {
            if (component instanceof JButton && text.equals(((JButton) component).getText())) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Missing selector: " + text);
    }

    private static JCheckBox option(JPopupMenu dropdown, String label) {
        for (Component component : dropdown.getComponents()) {
            JCheckBox option = (JCheckBox) component;
            if (option.getText().equals(label)) {
                return option;
            }
        }
        throw new AssertionError("Missing option: " + label);
    }
}
