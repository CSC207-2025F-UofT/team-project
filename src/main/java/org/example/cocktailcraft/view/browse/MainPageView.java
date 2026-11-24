package org.example.cocktailcraft.view.browse;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.interface_adapter.search.SearchState;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.search.SearchView;
import java.util.function.Consumer;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class MainPageView extends JPanel {
    private List<Cocktail> displayedCocktails;
    private Cocktail selectedCocktail;
    private final JButton customizeButton = new JButton("Customize");

    public MainPageView(SearchView search, List<Cocktail> cocktails,
                        ClickController clickController, ClickView clickView) {
        this(search, cocktails, clickController, clickView, null);
    }

    public MainPageView(SearchView search, List<Cocktail> cocktails,
                        ClickController clickController, ClickView clickView,
                        SearchViewModel searchViewModel) {
        super(new BorderLayout(12, 12));
        displayedCocktails = List.copyOf(cocktails);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("CocktailCraft"), BorderLayout.WEST);
        header.add(search, BorderLayout.CENTER);
        JButton profile = new JButton("Profile");
        profile.setEnabled(false);
        header.add(profile, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel catalog = new JPanel(new BorderLayout());
        JLabel catalogTitle = new JLabel("Explore Cocktails");
        catalog.add(catalogTitle, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();

        for (Cocktail cocktail : cocktails) {
            model.addElement(cocktail.getName());
        }

        JList<String> cocktailList = new JList<>(model);

        cocktailList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int index = cocktailList.getSelectedIndex();

                if (index == -1) {
                    selectedCocktail = null;
                    customizeButton.setEnabled(false);
                    return;
                }

                selectedCocktail = displayedCocktails.get(index);
                customizeButton.setEnabled(true);

                clickController.execute(selectedCocktail.getId());
            }
        });

        if (searchViewModel != null) {
            Runnable displaySearchState = () -> {
                SearchState state = searchViewModel.getState();
                displayedCocktails = state.getResults();
                model.clear();
                for (Cocktail cocktail : displayedCocktails) {
                    model.addElement(cocktail.getName());
                }
                if (!state.getErrorMessage().isEmpty()) {
                    catalogTitle.setText(state.getErrorMessage());
                } else if (!state.isSearchPerformed()) {
                    catalogTitle.setText("Explore Cocktails");
                } else if (state.getResults().isEmpty()) {
                    catalogTitle.setText("No cocktails found.");
                } else {
                    catalogTitle.setText("Search results (" + state.getResults().size() + ")");
                }
            };
            searchViewModel.addListener(displaySearchState::run);
            displaySearchState.run();
        }

        catalog.add(new JScrollPane(cocktailList), BorderLayout.CENTER);

        customizeButton.setEnabled(false);

        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.add(clickView, BorderLayout.CENTER);
        detailsPanel.add(customizeButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                catalog,
                detailsPanel
        );

        add(splitPane, BorderLayout.CENTER);
    }

    public void setCustomizeAction(Consumer<Cocktail> action) {
        customizeButton.addActionListener(event -> {
            if (selectedCocktail != null) {
                action.accept(selectedCocktail);
            }
        });
    }
}
