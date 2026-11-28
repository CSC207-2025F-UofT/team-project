package interface_adapter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class SwingCommandDispatcherTest {
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void holdsAdmissionUntilRenderAndRecoversFromFailure(boolean fail) throws Exception {
        GameViewModel model = new GameViewModel();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(1);
        CountDownLatch released = new CountDownLatch(1);
        AtomicInteger rejectedRuns = new AtomicInteger();
        List<String> events = new CopyOnWriteArrayList<>();
        model.addPropertyChangeListener(e -> {
            events.add(model.getState().busy() ? "busy" : "released");
            if (!model.getState().busy()) released.countDown();
        });
        try (SwingCommandDispatcher dispatcher = new SwingCommandDispatcher(model, err ->
                SwingUtilities.invokeLater(() -> events.add("error")))) {
            dispatcher.dispatch(() -> {
                events.add(SwingUtilities.isEventDispatchThread() ? "EDT" : "worker");
                started.countDown();
                try { finish.await(5, TimeUnit.SECONDS); }
                catch (InterruptedException e) { throw new IllegalStateException(e); }
                if (fail) throw new IllegalStateException("failure");
                SwingUtilities.invokeLater(() -> {
                    events.add("render");
                    dispatcher.dispatch(rejectedRuns::incrementAndGet);
                });
            });
            assertTrue(started.await(5, TimeUnit.SECONDS));
            SwingUtilities.invokeAndWait(() -> dispatcher.dispatch(rejectedRuns::incrementAndGet));
            finish.countDown();
            assertTrue(released.await(5, TimeUnit.SECONDS));
            assertEquals(List.of("busy", "worker", fail ? "error" : "render", "released"), events);
            assertEquals(0, rejectedRuns.get());
            CountDownLatch accepted = new CountDownLatch(1);
            SwingUtilities.invokeAndWait(() -> dispatcher.dispatch(accepted::countDown));
            assertTrue(accepted.await(5, TimeUnit.SECONDS));
        } finally { finish.countDown(); }
    }
}
