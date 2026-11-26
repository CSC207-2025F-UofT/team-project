package org.example.cocktailcraft.search;

import org.example.cocktailcraft.app.AppBuilder;
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
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.example.cocktailcraft.view.search.SearchView;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/** Exercises the Search UI through its controller, interactor, presenter and shared Home list. */
class SearchIntegrationTest {
    private final Cocktail gin = new Cocktail("gin-fizz", "Gin Fizz", List.of(),
            new Tags(Set.of(Tags.BaseSpirit.GIN), Set.of(Tags.Flavor.SOUR_CITRUS)), "", 12.5);
    private final Cocktail rum = new Cocktail("daiquiri", "Daiquiri", List.of(),
            new Tags(Set.of(Tags.BaseSpirit.RUM), Set.of(Tags.Flavor.SWEET)), "", 21.0);
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
            assertTrue(fixture.hasLabel("Please enter a cocktail name or select a filter."));
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

    @Test
    void filteredSelectionPassesTheSameRecipeToDetailsAndExistingCustomizeAction() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> List.of(gin, rum));
            AtomicReference<Cocktail> customized = new AtomicReference<>();
            fixture.home.setCustomizeAction(customized::set);
            JButton customize = find(fixture.home, JButton.class, "Customize");
            assertFalse(customize.isEnabled());
            fixture.name().setText("daiquiri");
            fixture.button("Search").doClick();
            fixture.results().setSelectedIndex(0);
            assertEquals("daiquiri", fixture.selectedId.get());
            assertTrue(customize.isEnabled());
            customize.doClick();
            assertEquals(rum, customized.get());
            fixture.button("Clear").doClick();
            assertFalse(customize.isEnabled());
            customized.set(null);
            customize.doClick();
            assertEquals(null, customized.get());
        });
    }

    @Test
    void startupFailureCanRecoverAndClearRestoresTheRecoveredCatalog() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            AtomicInteger attempts = new AtomicInteger();
            Fixture fixture = new Fixture(() -> {
                if (attempts.getAndIncrement() == 0) {
                    throw new IOException("Startup failure");
                }
                return catalog;
            }, true);
            assertTrue(fixture.hasLabel("Unable to load the cocktail catalog. Please try again."));
            assertEquals(0, fixture.results().getModel().getSize());
            fixture.name().setText("daiquiri");
            fixture.button("Search").doClick();
            assertEquals(1, fixture.results().getModel().getSize());
            fixture.button("Clear").doClick();
            assertTrue(fixture.hasLabel("Explore Cocktails"));
            assertEquals(2, fixture.results().getModel().getSize());
            assertEquals("Gin Fizz", fixture.results().getModel().getElementAt(0));
        });
    }

    @Test
    void clearCanRetryAStartupFailureWithoutEnteringCriteria() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            AtomicInteger attempts = new AtomicInteger();
            Fixture fixture = new Fixture(() -> {
                if (attempts.getAndIncrement() == 0) {
                    throw new IOException("Startup failure");
                }
                return catalog;
            }, true);
            fixture.button("Clear").doClick();
            assertTrue(fixture.hasLabel("Explore Cocktails"));
            assertEquals(2, fixture.results().getModel().getSize());
            assertFalse(fixture.model.getState().isSearchPerformed());
        });
    }

    @Test
    void newDropdownsCombineWithNameAndSpiritAndClearResetsEverySelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            fixture.name().setText("fizz");
            fixture.selectSpirit("Gin");
            fixture.selectFilter("Flavors", "Sour / Citrus");
            fixture.selectFilter("Alcohol content", "10% to below 20% ABV");
            fixture.name().postActionEvent();
            assertEquals(1, fixture.results().getModel().getSize());
            assertEquals("Gin Fizz", fixture.results().getModel().getElementAt(0));
            fixture.results().setSelectedIndex(0);
            assertEquals("gin-fizz", fixture.selectedId.get());
            fixture.button("Clear").doClick();
            assertEquals("", fixture.name().getText());
            assertTrue(fixture.search.getSelectedBaseSpirits().isEmpty());
            assertTrue(fixture.search.getSelectedFlavors().isEmpty());
            assertTrue(fixture.search.getSelectedAlcoholRanges().isEmpty());
            assertTrue(fixture.button("Flavors").isEnabled());
            assertTrue(fixture.button("Alcohol content").isEnabled());
            assertEquals(2, fixture.results().getModel().getSize());
        });
    }

    @Test
    void flavorOnlyAndAlcoholOnlySearchWorkFromDropdowns() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(() -> catalog);
            fixture.selectFilter("Flavors", "Sweet");
            fixture.button("Search").doClick();
            assertEquals("Daiquiri", fixture.results().getModel().getElementAt(0));
            assertEquals(1, fixture.results().getModel().getSize());
            fixture.button("Clear").doClick();
            fixture.selectFilter("Alcohol content", "10% to below 20% ABV");
            fixture.button("Search").doClick();
            assertEquals("Gin Fizz", fixture.results().getModel().getElementAt(0));
            assertEquals(1, fixture.results().getModel().getSize());
        });
    }

    @Test
    void failurePreservesNewDropdownSelectionsForRetry() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            AtomicInteger attempts = new AtomicInteger();
            Fixture fixture = new Fixture(() -> {
                if (attempts.getAndIncrement() == 0) {
                    throw new IOException("Unavailable");
                }
                return catalog;
            });
            fixture.selectFilter("Flavors", "Sweet");
            fixture.selectFilter("Alcohol content", "20% ABV and above");
            fixture.button("Search").doClick();
            assertTrue(fixture.hasLabel("Unable to load the cocktail catalog. Please try again."));
            assertEquals(Set.of(Tags.Flavor.SWEET), fixture.search.getSelectedFlavors());
            assertEquals(Set.of(Tags.AlcoholRange.HIGH), fixture.search.getSelectedAlcoholRanges());
            fixture.button("Search").doClick();
            assertEquals(1, fixture.results().getModel().getSize());
            assertEquals("Daiquiri", fixture.results().getModel().getElementAt(0));
        });
    }

    @Test
    void windowPreservesFilteredRecipeAcrossDetailsAndCustomize() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new AppBuilder().addSearchView().addSearchUseCase().addClickUseCase()
                    .addCustomizeUseCase(() -> null).addMainPageView().build();
            try {
                frame.pack();
                MainPageView home = find(frame.getContentPane(), MainPageView.class, null);
                CustomizeView customize = find(frame.getContentPane(), CustomizeView.class, null);
                find(home, JTextField.class, null).setText("negroni");
                selectMenu(home, "Base spirits", "Gin");
                selectMenu(home, "Flavors", "Bitter");
                selectMenu(home, "Alcohol content", "20% ABV and above");
                find(home, JButton.class, "Search").doClick();
                JList<?> results = find(home, JList.class, null);
                int negroniIndex = -1;
                for (int index = 0; index < results.getModel().getSize(); index++) {
                    if ("Negroni".equals(results.getModel().getElementAt(index))) {
                        negroniIndex = index;
                    }
                }
                assertTrue(negroniIndex >= 0, "The actual catalog must contain a matching Negroni");
                results.setSelectedIndex(negroniIndex);
                assertTrue(find(home, JTextArea.class, null).getText().contains("Campari"));
                find(home, JButton.class, "Customize").doClick();
                assertTrue(customize.isVisible());
                assertEquals("Negroni", find(customize, JTextField.class, null).getText());
                find(customize, JButton.class, "Cancel").doClick();
                assertTrue(home.isVisible());
                assertEquals("Negroni", results.getSelectedValue());
                SearchView search = find(home, SearchView.class, null);
                assertEquals(Set.of(Tags.Flavor.BITTER), search.getSelectedFlavors());
                assertEquals(Set.of(Tags.AlcoholRange.HIGH), search.getSelectedAlcoholRanges());
                find(home, JButton.class, "Clear").doClick();
                assertEquals(663, results.getModel().getSize());
                assertFalse(find(home, JButton.class, "Customize").isEnabled());
            } finally {
                frame.dispose();
            }
        });
    }

    private static void selectMenu(Container parent, String title, String label) {
        JPopupMenu menu = find(parent, JButton.class, title).getComponentPopupMenu();
        for (Component component : menu.getComponents()) {
            JCheckBox option = (JCheckBox) component;
            if (label.equals(option.getText())) {
                option.doClick();
                return;
            }
        }
        throw new AssertionError("Missing filter " + title + ": " + label);
    }

    private final class Fixture {
        private final SearchViewModel model = new SearchViewModel();
        private final SearchView search = new SearchView(model);
        private final AtomicReference<String> selectedId = new AtomicReference<>();
        private final MainPageView home;

        Fixture(SearchDataAccessInterface dao) {
            this(dao, false);
        }

        Fixture(SearchDataAccessInterface dao, boolean loadOnStartup) {
            SearchController controller = new SearchController(new SearchInteractor(dao, new SearchPresenter(model)));
            search.setSearchController(controller);
            ClickController details = new ClickController(input -> { }) {
                @Override
                public void execute(String cocktailId) {
                    selectedId.set(cocktailId);
                }
            };
            if (loadOnStartup) {
                controller.clear();
            } else {
                model.getState().setResults(catalog);
            }
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

        void selectFilter(String title, String label) {
            for (Component component : search.getComponents()) {
                if (component instanceof JButton && ((JButton) component).getText().startsWith(title)) {
                    JPopupMenu dropdown = ((JButton) component).getComponentPopupMenu();
                    for (Component item : dropdown.getComponents()) {
                        JCheckBox option = (JCheckBox) item;
                        if (label.equals(option.getText())) {
                            option.doClick();
                            return;
                        }
                    }
                }
            }
            throw new AssertionError("Missing filter " + title + ": " + label);
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
