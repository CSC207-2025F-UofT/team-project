package view;

import interface_adapter.hud.HudViewModel;
import interface_adapter.talk_to_npc.DialogueState;
import interface_adapter.talk_to_npc.DialogueViewModel;
import interface_adapter.talk_to_npc.TalkToNpcController;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The conversation screen: the stats on top, what the character says in
 * the middle, and the player's responses as buttons at the bottom.
 *
 * After the player picks a response, the same screen shows what happened
 * and the response buttons are replaced by a single Back button.
 *
 * This view only listens to the DialogueViewModel and calls the
 * TalkToNpcController. It never decides what a response does.
 */
public class DialogueView extends JPanel implements PropertyChangeListener {

    private final DialogueViewModel dialogueViewModel;
    private TalkToNpcController talkToNpcController;

    private final JLabel nameLabel = new JLabel();
    private final JTextArea speechArea = new JTextArea();
    private final JPanel buttonPanel = new JPanel();

    public DialogueView(DialogueViewModel dialogueViewModel, HudViewModel hudViewModel) {
        this.dialogueViewModel = dialogueViewModel;
        dialogueViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        add(new StatsPanel(hudViewModel), BorderLayout.NORTH);

        // A text area (not a label) because the speech can be a few
        // sentences long and needs to wrap onto several lines.
        speechArea.setEditable(false);
        speechArea.setLineWrap(true);
        speechArea.setWrapStyleWord(true);
        speechArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel speechPanel = new JPanel(new BorderLayout());
        speechPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        speechPanel.add(nameLabel, BorderLayout.NORTH);
        speechPanel.add(new JScrollPane(speechArea), BorderLayout.CENTER);
        add(speechPanel, BorderLayout.CENTER);

        // One column, as many rows as there are buttons.
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 4));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /** Given by AppBuilder once the talk use case has been built. */
    public void setTalkToNpcController(TalkToNpcController talkToNpcController) {
        this.talkToNpcController = talkToNpcController;
    }

    public String getViewName() {
        return dialogueViewModel.getViewName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DialogueState state = dialogueViewModel.getState();

        if (DialogueViewModel.ERROR_PROPERTY.equals(evt.getPropertyName())) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage());
        } else {
            redraw(state);
        }
    }

    /** Puts the current conversation on screen. */
    private void redraw(final DialogueState state) {
        nameLabel.setText(state.getNpcName());

        // The buttons are different every time, so throw the old ones
        // away and make new ones.
        buttonPanel.removeAll();

        if (state.isFinished()) {
            speechArea.setText(state.getResultMessage());
            buttonPanel.add(makeBackButton());
        } else {
            speechArea.setText(state.getText());
            List<String> optionIds = state.getOptionIds();
            List<String> optionTexts = state.getOptionTexts();
            for (int i = 0; i < optionIds.size(); i++) {
                buttonPanel.add(makeOptionButton(state, optionIds.get(i), optionTexts.get(i)));
            }
        }

        // Swing needs to be told that the panel's contents changed.
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private JButton makeOptionButton(final DialogueState state, final String optionId, String optionText) {
        JButton button = new JButton(optionText);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                talkToNpcController.choose(state.getNpcId(), state.getNodeId(), optionId);
            }
        });
        return button;
    }

    private JButton makeBackButton() {
        JButton button = new JButton("Back");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                talkToNpcController.leave();
            }
        });
        return button;
    }
}
