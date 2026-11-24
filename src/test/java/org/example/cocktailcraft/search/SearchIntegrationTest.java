package org.example.cocktailcraft.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.search.SearchView;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Exercises the Search UI through its controller, interactor, presenter and shared Home list. */
class SearchIntegrationTest {
    private final Cocktail gin = new Cocktail("gin-fizz", "Gin Fizz", List.of(),
            new Tags(Set.of(Tags.BaseSpirit.GIN)));
    private final Cocktail rum = new Cocktail("daiquiri", "Daiquiri", List.of(),
            new Tags(Set.of(Tags.BaseSpirit.RUM)));
    private final List<Cocktail> catalog = List.of(gin, rum);

    @Test
    void searchUpdatesHomeAndSelectionUsesResultId() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            assertEquals(2, fixture.results().getModel().getSize());
            fixture.name().setText("dai");
            fixture.button("Search").doClick();
            assertEquals(1, fixture.results().getModel().getSize());
            assertEquals("Daiquiri", fixture.results().getModel().getElementAt(0));
            fixture.results().setSelectedIndex(0);
            assertEquals("daiquiri", fixture.selectedId.get());
            assertTrue(fixture.hasLabel("Search results (1)"));
        });
    }

    @Test
    void clearResetsNameSpiritsAndRestoresCatalog() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            fixture.name().setText("fizz");
            fixture.selectSpirit("Gin");
            fixture.button("Search").doClick();
            assertEquals(1, fixture.results().getModel().getSize());
            fixture.button("Clear").doClick();
            assertEquals("", fixture.name().getText());
            assertEquals(Set.of(), fixture.search.getSelectedBaseSpirits());
            assertEquals(2, fixture.results().getModel().getSize());
            assertEquals("Base spirits", fixture.button("Base spirits").getText());
            assertTrue(fixture.hasLabel("Explore Cocktails"));
            assertFalse(fixture.model.getState().isSearchPerformed());
            fixture.results().setSelectedIndex(1);
            assertEquals("daiquiri", fixture.selectedId.get());
        });
    }

    @Test
    void emptyResultsAndMissingInputHaveDifferentMessages() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            fixture.name().setText("no such cocktail");
            fixture.button("Search").doClick();
            assertEquals(0, fixture.results().getModel().getSize());
            assertTrue(fixture.hasLabel("No cocktails found."));
            fixture.name().setText("  ");
            fixture.button("Search").doClick();
            assertTrue(fixture.hasLabel("Please enter a cocktail name or select a base spirit."));
            fixture.button("Clear").doClick();
            assertTrue(fixture.hasLabel("Explore Cocktails"));
            assertEquals("", fixture.model.getState().getErrorMessage());
        });
    }

    @Test
    void errorPreservesInputAndRetryReplacesErrorWithResults() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            AtomicInteger attempts = new AtomicInteger();
            Fixture fixture = new Fixture(() -> {
                if (attempts.getAndIncrement() == 0) {
                    throw new IOException("Unavailable");
                }
                return catalog;
            });
            fixture.name().setText("fizz");
            fixture.selectSpirit("Gin");
            fixture.button("Search").doClick();
            assertTrue(fixture.hasLabel("Unable to load the cocktail catalog. Please try again."));
            assertEquals("fizz", fixture.name().getText());
            assertEquals(Set.of(Tags.BaseSpirit.GIN), fixture.search.getSelectedBaseSpirits());
            fixture.button("Search").doClick();
            assertTrue(fixture.hasLabel("Search results (1)"));
            assertEquals("", fixture.model.getState().getErrorMessage());
        });
    }

    @Test
    void repeatedSearchClearsOldSelectionWithoutOpeningAnotherRecipe() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            fixture.results().setSelectedIndex(1);
            fixture.selectedId.set(null);
            fixture.name().setText("gin");
            fixture.button("Search").doClick();
            assertEquals(-1, fixture.results().getSelectedIndex());
            assertEquals(null, fixture.selectedId.get());
            fixture.results().setSelectedIndex(0);
            assertEquals("gin-fizz", fixture.selectedId.get());
        });
    }

    @Test
    void enterSearchesAndClearResetsAllControlsAndRestoresCatalog() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> List.of(gin, rum));
            fixture.name().setText("fizz");
            fixture.selectSpirit("Gin");
            fixture.name().postActionEvent();
            assertEquals(1, fixture.results().getModel().getSize());
            fixture.button("Clear").doClick();
            assertEquals("", fixture.name().getText());
            assertTrue(fixture.search.getSelectedBaseSpirits().isEmpty());
            assertTrue(fixture.button("Base spirits").isEnabled());
            assertEquals(2, fixture.results().getModel().getSize());
            assertEquals("Gin Fizz", fixture.results().getModel().getElementAt(0));
            assertEquals("Daiquiri", fixture.results().getModel().getElementAt(1));
            assertFalse(fixture.model.getState().isSearchPerformed());
            assertTrue(fixture.hasLabel("Explore Cocktails"));
        });
    }

    private final class Fixture {
        private final SearchViewModel model = new SearchViewModel();
        private final SearchView search = new SearchView(model);
        private final AtomicReference<String> selectedId = new AtomicReference<>();
        private final MainPageView home;

        Fixture(SearchDataAccessInterface dao) {
            search.setSearchController(new SearchController(new SearchInteractor(dao, new SearchPresenter(model))));
            ClickController details = new ClickController(input -> { }) {
                @Override
                public void execute(String cocktailId) {
                    selectedId.set(cocktailId);
                }
            };
            home = new MainPageView(search, catalog, details, new ClickView(new ClickViewModel()), model);
        }

        JButton button(String label) {
            return find(search, JButton.class, label);
        }

        JTextField name() {
            return find(search, JTextField.class, null);
        }

        JList<?> results() {
            return find(home, JList.class, null);
        }

        boolean hasLabel(String text) {
            return find(home, JLabel.class, text) != null;
        }

        void selectSpirit(String label) {
            JPopupMenu dropdown = button("Base spirits").getComponentPopupMenu();
            for (Component component : dropdown.getComponents()) {
                JCheckBox option = (JCheckBox) component;
                if (label.equals(option.getText())) {
                    option.doClick();
                    return;
                }
            }
            throw new AssertionError("Missing spirit " + label);
        }
    }

    private static <T extends Component> T find(Container parent, Class<T> type, String text) {
        for (Component child : parent.getComponents()) {
            if (type.isInstance(child) && (text == null
                    || child instanceof JButton && text.equals(((JButton) child).getText())
                    || child instanceof JLabel && text.equals(((JLabel) child).getText()))) {
                return type.cast(child);
            }
            if (child instanceof Container) {
                T found = find((Container) child, type, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
