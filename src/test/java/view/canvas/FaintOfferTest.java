package view.canvas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

// chip timing + placement, page faked
class FaintOfferTest {
    private final AtomicBoolean wanted = new AtomicBoolean(true);
    private final AtomicReference<Rectangle> spot = new AtomicReference<>(new Rectangle(10, 10, 20, 20));
    private final List<Long> taps = new ArrayList<>();
    private FaintOffer offer;

    @BeforeEach
    void setUp() throws Exception {
        onEdt(() -> {
            offer = new FaintOffer(new JPanel(), new FaintOffer.Host() {
                @Override
                public boolean stillWanted(long id, long gen) {
                    return wanted.get();
                }

                @Override
                public Rectangle spotFor(long id) {
                    return spot.get();
                }

                @Override
                public void tapped(long id) {
                    taps.add(id);
                }
            });
            offer.setDelay(20);
        });
    }

    private static void onEdt(Runnable r) throws Exception {
        SwingUtilities.invokeAndWait(r);
    }

    private FaintEquals visible() throws Exception {
        AtomicReference<FaintEquals> seen = new AtomicReference<>();
        onEdt(() -> seen.set(offer.visible()));
        return seen.get();
    }

    private FaintEquals shownFor(long id) throws Exception {
        onEdt(() -> offer.queue(id, 0));
        for (int i = 0; i < 50 && visible() == null; i++) {
            Thread.sleep(20);
        }
        return visible();
    }

    @Test
    void showsAfterTheDelayAtTheSpot() throws Exception {
        FaintEquals chip = shownFor(1);
        assertNotNull(chip);
        assertEquals(spot.get(), chip.getBounds());
    }

    @Test
    void staysHiddenOnceTheUserMovedOn() throws Exception {
        wanted.set(false);
        onEdt(() -> offer.queue(1, 0));
        Thread.sleep(200);
        assertNull(visible());
    }

    @Test
    void placeHidesItWhenTheFormulaIsGone() throws Exception {
        assertNotNull(shownFor(1));
        spot.set(null);
        onEdt(offer::place);
        assertNull(visible());
    }

    @Test
    void tapGoesToTheHostWithItsFormula() throws Exception {
        FaintEquals chip = shownFor(7);
        onEdt(() -> {
            chip.dispatchEvent(new MouseEvent(chip, MouseEvent.MOUSE_PRESSED, 0, 0, 10, 10, 1, false, MouseEvent.BUTTON1));
            chip.dispatchEvent(new MouseEvent(chip, MouseEvent.MOUSE_RELEASED, 0, 0, 10, 10, 1, false, MouseEvent.BUTTON1));
        });
        assertEquals(List.of(7L), taps);
        assertNull(visible());
    }
}
