package org.example.cocktailcraft.app;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.junit.jupiter.api.Test;
import org.example.cocktailcraft.view.language.I18n;
import org.example.cocktailcraft.view.language.Language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class AppBuilderTest {
    @Test
    void chosenLanguageRemainsAcrossNavigation() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        for (Language language : Language.values()) {
            SwingUtilities.invokeAndWait(() -> {
                I18n i18n = new I18n(language);
                JFrame frame = new AppBuilder(language)
                        .addSearchView().addSearchUseCase().addClickUseCase()
                        .addCustomizeUseCase(() -> null).addMainPageView().build();
                try {
                    MainPageView home = find(frame.getContentPane(), MainPageView.class, null);
                    CustomizeView customize = find(frame.getContentPane(), CustomizeView.class, null);
                    find(home, JList.class, null).setSelectedIndex(0);
                    find(home, JButton.class, i18n.text("customize")).doClick(0);
                    assertTrue(customize.isVisible());
                    find(customize, JButton.class, i18n.text("save")).doClick(0);
                    assertNotNull(findOrNull(customize, JLabel.class, i18n.text("message.login")));
                    assertNotNull(findOrNull(frame.getContentPane(), JLabel.class,
                            i18n.text("language.current", language)));
                    find(customize, JButton.class, i18n.text("cancel")).doClick(0);
                    assertTrue(home.isVisible());
                    find(home, JButton.class, i18n.text("customize")).doClick(0);
                    assertTrue(customize.isVisible());
                } finally {
                    frame.dispose();
                }
            });
        }
    }

    @Test
    void opensSelectedRecipeInstructionsAndCancelReturnsHome() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        Cocktail expected = new CocktailJsonReader().readAll().get(0);
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new AppBuilder()
                    .addSearchView()
                    .addSearchUseCase()
                    .addClickUseCase()
                    .addCustomizeUseCase(() -> null)
                    .addMainPageView()
                    .build();
            try {
                assertEquals("CocktailCraft", frame.getTitle());
                MainPageView home = find(frame.getContentPane(), MainPageView.class, null);
                CustomizeView customize = find(frame.getContentPane(), CustomizeView.class, null);
                assertTrue(find(home, JButton.class, "Base spirits").isEnabled());
                assertTrue(find(home, JButton.class, "Flavors").isEnabled());
                assertTrue(find(home, JButton.class, "Alcohol content").isEnabled());
                for (String label : new String[] {"Search", "Clear"}) {
                    JButton searchButton = find(home, JButton.class, label);
                    assertTrue(searchButton.isEnabled(), label);
                    assertEquals(1, searchButton.getActionListeners().length, label);
                }
                assertFalse(find(home, JButton.class, "Profile").isEnabled());
                assertTrue(home.isVisible());
                assertFalse(customize.isVisible());
                JButton button = find(home, JButton.class, "Customize");
                assertFalse(button.isEnabled());
                find(home, JList.class, null).setSelectedIndex(0);
                assertTrue(button.isEnabled());
                button.doClick(0);
                assertTrue(customize.isVisible());
                assertFalse(home.isVisible());
                JTextArea instructions = find(customize, JTextArea.class, null);
                assertEquals(expected.getInstructions(), instructions.getText());
                instructions.setText("Unsaved replacement steps.");
                find(customize, JButton.class, "Cancel").doClick(0);
                assertTrue(home.isVisible());
                assertFalse(customize.isVisible());
                button.doClick(0);
                assertEquals(expected.getInstructions(), instructions.getText());
            } finally {
                frame.dispose();
            }
        });
    }

    private static <T extends Component> T find(Container parent, Class<T> type, String text) {
        T result = findOrNull(parent, type, text);
        assertNotNull(result, "Missing " + type.getSimpleName() + " " + text);
        return result;
    }

    private static <T extends Component> T findOrNull(Container parent, Class<T> type, String text) {
        for (Component child : parent.getComponents()) {
            if (type.isInstance(child)
                    && (text == null || child instanceof JButton && text.equals(((JButton) child).getText())
                            || child instanceof JLabel && text.equals(((JLabel) child).getText()))) {
                return type.cast(child);
            }
            if (child instanceof Container) {
                T result = findOrNull((Container) child, type, text);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
