package interface_adapter.explore;

import use_case.explore.ItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Small helpers shared by the two Explore presenters for turning output data
 * into display text. Keeping them here avoids copying the same code into both
 * presenters.
 */
final class ExploreFormatter {

    /** Only static helpers, so this class is never instantiated. */
    private ExploreFormatter() {
    }

    /**
     * Gets the names of a list of items.
     *
     * @param items the items
     * @return their names, in the same order
     */
    static List<String> names(List<ItemData> items) {
        List<String> names = new ArrayList<>();
        for (ItemData item : items) {
            names.add(item.getName());
        }
        return names;
    }

    /**
     * Builds the text shown in the "examine item" dialog for each item, e.g.
     * <pre>
     * Potion of Healing
     * Type: Potion    Power: +2
     *
     * A red liquid that...
     * </pre>
     *
     * @param items the items
     * @return one block of text per item, in the same order
     */
    static List<String> details(List<ItemData> items) {
        List<String> details = new ArrayList<>();
        for (ItemData item : items) {
            details.add(item.getName() + "\n"
                    + "Type: " + capitalise(item.getType()) + "    Power: +" + item.getValue() + "\n\n"
                    + item.getDescription());
        }
        return details;
    }

    /**
     * Capitalises a word, e.g. "WEAPON" -> "Weapon".
     *
     * @param word the word
     * @return the capitalised word
     */
    static String capitalise(String word) {
        String lower = word.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
