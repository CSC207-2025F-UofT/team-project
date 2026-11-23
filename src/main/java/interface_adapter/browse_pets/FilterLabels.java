package interface_adapter.browse_pets;

import entity.Temperament;

/**
 * Turns filter values into text people can read.
 */
public final class FilterLabels {

    private FilterLabels() {
    }

    /**
     * Writes a temperament tag in plain words, for example GOOD_WITH_KIDS becomes "Good with kids".
     *
     * @param temperament the tag
     * @return the label to show
     */
    public static String temperament(Temperament temperament) {
        final String words = temperament.name().toLowerCase().replace('_', ' ');
        return words.substring(0, 1).toUpperCase() + words.substring(1);
    }
}
