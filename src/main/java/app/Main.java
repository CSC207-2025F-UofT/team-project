package app;

import interface_adapters.flashcard_generator.FlashcardGeneratorController;
import view.FlashcardGeneratorView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Study Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setVisible(true);
    }
}
