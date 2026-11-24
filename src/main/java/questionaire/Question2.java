package questionaire;

import javax.swing.*;

public class Question2 {
    public Question2() {
        JFrame question = new JFrame();
        JPanel question_panel = new JPanel();
        question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.Y_AXIS));
        JPanel add_question = new JPanel();
        JLabel q2 = new JLabel("Favourite Beverage?");
        String[] answers = {"Coffee", "Tea", "Soda", "Water"};
        JComboBox<String> options = new JComboBox<>(answers);
        add_question.add(q2);
        add_question.add(options);

        JPanel next = new JPanel();
        JButton choose_next = new JButton("Next");
        next.add(choose_next);
        choose_next.addActionListener(e ->{
            Question3 q3 = new Question3();
        });

        question_panel.add(add_question);
        question_panel.add(next);
        question.add(question_panel);
        question.setSize(500,500);
        question.setVisible(true);


    }
}
