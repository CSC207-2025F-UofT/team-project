package view;

import interface_adapter.get_deck.GetDeckController;
import interface_adapter.get_deck.GetDeckState;
import interface_adapter.get_deck.GetDeckViewModel;
import interface_adapter.get_deck.GetDeckViewModelListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user is viewing a Deck.
 */
public class DeckView extends JPanel implements ActionListener, GetDeckViewModelListener {

    private final GetDeckViewModel getDeckViewModel;

    private final JLabel deckName = new JLabel();
    private final JLabel getDeckErrorField = new JLabel();

//    private final JButton home;
    private GetDeckController getDeckController = null;

    public DeckView(GetDeckViewModel getDeckViewModel) {

        this.getDeckViewModel = getDeckViewModel;
        this.getDeckViewModel.addListener(this);

        this.add(deckName);
//        this.add(home);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void stateChanged() {
        setFields(getDeckViewModel.getState());
        getDeckErrorField.setText(getDeckViewModel.getState().getDeckError());
    }

    private void setFields(GetDeckState state) {
        deckName.setText(state.getDeckName());
    }

    public void setGetDeckController(GetDeckController getDeckController) {
        this.getDeckController = getDeckController;
        this.getDeckController = getDeckController;
    }

}
