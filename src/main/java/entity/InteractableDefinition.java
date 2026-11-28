package entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Immutable story configuration. */
public final class InteractableDefinition {
    public final String id;
    public final String name;
    public final Map<String, String> descriptionsByMode;
    public final Map<String, String> initialState;
    public final Map<String, Set<String>> allowedStateValues;
    public final List<ActionDefinition> actions;

    public InteractableDefinition(String id,
                                  String name,
                                  Map<String, String> descriptionsByMode,
                                  Map<String, String> initialState,
                                  Map<String, Set<String>> allowedStateValues,
                                  List<ActionDefinition> actions) {
        this.id = TemplateText.require(id, "id");
        this.name = TemplateText.require(name, "name");
        this.descriptionsByMode = Map.copyOf(descriptionsByMode);
        this.initialState = Map.copyOf(initialState);
        Map<String, Set<String>> allowed = new LinkedHashMap<>();
        allowedStateValues.forEach((key, values) -> allowed.put(key, Set.copyOf(values)));
        this.allowedStateValues = Map.copyOf(allowed);
        this.actions = List.copyOf(actions);
    }
}
