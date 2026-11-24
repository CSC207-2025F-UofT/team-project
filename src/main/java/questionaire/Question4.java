package questionaire;

import javax.swing.*;

public class Question4 {
    public Question4() {
        JFrame question = new JFrame();
        JPanel question_panel = new JPanel();
        question_panel.setLayout(new BoxLayout(question_panel, BoxLayout.Y_AXIS));
        JPanel add_question = new JPanel();
        JLabel q4 = new JLabel("Dream Travel Destination:");
        String[] answers = {"Beach/Resort", "Hiking/Camping", "Cruise"};
        JComboBox<String> options = new JComboBox<>(answers);
        add_question.add(q4);
        add_question.add(options);

        JPanel complete_questionnaire = new JPanel();
        JButton finish = new JButton("Finish");
        complete_questionnaire.add(finish);
        //finish.addActionListener(e ->{
        //    Question2 q2 = new Question2();
        //}); takes you to matching page

        question_panel.add(add_question);
        question_panel.add(complete_questionnaire);
        question.add(question_panel);
        question.setSize(500,500);
        question.setVisible(true);


    }
}
