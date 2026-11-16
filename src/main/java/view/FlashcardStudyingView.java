package view;

import interface_adapters.flashcard_studying.FlashcardStudyingController;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlashcardStudyingView extends JPanel implements ActionListener {

    private final FlashcardStudyingController controller;

    private final JLabel title;
    private final JLabel deckTitle;
    private final JLabel question;
    private final JLabel answer;
    private final JButton knewIt;
    private final JButton stillLearning;
    private final JButton flip;

    /**
     * Constructs the Flashcard Generator View
     * Initializes all it's components, and connects the View to the Controller
     * @param controller the Controller for FlashcardGenerator
     */
    public FlashcardStudyingView(FlashcardStudyingController controller){
        this.controller = controller;
        this.setLayout(null);

        title = new JLabel("FLASHCARD GENERATOR!!!");
        title.setBounds(0,0,500,35);
        title.setFont(new Font("Chalkduster",Font.BOLD, 30));
        this.add(title);

        tutorialTitle = new JLabel("TUTORIAL");
        tutorialTitle.setBounds(10,70,200,35);
        tutorialTitle.setFont(new Font("Chalkduster",Font.BOLD, 25));
        this.add(tutorialTitle);

        tutorial = new JTextArea("Add Tutorial later...");
        tutorial.setEditable(false);
        tutorial.setBounds(10,120, 300,300);
        tutorial.setFont(new Font("Chalkduster",Font.BOLD, 12));
        tutorial.setOpaque(false);
        tutorial.setBackground(new Color(0,0,0,0));
        this.add(tutorial);

        textInput = new JTextPane();
        textInput.setEditable(true);
        textInput.setBounds(400,100, 375, 350);
        textInput.setFont(new Font("Arial", Font.PLAIN, 12));
        this.add(textInput);

        scrollPane = new JScrollPane(textInput);
        scrollPane.setBounds(400,100,375,350);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);

        generateButton = new JButton("Generate");
        generateButton.setBounds(420,470, 200,60);
        generateButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        generateButton.setBackground(Color.LIGHT_GRAY);
        generateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateButton.addActionListener(this);
        this.add(generateButton);

        boldButton = new JButton("Bold");
        boldButton.setBounds(630,470, 100,60);
        boldButton.setFont(new Font("Chalkduster", Font.PLAIN, 30));
        boldButton.setBackground(Color.LIGHT_GRAY);
        boldButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boldButton.addActionListener(this);
        this.add(boldButton);









    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boldButton) {
            toggleBold();
        }
        else if (e.getSource() == generateButton){
            String text = textInput.getText();


        }


    }
}