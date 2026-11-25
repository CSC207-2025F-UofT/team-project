package org.example.cocktailcraft.view.search;

import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchState;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JCheckBox;
import java.util.Map;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Set;
import java.util.EnumSet;
import java.util.function.Function;

public class SearchView extends JPanel {
    private final Map<Tags.BaseSpirit, JCheckBox> spiritOptions = new EnumMap<>(Tags.BaseSpirit.class);
    private final Map<Tags.Flavor, JCheckBox> flavorOptions = new EnumMap<>(Tags.Flavor.class);
    private final Map<Tags.AlcoholRange, JCheckBox> alcoholOptions = new EnumMap<>(Tags.AlcoholRange.class);
    private final SearchViewModel searchViewModel;
    private final JTextField nameInputField = new JTextField(22);
    private final JButton searchButton = new JButton("Search");
    private final JButton spiritSelector;
    private final JButton flavorSelector;
    private final JButton alcoholSelector;
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        JLabel nameLabel = new JLabel("Cocktail name:");
        nameLabel.setLabelFor(nameInputField);
        add(nameLabel);
        add(nameInputField);
        spiritSelector = addSelector("Base spirits", Tags.BaseSpirit.class, spiritOptions, spirit -> {
            String name = spirit.name().toLowerCase(Locale.ROOT);
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        });
        flavorSelector = addSelector("Flavors", Tags.Flavor.class, flavorOptions, this::flavorLabel);
        alcoholSelector = addSelector("Alcohol content", Tags.AlcoholRange.class, alcoholOptions, this::alcoholLabel);
        spiritSelector.setToolTipText("Results must contain every selected base spirit.");
        flavorSelector.setToolTipText(
                "Results must have at least one selected flavor; unlabelled recipes do not match.");
        alcoholSelector.setToolTipText("Match any selected range using the recipe's recorded ABV percentage.");

        searchButton.setEnabled(false);
        searchButton.addActionListener(event -> search());
        nameInputField.addActionListener(event -> search());
        add(searchButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(event -> {
            nameInputField.setText("");
            clearSelections(spiritOptions);
            clearSelections(flavorOptions);
            clearSelections(alcoholOptions);
            spiritSelector.setText("Base spirits");
            flavorSelector.setText("Flavors");
            alcoholSelector.setText("Alcohol content");
            if (searchController != null) {
                searchController.clear();
            } else {
                searchViewModel.setState(new SearchState());
                searchViewModel.notifyStateChanged();
            }
        });
        add(clearButton);
    }

    private void search() {
        if (searchController != null) {
            searchController.execute(nameInputField.getText(), getSelectedBaseSpirits(),
                    getSelectedFlavors(), getSelectedAlcoholRanges());
        }
    }

    private <T extends Enum<T>> JButton addSelector(String title, Class<T> type,
                                                   Map<T, JCheckBox> options, Function<T, String> label) {
        JButton selector = new JButton(title);
        JPopupMenu dropdown = new JPopupMenu();
        for (T value : type.getEnumConstants()) {
            JCheckBox option = new JCheckBox(label.apply(value));
            options.put(value, option);
            option.addActionListener(event -> {
                long count = options.values().stream().filter(JCheckBox::isSelected).count();
                selector.setText(count == 0 ? title : title + " (" + count + ")");
            });
            dropdown.add(option);
        }
        selector.setComponentPopupMenu(dropdown);
        selector.addActionListener(event -> dropdown.show(selector, 0, selector.getHeight()));
        add(selector);
        return selector;
    }

    private String flavorLabel(Tags.Flavor flavor) {
        switch (flavor) {
            case SWEET:
                return "Sweet";
            case SOUR_CITRUS:
                return "Sour / Citrus";
            case FRUITY_TROPICAL:
                return "Fruity / Tropical";
            case BITTER:
                return "Bitter";
            case HERBAL_FLORAL:
                return "Herbal / Floral";
            case SMOKY_SPICY:
                return "Smoky / Spicy";
            case COFFEE_NUTTY:
                return "Coffee / Nutty";
            case SAVORY:
                return "Savory";
            default:
                return "Dry";
        }
    }

    private String alcoholLabel(Tags.AlcoholRange range) {
        switch (range) {
            case ZERO:
                return "0% ABV";
            case LOW:
                return "Above 0% to below 10% ABV";
            case MEDIUM:
                return "10% to below 20% ABV";
            case HIGH:
                return "20% ABV and above";
            default:
                return "ABV not specified";
        }
    }

    private void clearSelections(Map<?, JCheckBox> options) {
        options.values().forEach(option -> option.setSelected(false));
    }

    private <T extends Enum<T>> Set<T> selectedOptions(Class<T> type, Map<T, JCheckBox> options) {
        Set<T> selected = EnumSet.noneOf(type);
        options.forEach((value, option) -> {
            if (option.isSelected()) {
                selected.add(value);
            }
        });
        return Set.copyOf(selected);
    }

    public Set<Tags.BaseSpirit> getSelectedBaseSpirits() {
        return selectedOptions(Tags.BaseSpirit.class, spiritOptions);
    }

    public Set<Tags.Flavor> getSelectedFlavors() {
        return selectedOptions(Tags.Flavor.class, flavorOptions);
    }

    public Set<Tags.AlcoholRange> getSelectedAlcoholRanges() {
        return selectedOptions(Tags.AlcoholRange.class, alcoholOptions);
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
        searchButton.setEnabled(searchController != null);
    }
}
