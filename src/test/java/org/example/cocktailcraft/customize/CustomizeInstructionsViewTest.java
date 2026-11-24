package org.example.cocktailcraft.customize;

import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.data_access.FileCustomRecipeDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizePresenter;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeState;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.use_case.customize.CustomizeInteractor;
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomizeInstructionsViewTest {
    @TempDir
    Path directory;

    @Test
    void editsSavesReloadsAndReopensInstructionsWithoutChangingOriginal() throws Exception {
        Cocktail original = new CocktailJsonReader().read(
                "/bar-assistant/data/cocktails/8-amaro-sazerac/data.json");
        String originalInstructions = original.getInstructions();
        Path file = directory.resolve("recipes.json");
        SwingUtilities.invokeAndWait(() -> {
            try {
                CustomizeViewModel model = new CustomizeViewModel();
                CustomizeState state = model.getState();
                state.setOriginalCocktailId(original.getId());
                state.setName(original.getName());
                state.setIngredients(original.getIngredients());
                state.setInstructions(originalInstructions);
                FileCustomRecipeDataAccessObject dao = new FileCustomRecipeDataAccessObject(file);
                CustomizeView view = makeView(model, dao);
                JTextArea instructions = find(view, JTextArea.class, null);
                JTable table = find(view, JTable.class, null);
                assertEquals(originalInstructions, instructions.getText());

                table.setValueAt("5", 0, 1);
                String edited = "1. Use only 5 ml of Amaro Meletti.\n2. Stir with ice.\n3. Strain and serve.";
                instructions.setText(edited);
                find(view, JButton.class, "Save").doClick(0);
                assertTrue(state.isSaved());
                assertNull(state.getErrorMessage());
                assertEquals(edited, state.getInstructions());

                CustomRecipe reloaded = new FileCustomRecipeDataAccessObject(file)
                        .findById(state.getCustomRecipeId()).orElseThrow();
                assertEquals(edited, reloaded.getInstructions());
                assertEquals("5", reloaded.getIngredients().get(0).getAmount());
                assertEquals(originalInstructions, original.getInstructions());
                assertEquals("7.5", original.getIngredients().get(0).getAmount());

                CustomizeState reopened = new CustomizeState();
                reopened.setCustomRecipeId(reloaded.getId());
                reopened.setOriginalCocktailId(reloaded.getOriginalCocktailId());
                reopened.setName(reloaded.getName());
                reopened.setIngredients(reloaded.getIngredients());
                reopened.setInstructions(reloaded.getInstructions());
                model.setState(reopened);
                model.notifyStateChanged();
                assertEquals(edited, instructions.getText());
                instructions.setText("Shake with ice instead.");
                find(view, JButton.class, "Save").doClick(0);
                assertEquals(reloaded.getId(), model.getState().getCustomRecipeId());
                assertEquals("Shake with ice instead.", new FileCustomRecipeDataAccessObject(file)
                        .findById(reloaded.getId()).orElseThrow().getInstructions());
            } catch (Exception exception) {
                throw new AssertionError(exception);
            }
        });
    }

    @Test
    void validationFailureKeepsEditedInstructionsAndAllowsRetry() throws Exception {
        Cocktail original = new CocktailJsonReader().read("/cocktails/daiquiri.json");
        SwingUtilities.invokeAndWait(() -> {
            CustomizeViewModel model = new CustomizeViewModel();
            CustomizeState state = model.getState();
            state.setOriginalCocktailId(original.getId());
            state.setName(original.getName());
            state.setIngredients(original.getIngredients());
            state.setInstructions(original.getInstructions());
            CustomizeView view = makeView(model,
                    new FileCustomRecipeDataAccessObject(directory.resolve("retry.json")));
            JTextArea instructions = find(view, JTextArea.class, null);
            JTable table = find(view, JTable.class, null);
            String edited = "Keep my edited steps.\nUse a chilled glass.";
            instructions.setText(edited);
            table.setValueAt("0", 0, 1);
            find(view, JButton.class, "Save").doClick(0);
            assertEquals("Quantities must be greater than 0.", state.getErrorMessage());
            assertFalse(state.isSaved());
            assertEquals(edited, instructions.getText());
            assertEquals(edited, state.getInstructions());
            table.setValueAt("10", 0, 1);
            find(view, JButton.class, "Save").doClick(0);
            assertTrue(state.isSaved());
            assertEquals(edited, instructions.getText());
        });
    }

    @Test
    void fileAccessFailureKeepsEditedInstructions() throws Exception {
        Cocktail original = new CocktailJsonReader().read("/cocktails/daiquiri.json");
        Path invalidFile = directory.resolve("a-directory-not-a-json-file");
        java.nio.file.Files.createDirectory(invalidFile);
        SwingUtilities.invokeAndWait(() -> {
            CustomizeViewModel model = new CustomizeViewModel();
            CustomizeState state = model.getState();
            state.setOriginalCocktailId(original.getId());
            state.setName(original.getName());
            state.setIngredients(original.getIngredients());
            CustomizeView view = makeView(model, new FileCustomRecipeDataAccessObject(invalidFile));
            JTextArea instructions = find(view, JTextArea.class, null);
            String edited = "Do not lose my steps.\nServe with fresh ice.";
            instructions.setText(edited);
            find(view, JButton.class, "Save").doClick(0);
            assertEquals("Unable to load or save the recipe. Please try again.", state.getErrorMessage());
            assertFalse(state.isSaved());
            assertNull(state.getCustomRecipeId());
            assertEquals(edited, state.getInstructions());
            assertEquals(edited, instructions.getText());
            assertTrue(java.nio.file.Files.isDirectory(invalidFile));
        });
    }

    @Test
    void cancelCanDiscardInstructionEditsWithoutSaving() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            CustomizeViewModel model = new CustomizeViewModel();
            CustomizeView view = makeView(model,
                    new FileCustomRecipeDataAccessObject(directory.resolve("cancel.json")));
            AtomicBoolean returnedHome = new AtomicBoolean();
            view.setCancelAction(() -> {
                model.setState(new CustomizeState());
                model.notifyStateChanged();
                returnedHome.set(true);
            });
            JTextArea instructions = find(view, JTextArea.class, null);
            instructions.setText("Unsaved steps");
            find(view, JButton.class, "Cancel").doClick(0);
            assertTrue(returnedHome.get());
            assertEquals("", instructions.getText());
            assertFalse(java.nio.file.Files.exists(directory.resolve("cancel.json")));
        });
    }

    private CustomizeView makeView(CustomizeViewModel model, FileCustomRecipeDataAccessObject dao) {
        CustomizeInteractor interactor = new CustomizeInteractor(dao, new CustomizePresenter(model));
        return new CustomizeView(model, new CustomizeController(interactor), () -> "test-user");
    }

    private static <T extends JComponent> T find(Container root, Class<T> type, String text) {
        T result = findOrNull(root, type, text);
        if (result == null) {
            throw new AssertionError("Missing component: " + type.getSimpleName() + " " + text);
        }
        return result;
    }

    private static <T extends JComponent> T findOrNull(Container root, Class<T> type, String text) {
        for (Component component : root.getComponents()) {
            if (type.isInstance(component)
                    && (text == null || component instanceof JButton
                    && text.equals(((JButton) component).getText()))) {
                return type.cast(component);
            }
            if (component instanceof Container) {
                T result = findOrNull((Container) component, type, text);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
