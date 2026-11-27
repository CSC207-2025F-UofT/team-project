package view;

import interface_adapter.lobby.LobbyViewModel;
import interface_adapter.lobby.LobbyViewModelListener;
import java.awt.event.ActionListener;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * Displays the UNO Lobby with the Logged-in username and buttons
 * for starting a game, viewing personal information and statistics and opening settings.
 */
public class LobbyView extends BackgroundPanel
        implements LobbyViewModelListener {
    private final JButton startGameButton = new JButton("Start Game");
    private final JButton personalInformationButton = new JButton();
    private final JButton playerStatsButton = new JButton("Personal Stats");
    private final LobbyViewModel lobbyViewModel;

    /**
     * Creates the Lobby and registers it as a listener to its view model.
     *
     * @param lobbyViewModel the view model containing the Lobby state.
     */
    public LobbyView(LobbyViewModel lobbyViewModel) {
        super("/backgrounds/Lobby_view.png");

        this.lobbyViewModel = lobbyViewModel;
        this.lobbyViewModel.addListener(this);

        setPreferredSize((new Dimension(1080, 610)));
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel personalPanel = new JPanel();
        personalPanel.setOpaque(false);
        personalInformationButton.setFont(
                new Font(Font.SANS_SERIF, Font.BOLD, 22)
        );
        personalInformationButton.setForeground(new Color(0, 51, 102));
        personalInformationButton.setBackground(new Color(200, 230, 250));
        personalInformationButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        personalInformationButton.setToolTipText("View personal information");
        personalPanel.add(personalInformationButton);

        playerStatsButton.setIcon(new ImageIcon(
                getClass().getResource("/icons/Stats Button.png")
        ));

        playerStatsButton.setText("");
        playerStatsButton.setContentAreaFilled(false);
        playerStatsButton.setBorderPainted(false);
        playerStatsButton.setOpaque(false);
        playerStatsButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        playerStatsButton.setToolTipText("View personal statistics");
        playerStatsButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        rightPanel.add(playerStatsButton);

        topPanel.add(personalPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        ImageIcon startIcon = new ImageIcon(
                getClass().getResource("/icons/Start Game Button.png")
        );

        int width = (int) (startIcon.getIconWidth() * 0.97);
        int height = (int) (startIcon.getIconHeight() * 0.97);

        startGameButton.setIcon(new ImageIcon(
                startIcon.getImage().getScaledInstance(
                        width, height, Image.SCALE_SMOOTH
                )
        ));
        startGameButton.setText("");
        startGameButton.setContentAreaFilled(false);
        startGameButton.setBorderPainted(false);
        startGameButton.setOpaque(false);
        startGameButton.setMargin(new Insets(0, 0, 0, 0));
        startGameButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        startGameButton.setToolTipText("Start a game with bots");

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.insets = new Insets(0, 0, 73, 22);

        centerPanel.add(startGameButton, constraints);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        stateChanged();
    }
    
    /**
     * Registeres a listener for the player statistics button.
     *
     * @param listener handles the statistics navigation request
     */
    public void addViewStatsListener(ActionListener listener) {
        playerStatsButton.addActionListener(listener);
    }

    /**
     * Updates the displayed username from the current lobby state.
     */
    @Override
    public void stateChanged() {
        personalInformationButton.setText(
                lobbyViewModel.getState().getUsername()
        );
    }

    /**
     * Gets the start game button.
     *
     * @return the start game button
     */
    public JButton getStartGameButton() {
        return startGameButton;
    }

    /**
     * Gets the personal information button.
     *
     * @return the personal information button
     */
    public JButton getPersonalInformationButton() {
        return personalInformationButton;
    }

    /**
     * Gets the player statistics button.
     *
     * @return the player statistics button
     */
    public JButton getPlayerStatsButton() {
        return playerStatsButton;
    }

}

