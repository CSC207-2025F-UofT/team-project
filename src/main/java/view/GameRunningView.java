package view;

import interface_adapter.game_running.GameRunningState;
import interface_adapter.game_running.GameRunningViewModel;
import interface_adapter.game_running.GameRunningViewModelListener;
import interface_adapter.game_running.LoadQuestionController;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.*;

public class GameRunningView extends JPanel implements GameRunningViewModelListener {

    private final GameRunningViewModel gameRunningViewModel;
    private LoadQuestionController loadQuestionController = null;

    private final JLabel question;

    private final JButton loadQuestion; // TODO: should prob also have an end game button, among others

    public GameRunningView(GameRunningViewModel gameRunningViewModel) {
        this.gameRunningViewModel = gameRunningViewModel;

        final JLabel title = new JLabel(GameRunningViewModel.VIEW_NAME);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel questionInfo = new JLabel("Question: ");
        question = new JLabel();
        question.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();
        loadQuestion = new JButton("New Question");
        buttons.add(loadQuestion);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        loadQuestion.addActionListener(
                evt -> {
                    if (evt.getSource().equals(loadQuestion)) { // TODO: what is this line for...
                        this.loadQuestionController.execute();
                    }
                }
        );

        this.add(title);
        this.add(buttons);
    }

    @Override
    public void stateChanged() {
        GameRunningState state = gameRunningViewModel.getState();
        this.question.setText(state.getQuestionText());

        if (gameRunningViewModel.getState().getLoadQuestionError() != null) {
            JOptionPane.showMessageDialog(this, gameRunningViewModel.getState().getLoadQuestionError());
        }
    }

    public void setLoadQuestionController(LoadQuestionController loadQuestionController) {
        this.loadQuestionController = loadQuestionController;
    }
}
