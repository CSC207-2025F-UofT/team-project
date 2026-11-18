package view.canvas;

import javax.swing.Timer;
import java.awt.Component;
import java.awt.Rectangle;

// when + where the faint "=" chip shows; the page only says which formula and what a tap does
final class FaintOffer {
    static final int DELAY_MILLIS = 1000;  // after the read, so ~1.5s from pen up

    // what the chip needs from the page
    interface Host {
        boolean stillWanted(long id, long gen);  // user didn't move on, no popup, formula still unanswered

        Rectangle spotFor(long id);              // area box at the formula's end, null if it's gone

        void tapped(long id);
    }

    private final FaintEquals chip;
    private final Host host;
    private final Timer timer;
    private long target = -1;
    private long gen;

    FaintOffer(Component canvas, Host host) {
        this.chip = new FaintEquals(canvas, this::tapped);
        this.host = host;
        timer = new Timer(DELAY_MILLIS, e -> show());
        timer.setRepeats(false);
    }

    FaintEquals chip() {
        return chip;
    }

    private void tapped() {
        long id = target;
        hide();
        host.tapped(id);
    }

    // read fine, no "=", but it can be worked out: chip after a longer wait
    void queue(long id, long gen) {
        target = id;
        this.gen = gen;
        timer.restart();
    }

    void hide() {
        timer.stop();
        chip.hideNow();
    }

    // pan / zoom / resize, chip stays at the formula's end
    void place() {
        if (!chip.isShown()) {
            return;
        }
        Rectangle spot = host.spotFor(target);
        if (spot == null) {
            hide();
        } else {
            chip.setBounds(spot);
            chip.repaint();
        }
    }

    // null when not up
    FaintEquals visible() {
        return chip.isShown() ? chip : null;
    }

    // tests use a short wait
    void setDelay(int millis) {
        timer.setInitialDelay(millis);
    }

    private void show() {
        Rectangle spot = host.spotFor(target);
        if (host.stillWanted(target, gen) && spot != null) {
            chip.showAt(spot);
        }
    }
}
