package view;

import interface_adapters.flashcard_generator.FlashcardGeneratorController;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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

    private Image backgroundImage;
    private ImageIcon catBox;
    private ImageIcon clock;
    private ImageIcon tutorialBG;

   private final JLabel title;
   private final JLabel title2;
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
        timerPanel.setBounds(300, 0, 225, 85);
        this.add(timerPanel);

        try{
            backgroundImage = ImageIO.read(new java.io.File("src/images/flashcard_generator.png"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load image", e);
        }



        flashcardGeneratorViewModel.addListener(this); // register view as listener

        title = new JLabel("FLASHCARD");
        title.setBounds(10, 20, 500, 40);
        title.setFont(FontManager.getWinkle(50f));
        this.add(title);

        title2 = new JLabel("GENERATOR!!!");
        title2.setBounds(10, 75, 500, 40);
        title2.setFont(FontManager.getWinkle(50f));
        this.add(title2);

        tutorialBG = new ImageIcon("src/images/tutorial_background.png");
        JLabel tutorialLabel = new JLabel(tutorialBG); // creates clock label
        tutorialLabel.setBounds(-15, 150, 370, 300);

        Image scaledTutorial = tutorialBG.getImage().getScaledInstance(370, 300, Image.SCALE_SMOOTH); // rescales the cat
        tutorialLabel.setIcon(new ImageIcon(scaledTutorial));
        this.add(tutorialLabel);


        tutorialTitle = new JLabel("TUTORIAL");
        tutorialTitle.setBounds(10,130,200,35);
        tutorialTitle.setFont(FontManager.getGinny(45f));
        this.add(tutorialTitle);

        tutorial = new JTextArea("• Write your question (make sure it's bolded using the BOLD button) \n" +
                "• Write your answer (make sure it's NOT bolded) \n" +
                "• Repeat this until you have completed your deck. Click generate \n" +
                "•Enjoy your new study set!! \n" +
                "*TIP* Make sure questions are in BOLD and answers are UNBOLDED for generation to work properly");
        tutorial.setEditable(false);
        tutorial.setBounds(10,170, 350,300);
        tutorial.setFont(FontManager.getAppleGaramond(20f));
        tutorial.setOpaque(false);
        tutorial.setBackground(new Color(0,0,0,0));
        tutorial.setLineWrap(true);
        tutorial.setWrapStyleWord(true);
        this.add(tutorial);

        this.setComponentZOrder(tutorialLabel, 1); // makes the clock lael appear above timer panel
        this.setComponentZOrder(tutorial, 0);

        textInput = new JTextPane();
        textInput.setEditable(true);
        textInput.setBounds(400,100, 375, 350);
        textInput.setFont(new Font("Arial", Font.PLAIN, 17));

        catBox = new ImageIcon("src/images/cat_textbox.png");
        JLabel catBoxLabel = new JLabel(catBox); // creeates cat label
        Image scaledCat = catBox.getImage().getScaledInstance(540, 490, Image.SCALE_SMOOTH); // rescales the cat
        catBoxLabel.setIcon(new ImageIcon(scaledCat));
        catBoxLabel.setBounds(305, 55, 540, 490);
        this.add(catBoxLabel);


        scrollPane = new JScrollPane(textInput);
        scrollPane.setBounds(408, 140, 324, 297);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);

        generateButton = new JButton("Generate");
        generateButton.setBounds(408, 475, 209, 40);
        generateButton.setFont(FontManager.getWinkle(30f));
        generateButton.setBackground(Color.LIGHT_GRAY);
        generateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateButton.addActionListener(this);
        this.add(generateButton);

        boldButton = new JButton("Bold");
        boldButton.setBounds(632, 475, 100, 40);
        boldButton.setFont(FontManager.getWinkle(30f));        boldButton.setBackground(Color.LIGHT_GRAY);
        boldButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boldButton.addActionListener(this);
        this.add(boldButton);


        clock = new ImageIcon("src/images/clock_design.png");
        JLabel clockLabel = new JLabel(clock); // creeates clock label
        clockLabel.setBounds(287, -7, 250, 100);

        Image scaledClock = clock.getImage().getScaledInstance(250,100, Image.SCALE_SMOOTH); // rescales the cat
        clockLabel.setIcon(new ImageIcon(scaledClock));
        this.add(clockLabel);



        this.setComponentZOrder(clockLabel, 0); // makes the clock lael appear above timer panel
        this.setComponentZOrder(timerPanel, 1);









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

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if (backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

