package view;

import interface_adapter.game_running.GameRunningState;
import interface_adapter.game_running.GameRunningViewModel;
import interface_adapter.game_running.GameRunningViewModelListener;
import interface_adapter.game_running.LoadQuestionController;

import javax.swing.*;
import java.awt.*;

public class GameRunningView extends JPanel implements GameRunningViewModelListener {

    private final GameRunningViewModel gameRunningViewModel;
    private LoadQuestionController loadQuestionController = null;

    private final JLabel questionLabel = new JLabel("[Question Box]");
    private final JLabel[] answerLabels = new JLabel[8];
    private final JTextField answerInput = new JTextField(20);

    public GameRunningView(GameRunningViewModel gameRunningViewModel) {
        this.gameRunningViewModel = gameRunningViewModel;
        gameRunningViewModel.addListener(this);

        this.setUpDisplay();
    }

    private void setUpDisplay() {
        this.setSize(1000, 600);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Family Feud");
        title.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 48));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel questionPanel = new JPanel();
        questionLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        questionPanel.add(questionLabel);
        questionPanel.setMaximumSize(new Dimension(1000, 50));

        final JPanel answers = new JPanel();
        answers.setLayout(new BoxLayout(answers, BoxLayout.X_AXIS));

        final JPanel answersLeft = new JPanel();
        answersLeft.setLayout(new BoxLayout(answersLeft, BoxLayout.Y_AXIS));
        final JPanel answersRight = new JPanel();
        answersRight.setLayout(new BoxLayout(answersRight, BoxLayout.Y_AXIS));

        for (int i = 0; i < answerLabels.length; i++){
            answerLabels[i] = new JLabel("_");
            answerLabels[i].setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 28));
            answerLabels[i].setPreferredSize(new Dimension(100, 50));
            answerLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            if (i < answerLabels.length / 2) answersLeft.add(answerLabels[i]);
            else                             answersRight.add(answerLabels[i]);
        }

        answers.add(answersLeft);
        answers.add(Box.createRigidArea(new Dimension(200, 0))); // gap separating answers
        answers.add(answersRight);

        final JPanel inputPanel = new JPanel();
        JButton submitAnswerButton = new JButton("Submit");
        inputPanel.add(answerInput);
        inputPanel.add(submitAnswerButton);

        final JPanel loadQuestionButtonPanel = new JPanel();
        JButton loadQuestionButton = new JButton("Load Question");
        loadQuestionButton.addActionListener(
                evt -> {
                    this.loadQuestionController.execute();
                }
        );
        loadQuestionButtonPanel.add(loadQuestionButton);

        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(questionPanel);
        this.add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(answers);
        this.add(inputPanel);
        this.add(loadQuestionButtonPanel);
    }

    @Override
    public void stateChanged() {
        GameRunningState state = gameRunningViewModel.getState();
        this.questionLabel.setText(state.getQuestionText());
        for (int i = 0; i < state.getAnswerCount(); i++){
            answerLabels[i].setText("[ " + (i + 1) + " ]");
        }
        // TODO: When player gets answer correct, show correct answer (use case 3?)

        if (gameRunningViewModel.getState().getLoadQuestionError() != null) {
            JOptionPane.showMessageDialog(this, gameRunningViewModel.getState().getLoadQuestionError());
        }
    }

    public void setLoadQuestionController(LoadQuestionController loadQuestionController) {
        this.loadQuestionController = loadQuestionController;
    }
}
