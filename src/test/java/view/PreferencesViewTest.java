package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;
import interface_adapter.SwitchViewController;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_preferences.PreferencesViewModel;
import interface_adapter.save_preferences.SavePreferencesController;
import interface_adapter.save_preferences.SavePreferencesPresenter;
import use_case.save_preferences.SavePreferencesInteractor;

class PreferencesViewTest {
    private final PreferencesViewModel viewModel = new PreferencesViewModel();
    private AdopterPreferences saved;
    private final PreferencesView view = new PreferencesView(viewModel,
            new SavePreferencesController(new SavePreferencesInteractor(
                    new use_case.check_compatibility.AdopterPreferencesDataAccessInterface() {
                        @Override
                        public java.util.Optional<AdopterPreferences> findByAdopterId(String adopterId) {
                            return java.util.Optional.ofNullable(saved);
                        }

                        @Override
                        public void save(String adopterId, AdopterPreferences preferences) {
                            saved = preferences;
                        }
                    }, new SavePreferencesPresenter(viewModel))),
            () -> "a1", new SwitchViewController(new ViewManagerModel()), "gallery");

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

    private JButton button(String label) {
        return all(view, JButton.class).stream().filter(b -> b.getText().equals(label)).findFirst().orElseThrow();
    }

    @Test
    void savePassesTheTickedAnswers() {
        final List<JCheckBox> boxes = all(view, JCheckBox.class);
        assertEquals(5, boxes.size());
        boxes.get(0).setSelected(true);
        boxes.get(3).setSelected(true);

        button("Save profile").doClick();

        assertEquals(new AdopterPreferences(true, false, false, true, false, null), saved);
        assertTrue(all(view, JLabel.class).stream().anyMatch(l -> "Your profile is saved.".equals(l.getText())));
    }

    @Test
    void loadFillsTheBoxesFromWhatWasSaved() {
        saved = new AdopterPreferences(false, true, true, false, false, null);

        view.load();

        final List<JCheckBox> boxes = all(view, JCheckBox.class);
        assertEquals(List.of(false, true, true, false, false),
                List.of(boxes.get(0).isSelected(), boxes.get(1).isSelected(), boxes.get(2).isSelected(),
                        boxes.get(3).isSelected(), boxes.get(4).isSelected()));
    }
}
