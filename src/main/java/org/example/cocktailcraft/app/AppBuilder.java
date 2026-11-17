package org.example.cocktailcraft.app;

import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.search.SearchView;

import javax.swing.JFrame;

public class AppBuilder {
    public JFrame build() {
        JFrame frame = new JFrame("CocktailCraft");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainPageView(new SearchView()));
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
