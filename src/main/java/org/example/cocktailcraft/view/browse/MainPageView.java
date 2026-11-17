package org.example.cocktailcraft.view.browse;

import org.example.cocktailcraft.view.search.SearchView;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPageView extends JPanel {
    public MainPageView(SearchView search) {
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
        catalog.add(new JLabel("Cocktail cards and images will appear here."), BorderLayout.CENTER);
        add(catalog, BorderLayout.CENTER);
    }
}
