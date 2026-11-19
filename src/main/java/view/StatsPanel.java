package view;

import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * The day and the four stats, shown on every in-game screen. It only
 * listens to the HudViewModel, so any use case that changes stats
 * refreshes it without this panel knowing which one ran.
 */
public class StatsPanel extends JPanel implements PropertyChangeListener {

    /** Bar range for Energy, Sanity and Academic Survival (Player clamps stats to this range). */
    private static final int BAR_MIN = 0;
    private static final int BAR_MAX = 100;

    private final JLabel dayLabel = new JLabel();
    private final JProgressBar energyBar = createBar();
    private final JProgressBar sanityBar = createBar();
    private final JProgressBar academicBar = createBar();
    private final JLabel moneyLabel = new JLabel();

    public StatsPanel(HudViewModel hudViewModel) {
        setLayout(new GridLayout(0, 2, 8, 4));
        setBorder(BorderFactory.createTitledBorder("Stats"));

        add(new JLabel("Day"));
        add(dayLabel);
        add(new JLabel("Energy"));
        add(energyBar);
        add(new JLabel("Sanity"));
        add(sanityBar);
        add(new JLabel("Academic Survival"));
        add(academicBar);
        add(new JLabel("Money"));
        add(moneyLabel);

        hudViewModel.addPropertyChangeListener(this);
        show(hudViewModel.getState());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        show((HudState) evt.getNewValue());
    }

    private void show(HudState state) {
        dayLabel.setText("Day " + state.getDay() + " of " + state.getTotalDays());
        setBar(energyBar, state.getEnergy());
        setBar(sanityBar, state.getSanity());
        setBar(academicBar, state.getAcademicSurvival());
        moneyLabel.setText("$" + state.getMoney());
    }

    private static void setBar(JProgressBar bar, int value) {
        bar.setValue(value);
        bar.setString(Integer.toString(value));
    }

    private static JProgressBar createBar() {
        JProgressBar bar = new JProgressBar(BAR_MIN, BAR_MAX);
        bar.setStringPainted(true);
        return bar;
    }
}
