package quiz_system.view;

import quiz_system.entity.AnswerOption;
import quiz_system.entity.Quiz;
import quiz_system.interface_adapters.QuizController;
import quiz_system.interface_adapters.QuizViewModel;
import quiz_system.usecase.QuizDataAccessInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QuizView extends JFrame {

    private final QuizDataAccessInterface repo;
    private final QuizController controller;
    private final QuizViewModel vm;

    private final JLabel questionLabel = new JLabel();
    private final ButtonGroup optionGroup = new ButtonGroup();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final JButton submitButton = new JButton("Submit");
    private final JLabel feedbackLabel = new JLabel("", SwingConstants.CENTER);

    private int currentQuizId;

    public QuizView(QuizDataAccessInterface repo, QuizController controller, QuizViewModel vm) {
        this.repo = repo;
        this.controller = controller;
        this.vm = vm;

        setTitle("Quiz System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 360);
        setLocationRelativeTo(null);
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
        Quiz quiz = repo.findById(quizId);
        if (quiz == null) {
            return;
        }

        questionLabel.setText("<html><body style='width:400px; text-align:left;'>" +
                quiz.getQuestion().getText() +
                "</body></html>");

        var opts = quiz.getQuestion().getOptions();
        for (int i = 0; i < 4 && i < opts.size(); i++) {
            AnswerOption opt = opts.get(i);
            optionButtons[i].setText("<html><body style='width:400px;'>" +
                    opt.getText() +
                    "</body></html>");
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
        feedbackFrame.setLocationRelativeTo(this);

        JLabel messageLabel = new JLabel(vm.feedbackMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        feedbackFrame.add(messageLabel);
        feedbackFrame.setVisible(true);

        // close original quiz window automatically
        if ("INCORRECT".equals(vm.status) || "CORRECT".equals(vm.status)) {
            this.dispose();
        }
    }
}
