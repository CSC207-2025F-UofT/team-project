package profilesetup;

import questionaire.QuestionOne;

import javax.swing.*;

public class PrivateInfo {
    public PrivateInfo() {
        JFrame firstpage = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JLabel age = new JLabel("age");
        JTextField age_result = new JTextField(5);
        panel1.add(age);
        panel1.add(age_result);

        JPanel panel2 = new JPanel();
        JLabel dpmt = new JLabel("Department");
        String[] departments = {"HR", "Admin", "Tech Support", "Consulting", "Legal"};
        JComboBox<String> dpmt_types = new JComboBox<>(departments);
        panel2.add(dpmt);
        panel2.add(dpmt_types);
        dpmt_types.addActionListener(e ->{
            String selected = (String) dpmt_types.getSelectedItem();
            //store in database?!

        });

        JPanel panel3 = new JPanel();
        JLabel gender = new JLabel("Gender");
        String[] genders = {"Male", "Female", "Non-Binary", "Other"};
        JComboBox<String> choose_gender = new JComboBox<>(genders);
        panel3.add(gender);
        panel3.add(choose_gender);
        choose_gender.addActionListener(e -> {
                    String selected = (String) choose_gender.getSelectedItem();
                    //store in database?!
        });

        JPanel next = new JPanel();
        JButton choose_next = new JButton("Next");
        choose_next.addActionListener(e ->{
            QuestionOne q1 = new QuestionOne();
        });
        next.add(choose_next);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(next);
        firstpage.add(panel);
        firstpage.setSize(500,500);
        firstpage.setVisible(true);

    }
}
