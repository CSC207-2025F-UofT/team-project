package org.example.cocktailcraft.view.search;

import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
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

public class SearchView extends JPanel {
    private final Map<Tags.BaseSpirit, JCheckBox> spiritOptions = new EnumMap<>(Tags.BaseSpirit.class);
    private final SearchViewModel searchViewModel;
    private final JTextField nameInputField = new JTextField(22);
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;

        add(new JLabel("Cocktail name:"));
        add(nameInputField);
        JButton selector = new JButton("Base spirits");
        JPopupMenu dropdown = new JPopupMenu();
        for (Tags.BaseSpirit spirit : Tags.BaseSpirit.values()) {
            String name = spirit.name().toLowerCase(Locale.ROOT);
            JCheckBox option = new JCheckBox(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            spiritOptions.put(spirit, option);
            option.addActionListener(event -> {
                int count = getSelectedBaseSpirits().size();
                selector.setText(count == 0 ? "Base spirits" : "Base spirits (" + count + ")");
            });
            dropdown.add(option);
        }
        selector.setComponentPopupMenu(dropdown);
        selector.addActionListener(event -> dropdown.show(selector, 0, selector.getHeight()));
        add(selector);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(event ->
                searchController.execute(nameInputField.getText(), getSelectedBaseSpirits()));
        add(searchButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setEnabled(false);
        add(clearButton);
    }

    public Set<Tags.BaseSpirit> getSelectedBaseSpirits() {
        Set<Tags.BaseSpirit> selected = EnumSet.noneOf(Tags.BaseSpirit.class);
        spiritOptions.forEach((spirit, option) -> {
            if (option.isSelected()) {
                selected.add(spirit);
            }
        });
        return Set.copyOf(selected);
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

}
