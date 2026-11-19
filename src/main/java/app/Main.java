package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.SignupView;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Stock Analysis Platform");

            SignupView signupView =
                    SignupAppBuilder.createSignupView();

            frame.setContentPane(signupView);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
