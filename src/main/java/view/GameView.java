package view;

import interface_adapter.hud.HudViewModel;
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

    public GameView(HudViewModel hudViewModel) {
        setLayout(new BorderLayout());
        add(new StatsPanel(hudViewModel), BorderLayout.NORTH);
        add(new JLabel("Your week at UofT begins.", SwingConstants.CENTER), BorderLayout.CENTER);

        JButton talkButton = new JButton("Talk to the Shawarma Lady");
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

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(talkButton);
        add(actionPanel, BorderLayout.SOUTH);
    }

    /** Given by AppBuilder once the talk use case has been built. */
    public void setTalkToNpcController(TalkToNpcController talkToNpcController) {
        this.talkToNpcController = talkToNpcController;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
