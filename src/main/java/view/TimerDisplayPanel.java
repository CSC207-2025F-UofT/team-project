package view;

import interface_adapters.home.HomeViewModel;
import interface_adapters.home.HomeViewModelListener;

import javax.swing.*;
import java.awt.*;

/***
 * Displays the timer in a panel and updates it whenever the Home View Model changes.
 */
public class TimerDisplayPanel extends JPanel implements HomeViewModelListener {
    private final HomeViewModel homeViewModel;
    private final JLabel timerLabel;

    public TimerDisplayPanel(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
        homeViewModel.addListener(this);

        this.setBackground(Color.WHITE);
        timerLabel = new JLabel(homeViewModel.getState().getTimerDisplay());
        timerLabel.setFont(FontManager.getWinkle(100f));
        this.add(timerLabel);
    }

    /**
     * Updates the timer label when the Home View Model changes.
     */
    @Override
    public void stateChanged() {
        timerLabel.setText(homeViewModel.getState().getTimerDisplay());
    }
}
