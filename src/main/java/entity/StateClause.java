package entity;


/** Immutable story configuration. */
public final class StateClause {
    public final String objectId;
    public final String key;
    public final String value;

    public StateClause(String objectId,
                       String key,
                       String value) {
        this.objectId = TemplateText.require(objectId, "objectId");
        this.key = TemplateText.require(key, "key");
        this.value = TemplateText.require(value, "value");
    }
}
