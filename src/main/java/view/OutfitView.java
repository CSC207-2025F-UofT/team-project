package view;

import interface_adapter.outfit.*;
import use_case.outfit.OutfitOutputData;
import use_case.outfit.OutfitOutputData.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public final class OutfitView extends JPanel {
    private final JPanel rows = new JPanel();
    private final JButton filter = new JButton("Filter: All outfits");
    private final JButton create = new JButton("+ Create outfit");
    private final JButton newTag = new JButton("+ New tag");
    private final JButton refresh = new JButton("Refresh");
    private final JLabel status = new JLabel("Loading...");
    private final OutfitController controller;
    private final OutfitViewModel model;
    private boolean busy;

    public OutfitView(OutfitController controller, OutfitViewModel model) {
        this.controller = controller; this.model = model;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JLabel("Outfits")); toolbar.add(filter);
        toolbar.add(newTag); toolbar.add(refresh); toolbar.add(create);
        add(toolbar, BorderLayout.NORTH);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        add(new JScrollPane(rows), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        create.setEnabled(false); filter.setEnabled(false);

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
        for (Component row : rows.getComponents()) {
            if (row instanceof JPanel) {
                for (Component c : ((JPanel) row).getComponents()) {
                    if (c instanceof JButton) c.setEnabled(!busy);
                }
            }
        }
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
        rows.removeAll();
        String filterName = state.tags.stream().filter(t -> t.id.equals(state.selectedTagId))
                .map(t -> t.name).findFirst().orElse("All outfits");
        filter.setText("Filter: " + filterName);
        if (state.outfits.isEmpty()) {
            rows.add(new JLabel(state.selectedTagId == null
                    ? "No outfits yet. Create your first outfit!"
                    : "No outfits found for this tag."));
        }
        for (OutfitData outfit : state.outfits) {
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
            delete.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Delete this outfit?", "Confirm deletion",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    controller.deleteOutfit(outfit.id);
                }
            });
            row.add(delete, BorderLayout.EAST);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));
            rows.add(row);
        }
        status.setText(state.message.isBlank() ? "Demo storage: data is not persisted." : state.message);
        updateButtons(); rows.revalidate(); rows.repaint();
    }
}
