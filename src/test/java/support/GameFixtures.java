package support;

import entity.Action;
import entity.Collectable;
import entity.Game;
import entity.Interactable;
import entity.ItemLock;
import entity.Room;
import entity.SetObjectStateEffect;
import entity.Trivia;
import entity.TriviaLock;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** A small authored world for Package 3 tests and the local integration preview. */
public final class GameFixtures {
    private GameFixtures() { }

    public static Game drawerGame() {
        Action open = new Action("open", "Open drawer", Optional.empty(), Set.of("card"),
                "The drawer slides open.", false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Action inspect = new Action("read", "Read the inscription", Optional.empty(), Set.of(),
                "The inscription reads: the answer is 007.", true, List.of(), List.of());
        Interactable drawer = new Interactable("drawer", "Desk drawer",
                Map.of("closed", "A closed drawer is set into the desk.",
                        "open", "The drawer is open. Its card slot is empty."),
                Map.of("mode", "closed"), Map.of("mode", Set.of("closed", "open")),
                Map.of("open", open, "read", inspect));
        Room room = new Room("lab", "Abandoned Laboratory",
                "The exit needs an access card and the answer to a question.",
                new TriviaLock("question", "Exit question", "The answer is accepted.",
                        Optional.of(new Trivia("What three-digit code is on the desk?", List.of(), "007"))),
                new ItemLock("door", "Laboratory door", "The laboratory door opens.", Set.of("card")),
                Optional.empty(), Map.of("drawer", drawer),
                Map.of("card", new Collectable("card", "Laboratory access card", "A brass access card.", false)));
        return new Game("Escape Room Adventure", "Find the clues and escape the laboratory.",
                Map.of("lab", room), "lab");
    }
}
