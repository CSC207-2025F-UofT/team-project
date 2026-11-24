package questionaire;

import javax.swing.*;

public class QuestionOne {
    public QuestionOne() {
        JFrame question = new JFrame();
        JPanel question_panel = new JPanel();
        question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.Y_AXIS));
        JPanel add_question = new JPanel();
        JLabel q1 = new JLabel("What are your work habits?");
        String[] answers = {"Workaholic", "Slacker", "Good Work-Life Balance"};
        JComboBox<String> options = new JComboBox<>(answers);
        add_question.add(q1);
        add_question.add(options);

        JPanel next = new JPanel();
        JButton choose_next = new JButton("Next");
        next.add(choose_next);
        choose_next.addActionListener(e ->{
            QuestionTwo q2 = new QuestionTwo();
        });

        question_panel.add(add_question);
        question_panel.add(next);
        question.add(question_panel);
        question.setSize(500,500);
        question.setVisible(true);


    }
}
