
package view;

import javax.swing.*;
import java.awt.*;
import interface_adapter.*;
import entity.*;
import use_case.*;

public class MinesView extends JFrame {
    public MinesView() {
        setTitle("Mines");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        MinesViewModel vm = new MinesViewModel();
        MinesPresenter p = new MinesPresenter(vm);
        MinesGame g = new MinesGame(5, 5);
        MinesInteractor i = new MinesInteractor(g, p);
        MinesController c = new MinesController(i);

        JPanel grid = new JPanel(new GridLayout(5, 5));
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                JButton b = new JButton();
                final int fx = x, fy = y;
                b.addActionListener(e -> c.onReveal(fx, fy));
                grid.add(b);
            }
        }
        add(grid);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MinesView().setVisible(true));
    }
}