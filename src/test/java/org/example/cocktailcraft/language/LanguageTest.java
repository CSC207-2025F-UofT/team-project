package org.example.cocktailcraft.language;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.example.cocktailcraft.view.language.I18n;
import org.example.cocktailcraft.view.language.Language;
import org.example.cocktailcraft.view.search.SearchView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageTest {
    @Test
    void allBundlesHaveCompleteTranslations() {
        ResourceBundle english = ResourceBundle.getBundle("i18n.messages", Locale.ROOT);
        for (String suffix : List.of("_zh", "_fr")) {
            ResourceBundle translated = ResourceBundle.getBundle("i18n.messages" + suffix, Locale.ROOT);
            assertEquals(english.keySet(), translated.keySet());
            for (String key : english.keySet()) {
                assertFalse(translated.getString(key).isBlank(), key);
            }
        }
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.FRENCH);
            assertEquals("Save", new I18n(Language.ENGLISH).text("save"));
        } finally {
            Locale.setDefault(previous);
        }
        assertEquals("基酒（2）", new I18n(Language.CHINESE).text("spirits.count", 2));
        assertEquals("Langue : Français", new I18n(Language.FRENCH).text("language.current", Language.FRENCH));
    }

    @Test
    void everyExistingPageUsesTheSameLanguageWithoutChangingRecipeData() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (Language language : Language.values()) {
                I18n i18n = new I18n(language);
                SearchViewModel searchModel = new SearchViewModel();
                SearchView search = new SearchView(searchModel, i18n);
                JButton spirits = button(search, i18n.text("spirits"));
                JPopupMenu popup = spirits.getComponentPopupMenu();
                JCheckBox gin = (JCheckBox) popup.getComponent(0);
                assertEquals(i18n.text("spirit.GIN"), gin.getText());
                gin.doClick(0);
                assertEquals(Set.of(Tags.BaseSpirit.GIN), search.getSelectedBaseSpirits());
                assertEquals(i18n.text("spirits.count", 1), spirits.getText());

                ClickViewModel clickModel = new ClickViewModel();
                clickModel.getState().setInstructions("Stir gently. 自定义步骤。");
                ClickView details = new ClickView(clickModel, i18n);
                assertTrue(labels(details).contains(i18n.text("details")));
                assertTrue(labels(details).contains(i18n.text("instructions.label")));
                assertTrue(components(details, JTextArea.class).stream()
                        .anyMatch(area -> area.getText().equals("Stir gently. 自定义步骤。")));
                MainPageView home = new MainPageView(search, List.<Cocktail>of(),
                        new ClickController(input -> { }), details, searchModel, null, null, i18n);
                assertTrue(labels(home).contains(i18n.text("explore")));
                searchModel.getState().setErrorMessage("Please enter a cocktail name or select a filter.");
                searchModel.notifyStateChanged();
                assertTrue(labels(home).contains(i18n.text("message.filters")));
                assertFalse(button(home, i18n.text("customize")).isEnabled());

                CustomizeViewModel model = new CustomizeViewModel();
                model.getState().setInstructions("Keep this exact recipe text.");
                CustomizeView editor = new CustomizeView(model, new CustomizeController(input -> { }),
                        () -> "test-user", i18n);
                JTable table = components(editor, JTable.class).get(0);
                assertEquals(i18n.text("ingredient"), table.getColumnName(0));
                assertEquals(i18n.text("quantity"), table.getColumnName(1));
                assertEquals(i18n.text("unit"), table.getColumnName(2));
                button(editor, i18n.text("ingredient.add")).doClick(0);
                assertEquals(1, table.getRowCount());
                button(editor, i18n.text("ingredient.remove")).doClick(0);
                assertTrue(labels(editor).contains(i18n.text("ingredient.select")));
                model.getState().setErrorMessage("Please log in before saving a recipe.");
                model.notifyStateChanged();
                assertTrue(labels(editor).contains(i18n.text("message.login")));
                assertEquals("Keep this exact recipe text.",
                        components(editor, JTextArea.class).get(0).getText());
                assertEquals("My own text", i18n.message("My own text"));
                assertEquals("42: " + i18n.text("cocktail.missing"),
                        i18n.message("42: Cocktail does not exist."));
            }
        });
    }

    private static JButton button(Container parent, String text) {
        return components(parent, JButton.class).stream().filter(b -> b.getText().equals(text))
                .findFirst().orElseThrow(() -> new AssertionError("Missing button " + text));
    }

    private static List<String> labels(Container parent) {
        List<String> result = new ArrayList<>();
        for (JLabel label : components(parent, JLabel.class)) {
            result.add(label.getText());
        }
        return result;
    }

    private static <T extends Component> List<T> components(Container parent, Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Component child : parent.getComponents()) {
            if (type.isInstance(child)) {
                result.add(type.cast(child));
            }
            if (child instanceof Container) {
                result.addAll(components((Container) child, type));
            }
        }
        return result;
    }
}
