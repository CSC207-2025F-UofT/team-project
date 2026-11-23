package org.example.cocktailcraft.view.browse;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.view.click.ClickView;
import org.example.cocktailcraft.view.search.SearchView;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;

public class MainPageView extends JPanel {
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

        cocktailList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = cocktailList.getSelectedIndex();

                if (index != -1) {
                    Cocktail selectedCocktail = cocktails.get(index);
                    clickController.execute(selectedCocktail.getId());
                }
            }
        });

        catalog.add(new JScrollPane(cocktailList), BorderLayout.CENTER);

        add(catalog, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                catalog,
                clickView
        );

        add(splitPane, BorderLayout.CENTER);
    }

}
