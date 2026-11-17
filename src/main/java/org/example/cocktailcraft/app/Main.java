package org.example.cocktailcraft.app;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppBuilder().build().setVisible(true));
    }
}
