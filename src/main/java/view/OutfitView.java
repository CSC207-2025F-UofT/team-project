package view;

import interface_adapter.outfit.OutfitController;
import interface_adapter.outfit.OutfitViewModel;
import use_case.outfit.OutfitOutputData;
import use_case.outfit.OutfitOutputData.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class OutfitView extends JPanel {
    private final JPanel rows = new JPanel();
    private final JTextField search = new JTextField(18);
    private final JButton filter = new JButton("Filter: All outfits");
    private final JButton create = new JButton("+ Create outfit");
    private final JButton newTag = new JButton("+ New tag");
    private final JButton refresh = new JButton("Refresh");
    private final JLabel status = new JLabel("Loading...");
    private final java.util.ArrayList<JButton> rowButtons = new java.util.ArrayList<>();
    private final OutfitController controller;
    private final OutfitViewModel model;
    private boolean busy;

    public OutfitView(OutfitController controller, OutfitViewModel model) {
        this.controller = controller; this.model = model;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel toolbar = new JPanel(new GridLayout(0, 1));
        JPanel searchTools = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchTools.add(new JLabel("Outfits"));
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setLabelFor(search);
        searchTools.add(searchLabel);
        search.getAccessibleContext().setAccessibleName("Search outfits by name");
        search.setToolTipText("Search outfits by name");
        searchTools.add(search);
        searchTools.add(filter);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(newTag); actions.add(refresh); actions.add(create);
        toolbar.add(searchTools);
        toolbar.add(actions);
        add(toolbar, BorderLayout.NORTH);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        add(new JScrollPane(rows), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        create.setEnabled(false); filter.setEnabled(false);

        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent event) { render(); }
            public void removeUpdate(DocumentEvent event) { render(); }
            public void changedUpdate(DocumentEvent event) { render(); }
        });

        create.addActionListener(e -> new OutfitEditorDialog(
                SwingUtilities.getWindowAncestor(this), controller, model).setVisible(true));
        filter.addActionListener(e -> showFilters());
        refresh.addActionListener(e -> controller.load(
                model.getState() == null ? null : model.getState().selectedTagId));
        newTag.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "New tag name:");
            if (name != null) controller.createTag(name);
        });
        model.addListener(event -> {
            switch (event.getPropertyName()) {
                case "state": render(); break;
                case "error":
                    Window owner = SwingUtilities.getWindowAncestor(this);
                    Component parent = owner;
                    if (owner != null) {
                        for (Window child : owner.getOwnedWindows()) {
                            if (child.isVisible()) parent = child;
                        }
                    }
                    JOptionPane.showMessageDialog(parent, event.getNewValue(),
                            "Unable to complete request", JOptionPane.ERROR_MESSAGE);
                    break;
                case "busy":
                    busy = (Boolean) event.getNewValue();
                    updateButtons();
                    break;
                default: break;
            }
        });
    }
    private void updateButtons() {
        create.setEnabled(!busy && model.getState() != null);
        filter.setEnabled(!busy && model.getState() != null);
        newTag.setEnabled(!busy); refresh.setEnabled(!busy);
        rowButtons.forEach(button -> button.setEnabled(!busy));
    }
    private void showFilters() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem all = new JMenuItem("All outfits");
        all.addActionListener(e -> controller.load(null)); menu.add(all);
        for (TagData tag : model.getState().tags) {
            JMenuItem item = new JMenuItem(tag.name);
            item.addActionListener(e -> controller.load(tag.id)); menu.add(item);
        }
        menu.show(filter, 0, filter.getHeight());
    }
    private void render() {
        OutfitOutputData state = model.getState();
        if (state == null) return;
        String query = search.getText().trim().toLowerCase(Locale.ROOT);
        // State already contains the selected tag's outfits; name search narrows that result.
        List<OutfitData> shown = state.outfits.stream()
                .filter(outfit -> outfit.name.toLowerCase(Locale.ROOT).contains(query))
                .collect(Collectors.toList());
        rows.removeAll();
        rowButtons.clear();
        String filterName = state.tags.stream().filter(t -> t.id.equals(state.selectedTagId))
                .map(t -> t.name).findFirst().orElse("All outfits");
        filter.setText("Filter: " + filterName);
        if (shown.isEmpty()) {
            rows.add(new JLabel(!query.isEmpty()
                    ? "No outfits match your search and current tag filter."
                    : state.selectedTagId == null
                    ? "No outfits yet. Create your first outfit!"
                    : "No outfits found for this tag."));
        }
        for (OutfitData outfit : shown) {
            JPanel row = new JPanel(new BorderLayout(8, 8));
            row.setBorder(BorderFactory.createTitledBorder(outfit.name));
            JPanel clothes = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (String id : outfit.clothingIds) {
                state.clothing.stream().filter(c -> c.id.equals(id)).findFirst()
                        .ifPresent(c -> clothes.add(new ClothingCardPanel(c)));
            }
            row.add(clothes, BorderLayout.CENTER);
            List<String> names = state.tags.stream().filter(t -> outfit.tagIds.contains(t.id))
                    .map(t -> t.name).collect(Collectors.toList());
            row.add(new JLabel("Tags: " + String.join(", ", names)), BorderLayout.SOUTH);
            JButton delete = new JButton("Delete");
            JButton edit = new JButton("Edit");
            edit.addActionListener(e -> new OutfitEditorDialog(
                    SwingUtilities.getWindowAncestor(this), controller, model, outfit).setVisible(true));
            delete.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Delete this outfit?", "Confirm deletion",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    controller.deleteOutfit(outfit.id);
                }
            });
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actions.add(edit);
            actions.add(delete);
            rowButtons.add(edit);
            rowButtons.add(delete);
            row.add(actions, BorderLayout.EAST);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
            rows.add(row);
        }
        status.setText(state.message.isBlank() ? "Demo storage: data is not persisted." : state.message);
        updateButtons(); rows.revalidate(); rows.repaint();
    }
}
