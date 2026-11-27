package view;

import interface_adapter.wardrobe.WardrobeController;
import interface_adapter.wardrobe.WardrobeViewModel;
import use_case.outfit.OutfitOutputData.ClothingData;
import use_case.add_clothing.WardrobeOutputData;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/** Wardrobe page: grid of saved clothing, name search, and the "Add Clothing" button. */
public final class WardrobeView extends JPanel {
    private final JPanel grid = new JPanel(new GridLayout(0, 4, 10, 10));
    private final JPanel gridHolder = new JPanel(new BorderLayout());
    private final JTextField search = new JTextField(18);
    private final JComboBox<String> typeFilter = new JComboBox<>(
            new String[]{"All types", "HAT", "TOP", "BOTTOM", "SHOES"});
    private final JButton add = new JButton("Add Clothing");
    private final JLabel status = new JLabel("Loading...");
    private final WardrobeController controller;
    private final WardrobeViewModel model;
    private boolean busy;

    public WardrobeView(WardrobeController controller, WardrobeViewModel model) {
        this.controller = controller; this.model = model;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JLabel("Wardrobe"));
        toolbar.add(new JLabel("Search:"));
        toolbar.add(search);
        toolbar.add(new JLabel("Type:"));
        toolbar.add(typeFilter);
        toolbar.add(add);
        add(toolbar, BorderLayout.NORTH);

        gridHolder.add(grid, BorderLayout.NORTH);
        add(new JScrollPane(gridHolder), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        add.setEnabled(false);

        add.addActionListener(e -> new AddClothingDialog(
                SwingUtilities.getWindowAncestor(this), controller, model).setVisible(true));
        typeFilter.addActionListener(e -> render());
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { render(); }
            public void removeUpdate(DocumentEvent e) { render(); }
            public void changedUpdate(DocumentEvent e) { render(); }
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
                    add.setEnabled(!busy && model.getState() != null);
                    break;
                default: break;
            }
        });
    }

    private void render() {
        WardrobeOutputData state = model.getState();
        if (state == null) return;
        String query = search.getText().trim().toLowerCase();
        String selectedType = (String) typeFilter.getSelectedItem();
        boolean allTypes = selectedType == null || selectedType.equals("All types");
        List<ClothingData> shown = state.clothing.stream()
                .filter(c -> allTypes || c.category.equals(selectedType))
                .filter(c -> query.isEmpty() || c.name.toLowerCase().contains(query))
                .collect(Collectors.toList());

        grid.removeAll();
        if (state.clothing.isEmpty()) {
            grid.setLayout(new FlowLayout(FlowLayout.LEFT));
            grid.add(new JLabel("Your wardrobe is empty. Click \"Add Clothing\" to add your first item!"));
        } else if (shown.isEmpty()) {
            grid.setLayout(new FlowLayout(FlowLayout.LEFT));
            grid.add(new JLabel("No matching items"));
        } else {
            grid.setLayout(new GridLayout(0, 4, 10, 10));
            for (ClothingData item : shown) {
                ClothingCardPanel card = new ClothingCardPanel(item);
                card.setBorder(BorderFactory.createTitledBorder(item.category));
                grid.add(card);
            }
            // Keep a lone card from stretching across the whole grid.
            while (grid.getComponentCount() < 4) grid.add(new JPanel());
        }
        status.setText(state.message.isBlank()
                ? state.clothing.size() + " item(s) in your wardrobe" : state.message);
        add.setEnabled(!busy);
        grid.revalidate(); grid.repaint();
    }
}
