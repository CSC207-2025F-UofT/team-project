package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WelcomeView {
    static final int WIDTH = 300;
    static final int HEIGHT = 200;

    //This is the case if the user is playing for the first time.
    //If they've played before, they should be greeted by name
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            () -> {

                JPanel welcomePanel = new JPanel();
                welcomePanel.add(new JLabel("Welcome, [username]"));

                JPanel buttonPanel = new JPanel();
                JButton play = new JButton("Play");
                buttonPanel.add(play);
                JButton stats = new JButton("View Stats");
                buttonPanel.add(stats);

                play.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //mainMenuViewModel.setState();
                    }
                });
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                mainPanel.add(welcomePanel);
                mainPanel.add(buttonPanel);


                JFrame menu = new JFrame("Welcome");
                menu.setContentPane(mainPanel);
                menu.setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT));
                menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menu.pack();
                menu.setVisible(true);
            });
    }
}
