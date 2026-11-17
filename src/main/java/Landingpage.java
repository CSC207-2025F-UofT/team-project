import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Landingpage extends JFrame {

    public static void main(String[] args) {
        //giant frame
        //JFrame page = new JFrame();
        //page.setSize(1000,1000);

        // frame
        JFrame frame = new JFrame();
        frame.setSize(350,200);

        // login
        JPanel login = new JPanel();
        login.setBackground(Color.LIGHT_GRAY);
        login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));
        login.setSize(15, 20);

        //username input
        JPanel username = new JPanel();
        username.add(new JLabel("Username:"));
        username.add(new JTextField(10));
        login.add(username);

        //password input
        JPanel password = new JPanel();
        password.add(new JLabel("Password:"));
        password.add(new JPasswordField(10));
        login.add(password);

        frame.add(login);

        //login or signup button
        JPanel buttons = new JPanel();
        JButton signup = new JButton("Sign Up");
        buttons.add(signup);
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Signup signupPage= new Signup();
                signupPage.setVisible(true);
            }
        });

        buttons.add(new JButton("Log In"));
        login.add(buttons);

        frame.setVisible(true);
    }

}


