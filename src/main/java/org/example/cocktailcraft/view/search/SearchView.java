package org.example.cocktailcraft.view.search;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchView extends JPanel {
    public SearchView() {
        add(new JLabel("Cocktail name:"));
        add(new JTextField(22));
        for (String label : new String[] {"Base spirits", "Search", "Clear"}) {
            JButton button = new JButton(label);
            button.setEnabled(false);
            add(button);
        }
    }
}
