package interface_adapter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import use_case.StartGameInputBoundary;
import use_case.TriviaInputBoundary;

class GameControllerTest {
    @Test void allCommandsAreDeferredAndKeepTheirExactArguments() {
        List<String> calls = new ArrayList<>();
        List<Runnable> scheduled = new ArrayList<>();
        GameController controller = new GameController(new StartGameInputBoundary() {
            public void listStories() { calls.add("list"); }
            public void start(String id) { calls.add("start:" + id); }
        }, (object, action, item) -> calls.add("interact:" + object + ":" + action + ":" + item.orElse("none")),
                id -> calls.add("use:" + id), new TriviaInputBoundary() {
                    public void open(String id) { calls.add("open:" + id); }
                    public void submit(String id, String answer) { calls.add("answer:" + id + ":" + answer); }
                }, () -> calls.add("move"), () -> calls.add("save"), () -> calls.add("load"), scheduled::add);

        controller.listStories();
        controller.start("story-b");
        controller.interact("drawer-b", "open", Optional.of("key-b"));
        controller.useItems("door-b");
        controller.openTrivia("question-b");
        controller.submitAnswer("question-b", " 001a ");
        controller.move();
        controller.save();
        controller.load();

        assertTrue(calls.isEmpty(), "Controller must not bypass the command dispatcher");
        assertEquals(9, scheduled.size());
        scheduled.forEach(Runnable::run);
        assertEquals(List.of("list", "start:story-b", "interact:drawer-b:open:key-b", "use:door-b",
                "open:question-b", "answer:question-b: 001a ", "move", "save", "load"), calls);
    }

    @Test void controllerRejectsMissingDependencies() {
        assertThrows(NullPointerException.class, () ->
                new GameController(null, null, null, null, null, null, null, Runnable::run));
    }
}
