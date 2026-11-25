package org.example.cocktailcraft.search;

import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.entity.Tags.Flavor;
import org.example.cocktailcraft.entity.Tags.AlcoholRange;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.view.search.SearchView;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    void clearRemainsInsideSearchBarAfterCountsGrowAndWindowNarrows() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            SearchView view = new SearchView(new SearchViewModel());
            Dimension initialSize = view.getPreferredSize();
            view.setSize(initialSize);
            view.doLayout();
            for (String title : new String[] {"Base spirits", "Flavors", "Alcohol content"}) {
                JPopupMenu dropdown = selector(view, title).getComponentPopupMenu();
                for (Component component : dropdown.getComponents()) {
                    ((JCheckBox) component).doClick(0);
                }
            }
            view.doLayout();
            assertControlsFit(view);
            view.setSize(initialSize.width - 100, initialSize.height);
            view.doLayout();
            assertControlsFit(view);
            selector(view, "Clear").doClick(0);
            assertTrue(view.getSelectedBaseSpirits().isEmpty());
            assertTrue(view.getSelectedFlavors().isEmpty());
            assertTrue(view.getSelectedAlcoholRanges().isEmpty());
            view.doLayout();
            assertControlsFit(view);
        });
    }

    private static void assertControlsFit(SearchView view) {
        Rectangle visibleArea = new Rectangle(0, 0, view.getWidth(), view.getHeight());
        Component[] controls = view.getComponents();
        for (int index = 0; index < controls.length; index++) {
            Component control = controls[index];
            assertTrue(control.getWidth() > 0 && control.getHeight() > 0);
            assertTrue(visibleArea.contains(control.getBounds()),
                    control.getClass().getSimpleName() + " clipped: " + control.getBounds());
            for (int other = index + 1; other < controls.length; other++) {
                assertFalse(control.getBounds().intersects(controls[other].getBounds()), "Controls overlap");
            }
        }
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
