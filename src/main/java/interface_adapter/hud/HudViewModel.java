package interface_adapter.hud;

import interface_adapter.ViewModel;

/**
 * The shared heads-up display (day and stats) shown on every in-game
 * screen. Any presenter whose use case changes stats updates this one view
 * model, so StatsPanel never needs to know which use case ran.
 */
public class HudViewModel extends ViewModel<HudState> {

    public HudViewModel() {
        super("hud");
        setState(new HudState());
    }
}
