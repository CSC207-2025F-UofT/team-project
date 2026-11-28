package use_case.save_preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import entity.AdopterPreferences;
import use_case.check_compatibility.AdopterPreferencesDataAccessInterface;

class SavePreferencesInteractorTest {
    private final Map<String, AdopterPreferences> store = new HashMap<>();
    private final List<String> events = new ArrayList<>();
    private AdopterPreferences loaded = new AdopterPreferences(true, true, true, true, true, null);

    private final AdopterPreferencesDataAccessInterface dataAccess = new AdopterPreferencesDataAccessInterface() {
        @Override
        public Optional<AdopterPreferences> findByAdopterId(String adopterId) {
            return Optional.ofNullable(store.get(adopterId));
        }

        @Override
        public void save(String adopterId, AdopterPreferences preferences) {
            store.put(adopterId, preferences);
        }
    };

    private final SavePreferencesOutputBoundary presenter = new SavePreferencesOutputBoundary() {
        @Override
        public void presentSaved(AdopterPreferences preferences) {
            events.add("saved");
        }

        @Override
        public void presentLoaded(AdopterPreferences preferences) {
            loaded = preferences;
            events.add("loaded");
        }

        @Override
        public void presentError(String message) {
            events.add("error:" + message);
        }
    };

    private final SavePreferencesInteractor interactor = new SavePreferencesInteractor(dataAccess, presenter);

    @Test
    void savesTheAnswersForTheAdopter() {
        interactor.execute(new SavePreferencesInputData("a1", true, false, true, false, true));

        assertEquals(new AdopterPreferences(true, false, true, false, true, null), store.get("a1"));
        assertEquals(List.of("saved"), events);
    }

    @Test
    void savingWithoutLoggingInIsRefused() {
        interactor.execute(new SavePreferencesInputData("", true, false, true, false, true));

        assertTrue(store.isEmpty());
        assertEquals(List.of("error:Log in to save your profile."), events);
    }

    @Test
    void loadShowsWhatWasSaved() {
        final AdopterPreferences saved = new AdopterPreferences(true, false, true, false, false, null);
        store.put("a1", saved);

        interactor.load("a1");

        assertEquals(saved, loaded);
        assertEquals(List.of("loaded"), events);
    }

    @Test
    void loadWithNothingSavedShowsNull() {
        interactor.load("a1");

        assertNull(loaded);
    }

    @Test
    void loadWithoutLoggingInIsRefused() {
        interactor.load(null);

        assertEquals(List.of("error:Log in to save your profile."), events);
    }
}
