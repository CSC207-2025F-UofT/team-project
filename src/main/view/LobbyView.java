package main.view;

import main.interface_adapters.lobby.LobbyController;
import main.interface_adapters.lobby.LobbyViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyView extends JPanel implements ActionListener {

    private final LobbyViewModel lobbyViewModel;
    private LobbyController lobbyController = null;
    private final JButton returnButton;

    public LobbyView(LobbyViewModel lobbyViewModel) {
        this.lobbyViewModel = lobbyViewModel;
        this.setLayout(null);
        this.setBackground(new Color(5,5,5));

        returnButton = new JButton("<- Return");
        ViewUtils.setFont(returnButton, "Figtree Black", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(returnButton, 0.02, 0.88, 0.2, 0.1);
        returnButton.setFocusPainted(false);
        returnButton.setForeground(Color.white);
        returnButton.setBackground(new Color(196, 43, 64));
        returnButton.addActionListener(this);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));

        this.add(returnButton);
    }

    public void setController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(returnButton)) {
            lobbyController.switchToMainMenuView();
        }
    }
}
