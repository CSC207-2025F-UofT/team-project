package use_case;

import java.util.Objects;

/** A selectable template without its domain configuration. */
public final class StorySummary {
    private final String templateId;
    private final String title;
    private final String background;

    public StorySummary(String templateId, String title, String background) {
        this.templateId = Objects.requireNonNull(templateId, "templateId");
        this.title = Objects.requireNonNull(title, "title");
        this.background = Objects.requireNonNull(background, "background");
    }

    public String templateId() { return templateId; }
    public String title() { return title; }
    public String background() { return background; }
    @Override public String toString() { return title; }
}
