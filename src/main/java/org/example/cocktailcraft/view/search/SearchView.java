package org.example.cocktailcraft.view.search;

import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchState;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.view.language.I18n;
import org.example.cocktailcraft.view.language.Language;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JCheckBox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import java.util.EnumMap;
import java.util.Set;
import java.util.EnumSet;
import java.util.function.Function;

public class SearchView extends JPanel {
    private final Map<Tags.BaseSpirit, JCheckBox> spiritOptions = new EnumMap<>(Tags.BaseSpirit.class);
    private final Map<Tags.Flavor, JCheckBox> flavorOptions = new EnumMap<>(Tags.Flavor.class);
    private final Map<Tags.AlcoholRange, JCheckBox> alcoholOptions = new EnumMap<>(Tags.AlcoholRange.class);
    private final SearchViewModel searchViewModel;
    private final I18n i18n;
    private final JTextField nameInputField = new JTextField(22);
    private final JButton searchButton;
    private final JButton spiritSelector;
    private final JButton flavorSelector;
    private final JButton alcoholSelector;
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this(searchViewModel, new I18n(Language.ENGLISH));
    }

    public SearchView(SearchViewModel searchViewModel, I18n i18n) {
        super(new GridBagLayout());
        this.searchViewModel = searchViewModel;
        this.i18n = i18n;
        searchButton = new JButton(i18n.text("search"));
        JLabel nameLabel = new JLabel(i18n.text("cocktail.name"));
        nameLabel.setLabelFor(nameInputField);
        addControl(nameLabel, false);
        nameInputField.setMinimumSize(new Dimension(0, nameInputField.getPreferredSize().height));
        addControl(nameInputField, true);
        spiritSelector = addSelector("spirits", Tags.BaseSpirit.class, spiritOptions,
                spirit -> i18n.text("spirit." + spirit.name()));
        flavorSelector = addSelector("flavors", Tags.Flavor.class, flavorOptions,
                flavor -> i18n.text("flavor." + flavor.name()));
        alcoholSelector = addSelector("alcohol", Tags.AlcoholRange.class, alcoholOptions,
                range -> i18n.text("alcohol." + range.name()));
        spiritSelector.setToolTipText(i18n.text("spirits.tooltip"));
        flavorSelector.setToolTipText(i18n.text("flavors.tooltip"));
        alcoholSelector.setToolTipText(i18n.text("alcohol.tooltip"));

        searchButton.setEnabled(false);
        searchButton.addActionListener(event -> search());
        nameInputField.addActionListener(event -> search());
        addControl(searchButton, false);

        JButton clearButton = new JButton(i18n.text("clear"));
        clearButton.addActionListener(event -> {
            nameInputField.setText("");
            clearSelections(spiritOptions);
            clearSelections(flavorOptions);
            clearSelections(alcoholOptions);
            spiritSelector.setText(i18n.text("spirits"));
            flavorSelector.setText(i18n.text("flavors"));
            alcoholSelector.setText(i18n.text("alcohol"));
            if (searchController != null) {
                searchController.clear();
            } else {
                searchViewModel.setState(new SearchState());
                searchViewModel.notifyStateChanged();
            }
        });
        addControl(clearButton, false);
    }

    private void addControl(Component control, boolean expandable) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.weightx = expandable ? 1 : 0;
        constraints.fill = expandable ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        add(control, constraints);
    }

    private void search() {
        if (searchController != null) {
            searchController.execute(nameInputField.getText(), getSelectedBaseSpirits(),
                    getSelectedFlavors(), getSelectedAlcoholRanges());
        }
    }

    private <T extends Enum<T>> JButton addSelector(String key, Class<T> type,
                                                   Map<T, JCheckBox> options, Function<T, String> label) {
        String title = i18n.text(key);
        JButton selector = new JButton(i18n.text(key + ".count", (long) type.getEnumConstants().length));
        // Reserve space for the largest count so selecting options cannot grow the row.
        Dimension selectorSize = selector.getPreferredSize();
        selector.setPreferredSize(selectorSize);
        selector.setMinimumSize(selectorSize);
        selector.setText(title);
        JPopupMenu dropdown = new JPopupMenu();
        for (T value : type.getEnumConstants()) {
            JCheckBox option = new JCheckBox(label.apply(value));
            options.put(value, option);
            option.addActionListener(event -> {
                long count = options.values().stream().filter(JCheckBox::isSelected).count();
                selector.setText(count == 0 ? title : i18n.text(key + ".count", count));
            });
            dropdown.add(option);
        }
        selector.setComponentPopupMenu(dropdown);
        selector.addActionListener(event -> dropdown.show(selector, 0, selector.getHeight()));
        addControl(selector, false);
        return selector;
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
