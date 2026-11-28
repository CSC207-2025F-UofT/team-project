package entity;

import java.util.List;
import java.util.Map;

/** Immutable story configuration. */
public final class StoryTemplate {
    public final String id;
    public final int version;
    public final String title;
    public final String background;
    public final List<RoomDefinition> rooms;
    public final List<CollectableDefinition> items;
    public final String startingRoomId;
    public final Map<String, RandomizationRule> randomizationRules;

    public StoryTemplate(String id,
                         int version,
                         String title,
                         String background,
                         List<RoomDefinition> rooms,
                         List<CollectableDefinition> items,
                         String startingRoomId,
                         Map<String, RandomizationRule> randomizationRules) {
        this.id = TemplateText.require(id, "id");
        this.version = version;
        this.title = TemplateText.require(title, "title");
        this.background = TemplateText.require(background, "background");
        this.rooms = List.copyOf(rooms);
        this.items = List.copyOf(items);
        this.startingRoomId = TemplateText.require(startingRoomId, "startingRoomId");
        this.randomizationRules = Map.copyOf(randomizationRules);
        if (version < 1) throw new IllegalArgumentException("Template version must be positive");
    }
}
