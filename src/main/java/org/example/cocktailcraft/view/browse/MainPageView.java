package org.example.cocktailcraft.view.browse;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.search.SearchView;
import java.util.function.Consumer;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;

public class MainPageView extends JPanel {
    private Cocktail selectedCocktail;
    private final JButton customizeButton = new JButton("Customize");

    public MainPageView(SearchView search, List<Cocktail> cocktails,
                        ClickController clickController, ClickView clickView) {
        super(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("CocktailCraft"), BorderLayout.WEST);
        header.add(search, BorderLayout.CENTER);
        JButton profile = new JButton("Profile");
        profile.setEnabled(false);
        header.add(profile, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel catalog = new JPanel(new BorderLayout());
        catalog.add(new JLabel("Explore Cocktails"), BorderLayout.NORTH);

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

                selectedCocktail = cocktails.get(index);
                customizeButton.setEnabled(true);

                clickController.execute(selectedCocktail.getId());
            }
        });

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
