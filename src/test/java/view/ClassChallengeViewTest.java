package view;

import app.ClassChallengeFactory;
import entity.Question;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import support.FakeChallengeState;
import use_case.challenge.ChallengeCourse;
import use_case.question.QuestionGateway;
import use_case.question.QuestionTopic;
import static org.junit.jupiter.api.Assertions.*;

class ClassChallengeViewTest {
    @Test void completesThroughSwingWithoutBlockingEdtOrAcceptingDuplicateStarts() throws Exception {
        FakeChallengeState state = new FakeChallengeState();
        state.assign(new ChallengeCourse("cs-foundation", "Programming Foundation",
                QuestionTopic.PROGRAMMING_FOUNDATION, null));
        CountDownLatch entered = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        QuestionGateway gateway = (topic, purpose, count) -> {
            assertFalse(SwingUtilities.isEventDispatchThread());
            entered.countDown();
            try { assertTrue(release.await(5, TimeUnit.SECONDS)); }
            catch (InterruptedException exception) { Thread.currentThread().interrupt(); fail(exception); }
            return List.of(new Question("a", "Which value is an integer?", List.of("3", "three", "true", "null"), 0, null),
                    new Question("b", "Which value is a boolean?", List.of("true", "3", "three", "null"), 0, null),
                    new Question("c", "Which operation adds two numbers?", List.of("+", "-", "*", "/"), 0, null));
        };
        AtomicBoolean left = new AtomicBoolean();
        AtomicReference<ClassChallengeView> holder = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            ClassChallengeView view = ClassChallengeFactory.create(state, gateway, () -> left.set(true));
            holder.set(view);
            view.refresh();
        });
        ClassChallengeView view = holder.get();
        awaitButton(view, "challenge.select");
        snapshot(view, "courses");
        click(view, "challenge.select");
        awaitButton(view, "challenge.start");
        snapshot(view, "confirm");
        click(view, "challenge.start");
        assertTrue(entered.await(3, TimeUnit.SECONDS));
        try {
            SwingUtilities.invokeAndWait(() -> {
                assertFalse(((JButton) find(view, "challenge.start")).isEnabled());
                ((JButton) find(view, "challenge.start")).doClick();
            });
            assertEquals(100, state.energy, "API loading must precede the charge");
        }
        finally { release.countDown(); }
        awaitButton(view, "challenge.submit");
        snapshot(view, "question");
        click(view, "challenge.submit");
        awaitButton(view, "challenge.submit");
        SwingUtilities.invokeAndWait(() -> assertTrue(((JTextArea) find(view,
                "challenge.error")).getText().contains("Select an answer")));
        for (int index = 0; index < 3; index++) {
            SwingUtilities.invokeAndWait(() -> ((JRadioButton) find(view, "challenge.option.0")).doClick());
            click(view, "challenge.submit");
            awaitButton(view, "challenge.next");
            if (index == 0) { snapshot(view, "feedback"); }
            click(view, "challenge.next");
            awaitButton(view, index == 2 ? "challenge.courses" : "challenge.submit");
        }
        snapshot(view, "result");
        assertEquals(80, state.loadOverview().getEnergy());
        assertEquals(3, state.awardedPoints);
        assertEquals(1, state.starts);
        click(view, "challenge.back");
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
        while (!left.get() && System.nanoTime() < deadline) { Thread.sleep(10); }
        assertTrue(left.get());
    }

    @Test void errorMessageAndRetryButtonRemainVisibleWhenApiFails() throws Exception {
        FakeChallengeState state = new FakeChallengeState();
        state.assign(new ChallengeCourse("bio", "Cell Biology", QuestionTopic.CELL_BIOLOGY, null));
        AtomicReference<ClassChallengeView> holder = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            ClassChallengeView view = ClassChallengeFactory.create(state, (t, p, c) -> {
                throw new use_case.question.QuestionLoadException("Question service is offline");
            }, () -> { });
            holder.set(view);
            view.refresh();
        });
        ClassChallengeView view = holder.get();
        awaitButton(view, "challenge.select"); click(view, "challenge.select");
        awaitButton(view, "challenge.start"); click(view, "challenge.start");
        awaitButton(view, "challenge.start");
        SwingUtilities.invokeAndWait(() -> assertEquals("Question service is offline",
                ((JTextArea) find(view, "challenge.error")).getText()));
        assertEquals(100, state.loadOverview().getEnergy());
        snapshot(view, "api-error");
    }

    private void awaitButton(Container view, String name) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        AtomicBoolean ready = new AtomicBoolean();
        while (System.nanoTime() < deadline) {
            SwingUtilities.invokeAndWait(() -> {
                Component component = find(view, name);
                ready.set(component != null && component.isEnabled());
            });
            if (ready.get()) { return; }
            Thread.sleep(10);
        }
        fail("Timed out waiting for " + name);
    }

    private void click(Container view, String name) throws Exception {
        SwingUtilities.invokeAndWait(() -> ((JButton) find(view, name)).doClick());
    }

    private Component find(Container root, String name) {
        for (Component component : root.getComponents()) {
            if (name.equals(component.getName())) { return component; }
            if (component instanceof Container) {
                Component found = find((Container) component, name);
                if (found != null) { return found; }
            }
        }
        return null;
    }

    private void snapshot(ClassChallengeView view, String name) throws Exception {
        String directory = System.getProperty("uc2.render.dir");
        if (directory == null) { return; }
        Files.createDirectories(Path.of(directory));
        SwingUtilities.invokeAndWait(() -> {
            view.setSize(1000, 760);
            layout(view);
            BufferedImage image = new BufferedImage(1000, 760, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            view.paint(graphics);
            graphics.dispose();
            try { ImageIO.write(image, "png", Path.of(directory, name + ".png").toFile()); }
            catch (java.io.IOException exception) { throw new java.io.UncheckedIOException(exception); }
        });
    }

    private void layout(Container parent) {
        parent.doLayout();
        for (Component component : parent.getComponents()) {
            if (component instanceof Container) { layout((Container) component); }
        }
    }
}
