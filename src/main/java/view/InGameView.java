package view;

import interface_adapter.declareuno.DeclareUnoController;
import interface_adapter.drawcard.DrawCardController;
import interface_adapter.ingame.InGameViewModel;
import interface_adapter.ingame.InGameViewModelListener;
import interface_adapter.playcard.PlayCardController;

import javax.swing.*;
import java.awt.*;

/**
 * The screen with a card table where the player plays uno with other players.
 */
public class InGameView extends JPanel implements InGameViewModelListener {

    private final InGameViewModel inGameViewModel;
    private DrawCardController drawCardController;
    private PlayCardController playCardController;
    private DeclareUnoController declareUnoController;

    public InGameView(InGameViewModel inGameViewModel) {
        this.inGameViewModel = inGameViewModel;
        initializeComponents();
    }

    private void initializeComponents() {
        JButton drawButton = new JButton("Draw Card");
        JButton playButton = new JButton("Play Card");
        JButton unoButton = new JButton("Declare UNO");

        drawButton.addActionListener(e ->
                drawCardController.execute()
        );

//        playButton.addActionListener(e ->
//                playCardController.execute();
//        );

        unoButton.addActionListener(e ->
                declareUnoController.execute()
        );
    }

    @Override
    public void stateChanged() {

    }

    // -------- Controller setters ----------

    public void setDrawCardController(DrawCardController drawCardController) {
        this.drawCardController = drawCardController;
    }

    public void setPlayCardController(PlayCardController playCardController) {
        this.playCardController = playCardController;
    }

    public void setDeclareUnoController(DeclareUnoController declareUnoController) {
        this.declareUnoController = declareUnoController;
    }
}
