package view;

import interface_adapter.earn_reward_coins.EarnRewardCoinsController;
import interface_adapter.earn_reward_coins.EarnRewardCoinsState;
import interface_adapter.earn_reward_coins.EarnRewardCoinsViewModel;

import javax.swing.*;
import java.awt.*;

public class EarnRewardCoinsView extends JPanel{
    private final JLabel balanceLabel = new JLabel("Coin balance: ");
    private final JLabel messageLabel =
            new JLabel("Claim your reward for staying under budget yesterday.");
    private final JButton claimButton =
            new JButton("Claim Reward");
    public EarnRewardCoinsView(
            String userId,
            EarnRewardCoinsController controller,
            EarnRewardCoinsViewModel viewModel) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(balanceLabel);
        add(messageLabel);
        add(claimButton);

        claimButton.addActionListener(event -> controller.execute(userId));
        viewModel.addPropertyChangedListener(event -> {
            if (SwingUtilities.isEventDispatchThread()) {
                updateView(viewModel.getState());
            } else {
                SwingUtilities.invokeLater(
                        () -> updateView(viewModel.getState()));
            }
        });
        updateView(viewModel.getState());
        controller.loadStatus(userId);
    }
    private void updateView(EarnRewardCoinsState state) {
        if (state.getCoinBalance() != null) {
            balanceLabel.setText("Coin balance: " + state.getCoinBalance());
        }

        if (!state.getError().isEmpty()) {
            messageLabel.setText(state.getError());
        } else if (!state.getMessage().isEmpty()) {
            messageLabel.setText(state.getMessage());
        }

        claimButton.setEnabled(
                state.isEligible() && !state.isRewardClaimed());
        claimButton.setText(
                state.isRewardClaimed() ? "Reward Claimed" : "Claim Reward");
    }
}
