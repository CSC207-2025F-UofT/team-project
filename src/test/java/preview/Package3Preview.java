package preview;

import entity.DomainResult;
import interface_adapter.CommandDispatcher;
import interface_adapter.GameController;
import interface_adapter.GameViewModel;
import interface_adapter.GameViewState;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import support.GameFixtures;
import use_case.GameContext;
import use_case.GameOutputAssembler;
import use_case.GameOutputData;
import use_case.InteractInteractor;
import use_case.StartGameInputBoundary;
import use_case.StorySummary;
import use_case.TriviaInputBoundary;
import view.GameView;

/**
 * Local integration harness, deliberately in test sources. Real Package 3 code runs
 * against in-memory story/gate adapters; file save/load remain the Package 5 handoff.
 */
public final class Package3Preview {
    private final GameContext context = new GameContext(GameFixtures.drawerGame());
    private final GameOutputAssembler assembler = new GameOutputAssembler();
    private final GameViewModel model = new GameViewModel();
    private final ExecutorService worker = Executors.newSingleThreadExecutor(command -> {
        Thread thread = new Thread(command, "package3-preview");
        thread.setDaemon(true);
        return thread;
    });
    private final AtomicBoolean busy = new AtomicBoolean();

    private GameView createView() {
        StartGameInputBoundary start = new StartGameInputBoundary() {
            @Override public void listStories() {
                publish(assembler.fromStories(List.of(new StorySummary("lab", "Abandoned Laboratory",
                        "Find the clues and escape the laboratory.")), "Select the preview story."));
            }
            @Override public void start(String templateId) {
                if (!"lab".equals(templateId)) {
                    publishCurrent("Unknown preview story.", Optional.empty());
                    return;
                }
                context.replace(GameFixtures.drawerGame());
                publishCurrent("The laboratory story begins.", Optional.empty());
            }
        };
        TriviaInputBoundary trivia = new TriviaInputBoundary() {
            @Override public void open(String lockId) {
                publishCurrent("Answer the exit question.", Optional.of(lockId));
            }
            @Override public void submit(String lockId, String answer) {
                runDomain(() -> context.requireCurrent().submitAnswer(lockId, answer), Optional.of(lockId));
            }
        };
        CommandDispatcher commands = command -> {
            if (!busy.compareAndSet(false, true)) return;
            model.setBusy(true);
            worker.execute(() -> {
                try {
                    command.run();
                } catch (RuntimeException failure) {
                    publishCurrent("Preview command failed: " + failure.getMessage(), Optional.empty());
                } finally {
                    SwingUtilities.invokeLater(() -> {
                        model.setBusy(false);
                        busy.set(false);
                    });
                }
            });
        };
        GameController controller = new GameController(start,
                new InteractInteractor(context, assembler, this::publish),
                lockId -> runDomain(() -> context.requireCurrent().useItems(lockId), Optional.empty()),
                trivia, () -> runDomain(() -> context.requireCurrent().move(), Optional.empty()),
                () -> publishCurrent("Preview: file saving will be connected by Package 5.", Optional.empty()),
                () -> publishCurrent("Preview: file loading will be connected by Package 5.", Optional.empty()),
                commands);
        model.setState(new GameViewState(assembler.fromGame(context.requireCurrent(),
                "Package 3 preview — open the drawer, read the clue, and escape.", Optional.empty()), false));
        return new GameView(controller, model);
    }

    private void runDomain(Supplier<DomainResult> command, Optional<String> activeLock) {
        DomainResult result = command.get();
        publishCurrent(result.resultText(), activeLock);
    }

    private void publishCurrent(String message, Optional<String> activeLock) {
        publish(assembler.fromGame(context.requireCurrent(), message, activeLock));
    }

    private void publish(GameOutputData data) {
        SwingUtilities.invokeLater(() -> model.setState(new GameViewState(data, model.getState().busy())));
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2 && "--screenshot".equals(args[0])) {
            Path destination = Path.of(args[1]);
            SwingUtilities.invokeAndWait(() -> {
                Package3Preview preview = new Package3Preview();
                GameView view = preview.createView();
                DomainResult opened = preview.context.requireCurrent().interact("drawer", "open", Optional.empty());
                preview.model.setState(new GameViewState(preview.assembler.fromGame(
                        preview.context.requireCurrent(), opened.resultText(), Optional.of("question")), false));
                view.setSize(1100, 820);
                layoutTree(view);
                BufferedImage image = new BufferedImage(1100, 820, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                view.printAll(graphics);
                graphics.dispose();
                try {
                    ImageIO.write(image, "png", destination.toFile());
                } catch (java.io.IOException failure) {
                    throw new java.io.UncheckedIOException(failure);
                }
                preview.worker.shutdown();
            });
            return;
        }
        SwingUtilities.invokeLater(() -> {
            Package3Preview preview = new Package3Preview();
            JFrame frame = new JFrame("Escape Room Adventure — Package 3 Preview");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override public void windowClosed(java.awt.event.WindowEvent event) { preview.worker.shutdown(); }
            });
            frame.add(preview.createView(), BorderLayout.CENTER);
            frame.setSize(1100, 820);
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }

    private static void layoutTree(java.awt.Container container) {
        container.doLayout();
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof java.awt.Container) layoutTree((java.awt.Container) child);
        }
    }
}
