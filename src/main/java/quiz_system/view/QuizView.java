package quiz_system.view;

import quiz_system.entity.AnswerOption;
import quiz_system.entity.Quiz;
import quiz_system.interface_adapters.QuizController;
import quiz_system.interface_adapters.QuizViewModel;
import quiz_system.usecase.QuizRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class QuizView extends JFrame {

    private final QuizRepository repo;
    private final QuizController controller;
    private final QuizViewModel vm;

    private final JLabel questionLabel = new JLabel();
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final JButton submitButton = new JButton("Submit");
    private final JLabel feedbackLabel = new JLabel("", SwingConstants.CENTER);

    private int currentQuizId;

    public QuizView(QuizRepository repo, QuizController controller, QuizViewModel vm) {
        this.repo = repo;
        this.controller = controller;
        this.vm = vm;

        setTitle("Quiz System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // question
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        // options
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 6, 6));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFocusPainted(false);
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        questionPanel.add(optionsPanel, BorderLayout.CENTER);

        // submit and feedback
        JPanel bottomPanel = new JPanel(new BorderLayout(6, 6));
        submitButton.addActionListener(this::handleSubmit);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(submitButton, BorderLayout.CENTER);
        bottomPanel.add(feedbackLabel, BorderLayout.SOUTH);

        add(questionPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadQuiz(int quizId) {
        this.currentQuizId = quizId;
        Quiz quiz = repo.findById(quizId);
        if (quiz == null) {
            return;
        }

        questionLabel.setText("<html><body style='width:470px;'>" +
                quiz.getQuestion().getText() + "</body></html>");

        // texts of each option
        var opts = quiz.getQuestion().getOptions();
        for (int i = 0; i < 4 && i < opts.size(); i++) {
            AnswerOption opt = opts.get(i);
            optionButtons[i].setText(opt.getText());
            optionButtons[i].setSelected(false);
        }
        optionGroup.clearSelection();
        feedbackLabel.setText("");
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

        controller.onSubmit(currentQuizId, selectedId);

        // feedback msg in separate frame
        JFrame feedbackFrame = new JFrame();
        feedbackFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        feedbackFrame.setSize(300, 150);
        feedbackFrame.setLocationRelativeTo(this); // center to main window

        JLabel messageLabel = new JLabel(vm.feedbackMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        feedbackFrame.add(messageLabel);
        feedbackFrame.setVisible(true);

        // close original quiz window automatically for incorrect answer
        if ("INCORRECT".equals(vm.status)) {
            this.dispose(); //get rid of quiz window
        }
    }
}