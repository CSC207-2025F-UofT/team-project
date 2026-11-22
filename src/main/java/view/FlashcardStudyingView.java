package view;

import interface_adapters.flashcard_studying.FlashcardStudyingController;
import interface_adapters.flashcard_studying.FlashcardStudyingViewModel;
import interface_adapters.flashcard_studying.FlashcardStudyingViewModelListener;
import interface_adapters.flashcard_studying.FlashcardStudyingState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlashcardStudyingView extends JPanel implements ActionListener, FlashcardStudyingViewModelListener {

    private final FlashcardStudyingController controller;
    private final FlashcardStudyingViewModel viewModel;

    private final JLabel title;
    private final JTextField deckTitle;
    private final JLabel titleLabel;
    private final JButton startButton;
    private final JLabel displayText;
    private final JButton knewItButton;
    private final JButton stillLearningButton;
    private final JButton flipButton;

    /**
     * Constructs the Flashcard Studying View
     * Initializes all it's components, and connects the View to the Controller
     * @param controller the Controller for FlashcardStudying
     */
    public FlashcardStudyingView(FlashcardStudyingController controller, FlashcardStudyingViewModel viewModel){
        this.controller = controller;
        this.setLayout(null);

        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        title = new JLabel("Time to review!");
        title.setBounds(0,0,500,35);
        title.setFont(new Font("Chalkduster",Font.BOLD, 30));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(title);

        deckTitle = new JTextField(20);
        deckTitle.setBounds(250,65,200,35);
        deckTitle.setFont(new Font("Chalkduster",Font.BOLD, 25));
        this.add(deckTitle);

        titleLabel = new JLabel("Deck name: ");
        titleLabel.setBounds(100,65,150,35);
        titleLabel.setFont(new Font("Chalkduster",Font.BOLD, 25));
        this.add(titleLabel);

        startButton = new JButton("Start");
        startButton.setBounds(600,65, 100,35);
        startButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        startButton.setBackground(Color.BLUE);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(this);
        this.add(startButton);


        flipButton = new JButton("Flip");
        flipButton.setBounds(100,410, 600,60);
        flipButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        flipButton.setBackground(Color.BLUE);
        flipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flipButton.addActionListener(this);
        this.add(flipButton);

        knewItButton = new JButton("Knew it!");
        knewItButton.setBounds(400,470, 300,60);
        knewItButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        knewItButton.setBackground(Color.GREEN);
        knewItButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        knewItButton.addActionListener(this);
        this.add(knewItButton);

        stillLearningButton = new JButton("Still learning...");
        stillLearningButton.setBounds(100,470, 300,60);
        stillLearningButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        stillLearningButton.setBackground(Color.RED);
        stillLearningButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stillLearningButton.addActionListener(this);
        this.add(stillLearningButton);

        displayText = new JLabel("");
        displayText.setHorizontalAlignment(SwingConstants.CENTER);
        displayText.setVerticalAlignment(SwingConstants.CENTER);
        displayText.setBounds(100,100,600,300);
        displayText.setFont(new Font("Chalkduster",Font.BOLD, 30));
        displayText.setAlignmentX(Component.CENTER_ALIGNMENT);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        displayText.setOpaque(true);
        displayText.setBackground(Color.LIGHT_GRAY);
        displayText.setForeground(Color.BLACK);
        displayText.setBorder(border);
        this.add(displayText);









    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            System.out.println("Click " + e.getActionCommand());
            final FlashcardStudyingState currentState = viewModel.getState();
            currentState.setText(deckTitle.getText());
            viewModel.setState(currentState);
            controller.execute("Start", deckTitle.getText());
        }

        if (e.getSource() == flipButton) {
            System.out.println("Click " + e.getActionCommand());
            final FlashcardStudyingState currentState = viewModel.getState();
            controller.execute("Flip");
        }
        else if (e.getSource() == knewItButton) {
            System.out.println("Click " + e.getActionCommand());
            controller.execute("Knew it");
        } else if (e.getSource() == stillLearningButton) {
            System.out.println("Click " + e.getActionCommand());
            controller.execute("Still learning");
        }


    }

    @Override
    public void stateChanged() {
        displayText.setText(viewModel.getState().getText());
        title.setText(viewModel.getState().getDeckTitle());
    }

//    private void setFields(FlashcardStudyingState state) {
//        title.setText(state.getUsername());
//    }
//
//    public void setLoginController(LoginController loginController) {
//        this.loginController = loginController;
//    }
}