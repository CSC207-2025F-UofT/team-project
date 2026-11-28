package entity;


/** Immutable story configuration. */
public final class CollectableDefinition {
    public final String id;
    public final String name;
    public final String description;
    public final String roomId;
    public final boolean initiallyRevealed;

    public CollectableDefinition(String id,
                                 String name,
                                 String description,
                                 String roomId,
                                 boolean initiallyRevealed) {
        this.id = TemplateText.require(id, "id");
        this.name = TemplateText.require(name, "name");
        this.description = TemplateText.require(description, "description");
        this.roomId = TemplateText.require(roomId, "roomId");
        this.initiallyRevealed = initiallyRevealed;
    }
}
