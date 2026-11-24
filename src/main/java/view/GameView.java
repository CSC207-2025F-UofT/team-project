package view;

import interface_adapter.hud.HudViewModel;
import interface_adapter.persistence.PersistenceController;
import interface_adapter.persistence.PersistenceViewModel;
import interface_adapter.talk_to_npc.TalkToNpcController;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The main in-game screen. It shows the stats panel on top and, since
 * Story 4, a Talk button at the bottom. Later stories fill the centre
 * (map, location).
 */
public class GameView extends JPanel {

    public static final String VIEW_NAME = "game";

    /**
     * The only character in the game so far. There are no locations yet,
     * so the Talk button always talks to her. Story 7 will replace this
     * with whoever is at the player's current building.
     */
    public static final String SHAWARMA_LADY_ID = "shawarma_lady";

    private TalkToNpcController talkToNpcController;
    private final JPanel footer = new JPanel(new BorderLayout());
    private final JPanel actionPanel = new JPanel(new FlowLayout());
    private final JButton talkButton = new JButton("Talk to the Shawarma Lady");

    public GameView(HudViewModel hudViewModel) {
        setLayout(new BorderLayout());
        add(new StatsPanel(hudViewModel), BorderLayout.NORTH);
        add(new JLabel("Your week at UofT begins.", SwingConstants.CENTER), BorderLayout.CENTER);

        talkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // The controller is only missing if the app was built
                // without the talk use case, and then there is nothing to do.
                if (talkToNpcController != null) {
                    talkToNpcController.start(SHAWARMA_LADY_ID);
                }
            }
        });

        actionPanel.add(talkButton);
        footer.add(actionPanel, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);
    }

    public void addSaveGame(PersistenceController controller, PersistenceViewModel feedback) {
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JLabel message = new JLabel(" ", SwingConstants.CENTER);
        footer.add(message, BorderLayout.SOUTH);
        saveButton.addActionListener(e -> controller.save());
        loadButton.addActionListener(e -> controller.load());
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        feedback.addPropertyChangeListener(event -> {
            var state = feedback.getState();
            talkButton.setEnabled(!state.busy());
            saveButton.setEnabled(!state.busy());
            loadButton.setEnabled(!state.busy());
            message.setText(state.message());
        });
    }

    /** Given by AppBuilder once the talk use case has been built. */
    public void setTalkToNpcController(TalkToNpcController talkToNpcController) {
        this.talkToNpcController = talkToNpcController;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
