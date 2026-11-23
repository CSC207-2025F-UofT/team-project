package interface_adapter.manage_pet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import entity.PetSex;
import entity.Species;
import entity.Temperament;

/** Display state observed by the pet management screen. */
public final class ManagePetViewModel {
    /** Name the pet management screen is registered under in the view manager. */
    public static final String VIEW_NAME = "manage pets";

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private List<PetViewData> pets = List.of();
    private List<PetViewData> availablePets = List.of();
    private String message = "";
    private boolean error;

    /**
     * Registers a view to receive state changes.
     * @param listener the view's listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    /** @return the listings available to the admin */
    public List<PetViewData> getPets() {
        return pets;
    }

    /** @return the listings shown in the public preview */
    public List<PetViewData> getAvailablePets() {
        return availablePets;
    }

    /** @return feedback for the most recent action */
    public String getMessage() {
        return message;
    }

    /** @return whether the most recent action failed */
    public boolean hasError() {
        return error;
    }

    /** @return the valid species choices */
    public String[] getSpeciesOptions() {
        return optionNames(Species.values());
    }

    /** @return the valid sex choices */
    public String[] getSexOptions() {
        return optionNames(PetSex.values());
    }

    /** @return the valid temperament choices */
    public String[] getTemperamentOptions() {
        return optionNames(Temperament.values());
    }

    void showListings(List<PetViewData> listings, List<PetViewData> available, String feedback) {
        pets = List.copyOf(listings);
        availablePets = List.copyOf(available);
        message = feedback;
        error = false;
        changes.firePropertyChange("listings", null, pets);
    }

    void showError(String feedback) {
        message = feedback;
        error = true;
        changes.firePropertyChange("error", null, feedback);
    }

    private static String[] optionNames(Enum<?>[] choices) {
        final String[] names = new String[choices.length];
        for (int index = 0; index < choices.length; index++) {
            names[index] = choices[index].name();
        }
        return names;
    }
}
