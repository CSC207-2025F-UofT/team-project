import profilesetup.PrivateInfo;

import javax.swing.*;
import java.awt.*;

public class Signup extends JFrame {
    public Signup() {
        JFrame frame = new JFrame();
        frame.setSize(350, 200);

        JPanel signup = new JPanel();
        signup.setBackground(Color.BLUE);
        signup.setLayout(new BoxLayout(signup, BoxLayout.Y_AXIS));
        signup.setSize(15, 20);

        //username input
        JPanel username = new JPanel();
        username.add(new JLabel("Username:"));
        username.add(new JTextField(10));
        signup.add(username);

        //password input
        JPanel password = new JPanel();
        password.add(new JLabel("Password:"));
        password.add(new JPasswordField(10));
        signup.add(password);

        // confirm password
        JPanel confirm_password = new JPanel();
        confirm_password.add(new JLabel("Confirm Password:"));
        confirm_password.add(new JPasswordField(10));
        signup.add(confirm_password);

        frame.add(signup);

        //login or signup button
        JPanel buttons = new JPanel();
        JButton signup_button = new JButton("Sign Up");
        buttons.add(signup_button);
        signup.add(buttons);

        signup_button.addActionListener(e ->{
            PrivateInfo privinfo = new PrivateInfo();
        });


        ImageIcon logo = new ImageIcon("src/main/java/templogo.png");
        //JPanel panel = new JPanel(logo);
       // frame.add(panel);

        frame.setVisible(true);
    }
}

