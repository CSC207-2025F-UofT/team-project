package pdf_editor.adapter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Transient UI selection IDs; never persisted into the PDF document or Undo snapshots. */
public final class PdfSelectionModel {
    private final LinkedHashSet<String> ids = new LinkedHashSet<>();
    private String primary;

    public Set<String> ids() { return Set.copyOf(ids); }
    public String primary() { return primary; }
    public void clear() { ids.clear(); primary = null; }
    public void select(String id) {
        clear();
        if (id != null) { ids.add(id); primary = id; }
    }
    public void toggle(String id) {
        Objects.requireNonNull(id);
        if (!ids.remove(id)) { ids.add(id); primary = id; }
        else if (id.equals(primary)) primary = ids.isEmpty() ? null : ids.iterator().next();
    }
    public void replace(Collection<String> selected) {
        clear();
        add(selected);
    }
    public void add(Collection<String> selected) {
        for (String id : selected) {
            Objects.requireNonNull(id);
            if (ids.add(id) && primary == null) primary = id;
        }
    }
    public void retain(Collection<String> existing) {
        ids.retainAll(existing);
        if (!ids.contains(primary)) primary = ids.isEmpty() ? null : ids.iterator().next();
    }
}
