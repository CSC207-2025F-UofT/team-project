package home.tests;
import home.HomeUI;

import javax.swing.*;

public class UITest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Intro JFrame Example");
        frame.setMinimumSize(new java.awt.Dimension(666, 1000));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.add(new HomeUI());
    }
}
