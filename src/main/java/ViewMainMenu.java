import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ViewMainMenu {

    static final int WIDTH = 300;
    static final int HEIGHT = 200;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {

                    JPanel welcomePanel = new JPanel();
                    welcomePanel.add(new JLabel("Welcome to CityGuesser"));

                    JPanel usernamePanel = new JPanel();
                    JTextField usernameField = new JTextField(10);
                    usernamePanel.add(new JLabel("Username:"));
                    usernamePanel.add(usernameField);

                    JPanel buttonPanel = new JPanel();
                    JButton play = new JButton("Play");
                    buttonPanel.add(play);
                    JButton stats = new JButton("Check Stats");
                    buttonPanel.add(stats);

                    play.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String username = usernameField.getText();
                        }
                    });
                    JPanel mainPanel = new JPanel();
                    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
                    mainPanel.add(welcomePanel);
                    mainPanel.add(usernamePanel);
                    mainPanel.add(buttonPanel);


                    JFrame menu = new JFrame("Main Menu");
                    menu.setContentPane(mainPanel);
                    menu.setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT));
                    menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    menu.pack();
                    menu.setVisible(true);
                });

    }

}
