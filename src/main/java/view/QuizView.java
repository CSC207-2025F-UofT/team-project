package view;

import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizState;
import interface_adapter.quiz.QuizViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class QuizView extends JPanel implements PropertyChangeListener {
    private final String viewName = "Quiz";
    private QuizController controller;
    private final QuizViewModel viewModel;

    private final JLabel questionLabel = new JLabel();
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final JLabel feedbackLabel = new JLabel("", SwingConstants.CENTER);

    private int currentQuizId;

    public QuizView(QuizViewModel viewModel) {
        this.viewModel = viewModel;

        // Listen to ViewModel changes
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // main panel (with padding around everything)
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        add(mainPanel, BorderLayout.CENTER);

        // question
        JPanel questionPanel = new JPanel(new BorderLayout(0, 12));
        questionPanel.setOpaque(false);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        // options
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        optionsPanel.setOpaque(false);
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        questionPanel.add(optionsPanel, BorderLayout.CENTER);

        // submit and feedback msg
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this::handleSubmit);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(0, 36));

        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bottomPanel.add(submitButton, BorderLayout.CENTER);
        bottomPanel.add(feedbackLabel, BorderLayout.SOUTH);

        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadQuiz(int quizId) {
        this.currentQuizId = quizId;
        controller.loadQuiz(quizId);
    }

    /**
     * Sets the controller for inventory
     */
    public void setQuizController(QuizController controller) {
        this.controller = controller;
    }
    /**
     * Returns the view name.
     */
    public String getViewName() {
        return viewName;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        QuizState state = viewModel.getState();

        // Check if quiz was loaded (question text is set)
        if (state.getQuestionText() != null) {
            updateQuizDisplay(state);
        }

        // Check if feedback should be shown
        if (state.getStatus() != null) {
            showFeedback(state);
        }
    }

    private void updateQuizDisplay(QuizState state) {
        questionLabel.setText("<html><body style='width:400px; text-align:left;'>" +
                state.getQuestionText() +
                "</body></html>");

        var optionTexts = state.getOptionTexts();
        for (int i = 0; i < 4 && i < optionTexts.size(); i++) {
            optionButtons[i].setText("<html><body style='width:400px;'>" +
                    optionTexts.get(i) +
                    "</body></html>");
            optionButtons[i].setVisible(true);
        }

        // Hide unused buttons
        for (int i = optionTexts.size(); i < 4; i++) {
            optionButtons[i].setVisible(false);
        }

        optionGroup.clearSelection();
        feedbackLabel.setText("");
    }

    private void showFeedback(QuizState state) {
        // Show feedback in separate frame
        JFrame feedbackFrame = new JFrame("Quiz Result");
        feedbackFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        feedbackFrame.setSize(300, 150);
        feedbackFrame.setLocationRelativeTo(this);

        JLabel messageLabel = new JLabel(state.getFeedbackMessage(), SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            feedbackFrame.dispose();
            // Switch to battle view if correct or incorrect (not warning)
            if ("CORRECT".equals(state.getStatus()) || "INCORRECT".equals(state.getStatus())) {
                controller.switchToBattleView();
            }
        });
        panel.add(okButton, BorderLayout.SOUTH);

        feedbackFrame.add(panel);
        feedbackFrame.setVisible(true);
    }

    private void showNoSelectionWarning() {
        JFrame warningFrame = new JFrame("Warning");
        warningFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        warningFrame.setSize(350, 150);
        warningFrame.setLocationRelativeTo(this);

        JLabel messageLabel = new JLabel("Question Not Answered", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> warningFrame.dispose());
        panel.add(okButton, BorderLayout.SOUTH);

        warningFrame.add(panel);
        warningFrame.setVisible(true);
    }

    private void handleSubmit(ActionEvent e) {
        ButtonModel selectedModel = optionGroup.getSelection();
        Integer selectedId = null;

        if (selectedModel != null) {
            int index = 1;
            var buttons = optionGroup.getElements();
            while (buttons.hasMoreElements()) {
                AbstractButton btn = buttons.nextElement();
                if (btn.getModel() == selectedModel) {
                    selectedId = index;
                    break;
                }
                index++;
            }
        }

        if (selectedId == null) {
            showNoSelectionWarning();
            return;
        }

        controller.onSubmit(currentQuizId, selectedId);
    }
}