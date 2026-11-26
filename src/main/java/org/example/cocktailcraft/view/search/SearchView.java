package org.example.cocktailcraft.view.search;

import org.example.cocktailcraft.view.language.I18n;
import org.example.cocktailcraft.view.language.Language;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class SearchView extends JPanel {
    private final Map<Tags.BaseSpirit, JCheckBox> spiritOptions = new EnumMap<>(Tags.BaseSpirit.class);
    private final SearchViewModel searchViewModel;
    private final JTextField nameInputField = new JTextField(22);
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this(searchViewModel, new I18n(Language.ENGLISH));
    }

    public SearchView(SearchViewModel searchViewModel, I18n i18n) {
        this.searchViewModel = searchViewModel;

        add(new JLabel(i18n.text("cocktail.name")));
        add(nameInputField);
        JButton selector = new JButton(i18n.text("spirits"));
        JPopupMenu dropdown = new JPopupMenu();
        for (Tags.BaseSpirit spirit : Tags.BaseSpirit.values()) {
            JCheckBox option = new JCheckBox(i18n.text("spirit." + spirit.name()));
            spiritOptions.put(spirit, option);
            option.addActionListener(event -> {
                int count = getSelectedBaseSpirits().size();
                selector.setText(count == 0 ? i18n.text("spirits") : i18n.text("spirits.count", count));
            });
            dropdown.add(option);
        }
        selector.setComponentPopupMenu(dropdown);
        selector.addActionListener(event -> dropdown.show(selector, 0, selector.getHeight()));
        add(selector);

        JButton searchButton = new JButton(i18n.text("search"));
        searchButton.addActionListener(event ->
                searchController.execute(nameInputField.getText(), getSelectedBaseSpirits()));
        add(searchButton);

        JButton clearButton = new JButton(i18n.text("clear"));
        clearButton.setEnabled(false);
        add(clearButton);
        JLabel messageLabel = new JLabel();
        add(messageLabel);
        searchViewModel.addListener(() ->
                messageLabel.setText(i18n.message(searchViewModel.getErrorMessage())));
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
