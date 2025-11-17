package app.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

//import org.json.JSONException;
//import app.Config;


/**
 * GUI class to run the GUI for the Grade App.
 */
public class Application {
    static final int ROWS = 4;
    static final int COLS = 2;
    static final int WIDTH = 850;
    static final int HEIGHT = 500;

    /**
     * Main method to run the GUI.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Initial setup for the program.
        // The config hides the details of which implementation of GradeDB
        // we are using in the program. If we were to use a different implementation
        // of GradeDB, this config is what we would change.
//        final Config config = new Config();

//        final GetTransactionUseCase getTransactionCase = config.getTransactionUseCase();

        // this is the code that runs to set up our GUI
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("Grade GUI App");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);

            final CardLayout cardLayout = new CardLayout();
            final JPanel cardPanel = new JPanel(cardLayout);

            final JPanel defaultCard = createDefaultCard();
            final JPanel getTransactionCard = createGetTransactionCard(frame);
            cardPanel.add(defaultCard, "DefaultCard");
            cardPanel.add(getTransactionCard, "GetTransactionCard");


            final JButton getGradeButton = new JButton("Transaction");
            getGradeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "GetTransactionCard");
                }
            });



            final JPanel buttonPanel = new JPanel();
            buttonPanel.add(getGradeButton);

            frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
//
//
        });
    }

    // utility methods that take care of setting up each JPanel to be displayed
    // in our GUI
    private static JPanel createDefaultCard() {
        final JPanel defaultCard = new JPanel();
        defaultCard.setLayout(new GridBagLayout());



        return defaultCard;
    }

    private static JPanel createGetTransactionCard(JFrame jFrame) {
        final JPanel getTransactionCard = new JPanel();
        getTransactionCard.setLayout(new GridLayout(ROWS, COLS));

        final JTextField usernameField = new JTextField(20);
        final JTextField courseField = new JTextField(20);
        final JButton getButton = new JButton("Get");

        final JLabel resultLabel = new JLabel();

        getButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String username = usernameField.getText();
                final String course = courseField.getText();

                JOptionPane.showMessageDialog(jFrame, String.format("Grade: %d", 80));

            }
        });

        getTransactionCard.add(new JLabel("Username:"));
        getTransactionCard.add(usernameField);
        getTransactionCard.add(new JLabel("Course:"));
        getTransactionCard.add(courseField);
        getTransactionCard.add(getButton);
        getTransactionCard.add(resultLabel);

        return getTransactionCard;
    }



}
