package use_case;

import entity.ActionOption;
import entity.Collectable;
import entity.Game;
import entity.GameStatus;
import entity.Interactable;
import entity.ItemLock;
import entity.Lock;
import entity.Room;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** Copies read-only domain queries into immutable UI data, without saved answers. */
public final class GameOutputAssembler {
    public GameOutputData fromGame(Game game, String message, Optional<String> activeLockId) {
        Objects.requireNonNull(game, "game");
        Objects.requireNonNull(activeLockId, "activeLockId");
        Room room = game.currentRoom();
        Set<String> heldIds = game.inventoryItems().stream().map(Collectable::id)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Map<String, String> descriptions = new LinkedHashMap<>();
        Map<String, String> objectNames = new LinkedHashMap<>();
        for (Interactable object : room.interactables()) {
            descriptions.put(object.id(), object.name() + "\n" + object.describe());
            objectNames.put(object.id(), object.name());
        }
        List<ActionData> actions = new ArrayList<>();
        if (game.status() == GameStatus.ACTIVE) {
            for (ActionOption option : room.availableActions(heldIds)) {
                actions.add(new ActionData(option.objectId(), option.actionId(),
                        objectNames.get(option.objectId()) + " — " + option.label(), option.requiresItem()));
            }
        }
        List<LockData> locks = new ArrayList<>();
        for (Lock lock : room.availableLocks()) {
            List<ItemData> required = List.of();
            Set<String> heldRequired = new LinkedHashSet<>();
            boolean canUnlock = false;
            if (lock instanceof ItemLock) {
                Set<String> requiredIds = ((ItemLock) lock).requiredItemIds();
                required = itemData(game.itemDetails(requiredIds));
                heldRequired.addAll(requiredIds);
                heldRequired.retainAll(heldIds);
                canUnlock = game.status() == GameStatus.ACTIVE && !lock.isUnlocked()
                        && heldIds.containsAll(requiredIds);
            }
            locks.add(new LockData(lock.id(), room.id(), lock.description(), lock.kind(),
                    lock.isUnlocked(), required, heldRequired, canUnlock));
        }
        Optional<TriviaData> activeTrivia = activeLockId
                .filter(id -> room.exitChallenge().id().equals(id))
                .flatMap(id -> room.exitChallenge().getTrivia()
                        .map(trivia -> new TriviaData(id, trivia.prompt(), trivia.choices())));
        return new GameOutputData(Optional.of(room.id()), game.name(),
                game.background() + "\n\n" + room.name() + "\n" + room.describe(),
                descriptions, itemData(game.inventoryItems()), actions, locks, activeTrivia,
                List.of(), message, game.canMove(), room.isFinal(),
                game.status() == GameStatus.ESCAPED);
    }

    public GameOutputData fromStories(List<StorySummary> stories, String message) {
        return new GameOutputData(Optional.empty(), "Escape Room Adventure", "Choose a story.",
                Map.of(), List.of(), List.of(), List.of(), Optional.empty(), stories,
                message, false, false, false);
    }

    public GameOutputData fromNoGame(String message) {
        return fromStories(List.of(), message);
    }

    private List<ItemData> itemData(List<Collectable> items) {
        return items.stream().map(item -> new ItemData(item.id(), item.name()))
                .collect(Collectors.toList());
    }
}
