package view;

import javax.swing.*;
import java.awt.*;
import interface_adapter.*;
import entity.*;
import use_case.*;

public class BlackjackView extends JFrame {
    public BlackjackView() {
        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        BlackjackViewModel vm = new BlackjackViewModel();
        BlackjackPresenter p = new BlackjackPresenter(vm);
        BlackjackGame g = new BlackjackGame(100);
        BlackjackInteractor i = new BlackjackInteractor(g, p);
        BlackjackController c = new BlackjackController(i);

        JPanel panel = new JPanel();
        JButton deal = new JButton("Deal");
        JButton hit = new JButton("Hit");
        JButton stand = new JButton("Stand");

        deal.addActionListener(e -> c.onDeal(10));
        hit.addActionListener(e -> c.onHit());
        stand.addActionListener(e -> c.onStand());

        panel.add(deal);
        panel.add(hit);
        panel.add(stand);
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackjackView().setVisible(true));
    }
}