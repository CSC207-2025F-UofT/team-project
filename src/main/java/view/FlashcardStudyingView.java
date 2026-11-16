package view;

import interface_adapters.flashcard_studying.FlashcardStudyingController;
import interface_adapters.flashcard_studying.FlashcardStudyingViewModel;
import interface_adapters.flashcard_studying.FlashcardStudyingViewModelListener;
import interface_adapters.flashcard_studying.FlashcardStudyingState;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlashcardStudyingView extends JPanel implements ActionListener, FlashcardStudyingViewModelListener {

    private final FlashcardStudyingController controller;
    private final FlashcardStudyingViewModel viewModel;

    private final JLabel title;
    private final JLabel deckTitle;
//    private final JLabel question;
//    private final JLabel answer;
    private final JButton knewIt;
    private final JButton stillLearning;
    private final JButton flipButton;

    /**
     * Constructs the Flashcard Studying View
     * Initializes all it's components, and connects the View to the Controller
     * @param controller the Controller for FlashcardGenerator
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

        deckTitle = new JLabel("TUTORIAL");
        deckTitle.setBounds(10,70,200,35);
        deckTitle.setFont(new Font("Chalkduster",Font.BOLD, 25));
        this.add(deckTitle);


        flipButton = new JButton("Flip");
        flipButton.setBounds(100,410, 600,60);
        flipButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        flipButton.setBackground(Color.LIGHT_GRAY);
        flipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flipButton.addActionListener(this);
        this.add(flipButton);

        knewIt = new JButton("Knew it!");
        knewIt.setBounds(400,470, 300,60);
        knewIt.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        knewIt.setBackground(Color.GREEN);
        knewIt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        knewIt.addActionListener(this);
        this.add(knewIt);

        stillLearning = new JButton("Still learning...");
        stillLearning.setBounds(100,470, 300,60);
        stillLearning.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        stillLearning.setBackground(Color.RED);
        stillLearning.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stillLearning.addActionListener(this);
        this.add(stillLearning);








    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == boldButton) {
//            toggleBold();
//            System.out.println("Click " + evt.getActionCommand());
//        }
//        else if (e.getSource() == generateButton){
//            String text = textInput.getText();
//
//
//        }


    }

    @Override
    public void stateChanged() {
//      setFields(viewModel.getState());
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