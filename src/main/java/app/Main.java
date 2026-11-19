package app;

import interface_adapters.flashcard_generator.FlashcardGeneratorController;
import interface_adapters.home.HomeViewModel;
import view.FlashcardGeneratorView;
import view.FontManager;
import view.TimerDisplayPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Study Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setVisible(true);
    }
}
