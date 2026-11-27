package view;

import interface_adapter.viewplayerstats.ViewPlayerStatsController;
import interface_adapter.viewplayerstats.ViewPlayerStatsState;
import interface_adapter.viewplayerstats.ViewPlayerStatsViewModel;

import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Displays player statistics and forwards refresh requests
 * to the controller.
 */
public final class ViewPlayerStatsView extends BackgroundPanel
        implements PropertyChangeListener {

    private final RoundedButton backButton =
            new RoundedButton("Back to Lobby", 180, 44);

    private final ViewPlayerStatsViewModel viewModel;

    // Display text prepared by the presenter.
    private final JLabel playerIdLabel = new JLabel();
    private final JLabel gamesPlayedLabel = new JLabel();
    private final JLabel gameWinLabel = new JLabel();
    private final JLabel winRateLabel = new JLabel();
    private final JLabel unoCallsMadeLabel = new JLabel();

    // Displays an error message when statistics cannot be loaded.
    private final JLabel errorLabel = new JLabel(" ");

    // Holds the achievement names currently displayed in the list.
    private final DefaultListModel<String> achievementsModel =
            new DefaultListModel<>();

    public ViewPlayerStatsView(ViewPlayerStatsViewModel viewModel,
                               ViewPlayerStatsController controller) {
        this.viewModel = Objects.requireNonNull(viewModel);
        Objects.requireNonNull(controller);

        setPreferredSize(new Dimension(1080, 610));
        setLayout(new GridBagLayout());

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(new Color(250, 247, 244));
        content.setBorder(
                BorderFactory.createEmptyBorder(24, 28, 24, 28));
        content.setPreferredSize(new Dimension(520, 500));

        JLabel title = new JLabel(
                "Player Statistics", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        content.add(title, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(5, 2, 12, 12));
        stats.setOpaque(false);

        addStatRow(stats, "Player", playerIdLabel);
        addStatRow(stats, "Games played", gamesPlayedLabel);
        addStatRow(stats, "Games won", gameWinLabel);
        addStatRow(stats, "Win rate", winRateLabel);
        addStatRow(stats, "UNO calls", unoCallsMadeLabel);

        JList<String> achievementsList =
                new JList<>(achievementsModel);

        JScrollPane achievementsPane =
                new JScrollPane(achievementsList);
        achievementsPane.setBorder(
                BorderFactory.createTitledBorder("Achievements"));
        achievementsPane.setPreferredSize(new Dimension(400, 110));

        JPanel details = new JPanel(new BorderLayout(0, 20));
        details.setOpaque(false);
        details.add(stats, BorderLayout.NORTH);
        details.add(achievementsPane, BorderLayout.CENTER);
        content.add(details, BorderLayout.CENTER);

        errorLabel.setForeground(new Color(160, 25, 25));

        RoundedButton refreshButton =
                new RoundedButton("Refresh", 180, 44);
        refreshButton.addActionListener(
                event -> controller.execute());

        backButton.setEnabled(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        buttons.add(refreshButton);
        buttons.add(backButton);

        JPanel actions = new JPanel(new BorderLayout(0, 12));
        actions.setOpaque(false);
        actions.add(errorLabel, BorderLayout.NORTH);
        actions.add(buttons, BorderLayout.SOUTH);
        content.add(actions, BorderLayout.SOUTH);

        add(content);
        updateView();
    }

    /**
     * Adds a statistic name and its value label to the grid.
     */
    private static void addStatRow(JPanel panel,
                                   String name,
                                   JLabel valueLabel) {
        panel.add(new JLabel(name));
        panel.add(valueLabel);
    }

    /**
     * Registers the action for returning to the lobby.
     *
     * @param listener handles the back navigation request
     */
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(
                Objects.requireNonNull(listener));
        backButton.setEnabled(true);
    }

    /**
     * Starts listening when this panel is attached
     * to a displayable container.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        viewModel.addPropertyChangeListener(this);
        updateView();
    }

    /**
     * Stops listening when this panel is removed
     * from its displayable container.
     */
    @Override
    public void removeNotify() {
        viewModel.removePropertyChangeListener(this);
        super.removeNotify();
    }

    /**
     * Updates the screen when the ViewModel publishes a new state.
     * Swing components are updated on the event dispatch thread.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("state".equals(event.getPropertyName())) {
            if (SwingUtilities.isEventDispatchThread()) {
                updateView();
            }
            else {
                SwingUtilities.invokeLater(this::updateView);
            }
        }
    }

    /**
     * Copies the current display state into the screen components.
     */
    private void updateView() {
        ViewPlayerStatsState state = viewModel.getStats();

        playerIdLabel.setText(state.getPlayerId());
        gamesPlayedLabel.setText(state.getGamesPlayedText());
        gameWinLabel.setText(state.getGameWinText());
        winRateLabel.setText(state.getWinRateText());
        unoCallsMadeLabel.setText(state.getUnoCallsMadeText());

        achievementsModel.clear();
        for (String achievement : state.getAchievements()) {
            achievementsModel.addElement(achievement);
        }

        String error = state.getErrorMessage();
        errorLabel.setText(error.isEmpty() ? " " : error);
    }
}