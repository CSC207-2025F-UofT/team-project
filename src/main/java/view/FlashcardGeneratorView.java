package view;

import interface_adapters.flashcard_generator.FlashcardGeneratorController;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interface_adapters.flashcard_generator.FlashcardGeneratorState;
import interface_adapters.flashcard_generator.FlashcardGeneratorViewModel;
import interface_adapters.flashcard_generator.FlashcardGeneratorViewModelListener;
import interface_adapters.home.HomeViewModel;
import use_case.flashcard_generator.FlashcardGeneratorInputData.TextPiece;

public class FlashcardGeneratorView extends JPanel implements ActionListener, FlashcardGeneratorViewModelListener {

    private final FlashcardGeneratorViewModel flashcardGeneratorViewModel;
    private FlashcardGeneratorController controller;
    private final HomeViewModel homeViewModel;

   private final JLabel title;
   private final JLabel tutorialTitle;
   private final JTextPane textInput;
   private final JTextArea tutorial;
   private final JScrollPane scrollPane;
   private final JButton generateButton;
   private final JButton boldButton;

    /**
     * Constructs the Flashcard Generator View
     * Initializes all it's components, and connects the View to the Controller
     *
     */
    public FlashcardGeneratorView(FlashcardGeneratorViewModel flashcardGeneratorViewModel, HomeViewModel homeViewModel){
        this.homeViewModel = homeViewModel;
        this.flashcardGeneratorViewModel = flashcardGeneratorViewModel;
        this.setLayout(null);

        TimerDisplayPanel timerPanel = new TimerDisplayPanel(homeViewModel);
        timerPanel.setLocation(10, 425);
        this.add(timerPanel);

        flashcardGeneratorViewModel.addListener(this); // register view as listener

        title = new JLabel("FLASHCARD GENERATOR!!!");
        title.setBounds(0,0,500,35);
        title.setFont(FontManager.getWinkle(30f));
        this.add(title);

        tutorialTitle = new JLabel("TUTORIAL");
        tutorialTitle.setBounds(10,70,200,35);
        tutorialTitle.setFont(FontManager.getGinny(25f));
        this.add(tutorialTitle);

        tutorial = new JTextArea("Add Tutorial later...");
        tutorial.setEditable(false);
        tutorial.setBounds(10,120, 300,300);
        tutorial.setFont(FontManager.getAppleGaramond(12f));
        tutorial.setOpaque(false);
        tutorial.setBackground(new Color(0,0,0,0));
        this.add(tutorial);

        textInput = new JTextPane();
        textInput.setEditable(true);
        textInput.setBounds(400,100, 375, 350);
        textInput.setFont(new Font("Arial", Font.PLAIN, 12));

        scrollPane = new JScrollPane(textInput);
        scrollPane.setBounds(400,100,375,350);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);

        generateButton = new JButton("Generate");
        generateButton.setBounds(420,470, 200,60);
        generateButton.setFont(FontManager.getWinkle(30f));
        generateButton.setBackground(Color.LIGHT_GRAY);
        generateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateButton.addActionListener(this);
        this.add(generateButton);

        boldButton = new JButton("Bold");
        boldButton.setBounds(630,470, 100,60);
        boldButton.setFont(FontManager.getWinkle(30f));        boldButton.setBackground(Color.LIGHT_GRAY);
        boldButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boldButton.addActionListener(this);
        this.add(boldButton);









    }

    /**
     * Sets the Controller for FlashcardGenerator
     * @param controller the Controller to be assigned to View
     */
    public void setController(FlashcardGeneratorController controller){
        this.controller = controller;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boldButton) {
            toggleBold();
        }
        else if (e.getSource() == generateButton){
            List<TextPiece> condensedList = extractText();
            controller.execute(condensedList);


        }


    }

    /**
     * Toggles/Removes bold for selected text in textInput.
     * If no text selected, then toggles/removes bold for subsequent typing.
     */
    private void toggleBold(){
        AttributeSet attributes = textInput.getCharacterAttributes();
        boolean isBold = StyleConstants.isBold(attributes);
        SimpleAttributeSet boldedText = new SimpleAttributeSet();
        StyleConstants.setBold(boldedText, !isBold);
        textInput.setCharacterAttributes(boldedText, false);
        textInput.requestFocusInWindow();

    }

    /**
     * Returns a list of TextPieces containing whether each text segment is bold/unbolded (and in order)
     * @return
     */
    private List<TextPiece> extractText(){
        List<TextPiece> pieces = new ArrayList<>();
        StyledDocument text = textInput.getStyledDocument();
        int i = 0;
        while (i < text.getLength()){
            Element element = text.getCharacterElement(i);
            boolean isBold = StyleConstants.isBold(element.getAttributes());
            int startOfPart = element.getStartOffset();
            int endOfPart = Math.min(element.getEndOffset(), text.getLength());
            try{
                String textToAdd = text.getText(startOfPart, endOfPart - startOfPart);
                TextPiece textPiece = new TextPiece(textToAdd, isBold);
                pieces.add(textPiece);

            } catch (BadLocationException e) {
                throw new IllegalStateException("Failed to extract text", e);
            }
            i = endOfPart;

            }
        return pieces;

        }

    @Override
    public void stateChanged() {

        FlashcardGeneratorState state = flashcardGeneratorViewModel.getState();

        if (state.getMessage() != null && !state.getMessage().isEmpty()) {

        JOptionPane.showMessageDialog(this, state.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }
}

