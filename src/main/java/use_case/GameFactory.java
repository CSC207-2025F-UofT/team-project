package use_case;

import entity.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.*;
import java.util.stream.Collectors;

/** Builds new runtime components from immutable definitions and one session's generated values. */
public final class GameFactory {
    private final Random random;
    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^{}]+)}");

    public GameFactory(Random random) { this.random = Objects.requireNonNull(random); }

    public Game create(StoryTemplate template) {
        TemplateValidator.validate(template);
        Map<String, String> values = new LinkedHashMap<>();
        template.randomizationRules.forEach((key, rule) -> values.put(key, rule.generate(random)));
        Map<String, Room> rooms = buildRooms(template, values);
        return new Game(resolve(template.title, values), resolve(template.background, values), rooms,
                template.startingRoomId, template.id, template.version, values);
    }

    static String resolve(String source, Map<String, String> values) {
        String result = PLACEHOLDER.matcher(source).replaceAll(match -> {
            String value = values.get(match.group(1));
            if (value == null) throw new IllegalArgumentException("Unknown placeholder " + match.group());
            return Matcher.quoteReplacement(value);
        });
        if (result.contains("${")) throw new IllegalArgumentException("Malformed placeholder in " + source);
        return result;
    }

    // Saved-state construction can reuse this step with retained values, without entering a room.
    static Map<String, Room> buildRooms(StoryTemplate template, Map<String, String> values) {
        Function<String, String> text = source -> resolve(source, values);
        Map<String, Room> rooms = new LinkedHashMap<>();
        for (RoomDefinition room : template.rooms) {
            Map<String, Collectable> items = new LinkedHashMap<>();
            for (CollectableDefinition item : template.items) {
                if (room.id.equals(item.roomId)) items.put(item.id, new Collectable(item.id,
                        text.apply(item.name), text.apply(item.description), item.initiallyRevealed));
            }
            Map<String, Interactable> objects = new LinkedHashMap<>();
            for (InteractableDefinition obj : room.interactables) {
                Map<String, Action> actions = new LinkedHashMap<>();
                for (ActionDefinition a : obj.actions) {
                    List<Condition> conditions = a.preconditions.stream()
                            .map(c -> new ObjectStateCondition(c.objectId, c.key, c.value)).collect(Collectors.toList());
                    List<Effect> effects = a.effects.stream()
                            .map(c -> new SetObjectStateEffect(c.objectId, c.key, c.value)).collect(Collectors.toList());
                    actions.put(a.id, new Action(a.id, text.apply(a.label), a.requiredItemId, a.appearingItemIds,
                            text.apply(a.resultText), a.repeatable, conditions, effects));
                }
                Map<String, String> modes = new LinkedHashMap<>();
                obj.descriptionsByMode.forEach((key, desc) -> modes.put(key, text.apply(desc)));
                objects.put(obj.id, new Interactable(obj.id, text.apply(obj.name), modes, obj.initialState,
                        obj.allowedStateValues, actions));
            }
            ItemLockDefinition door = room.doorLock;
            TriviaLockDefinition lock = room.exitChallenge;
            Optional<Trivia> trivia = lock.initialTrivia.map(q -> new Trivia(text.apply(q.prompt),
                    q.choices.stream().map(text).collect(Collectors.toList()), text.apply(q.expectedAnswer)));
            rooms.put(room.id, new Room(room.id, text.apply(room.name), text.apply(room.description),
                    new TriviaLock(lock.id, text.apply(lock.description), text.apply(lock.successText), trivia, lock.spec),
                    new ItemLock(door.id, text.apply(door.description), text.apply(door.successText), door.requiredItemIds),
                    room.nextRoomId, objects, items));
        }
        return rooms;
    }
}
