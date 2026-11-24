package View.Questionaire;

import javax.swing.*;

public class QuestionThree {
    public QuestionThree() {
        JFrame question = new JFrame();
        JPanel question_panel = new JPanel();
        question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.Y_AXIS));
        JPanel add_question = new JPanel();
        JLabel q3 = new JLabel("How do you like to spend your evening?");
        String[] answers = {"Stay in", "Go out"};
        JComboBox<String> options = new JComboBox<>(answers);
        add_question.add(q3);
        add_question.add(options);

        JPanel next = new JPanel();
        JButton choose_next = new JButton("Next");
        next.add(choose_next);
        choose_next.addActionListener(e ->{
            View.Questionaire.QuestionFour q4 = new View.Questionaire.QuestionFour();
        });

        question_panel.add(add_question);
        question_panel.add(next);
        question.add(question_panel);
        question.setSize(500,500);
        question.setVisible(true);


    }
}
