package view;

import interface_adapter.outfit.*;
import use_case.outfit.OutfitOutputData;
import use_case.outfit.OutfitOutputData.*;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public final class OutfitEditorDialog extends JDialog {
    private final JTextField name = new JTextField(22);
    private final Map<String, JCheckBox> clothingChoices = new LinkedHashMap<>();
    private final Map<String, JCheckBox> tagChoices = new LinkedHashMap<>();
    private final JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JButton save = new JButton("Save outfit");
    private final JButton newTag = new JButton("+ New tag");
    private final JButton cancel = new JButton("Cancel");
    private final JLabel status = new JLabel(" ");
    private final OutfitViewModel model;
    private final PropertyChangeListener listener;

    public OutfitEditorDialog(Window owner, OutfitController controller, OutfitViewModel model) {
        super(owner, "Create outfit", ModalityType.APPLICATION_MODAL);
        this.model = model;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(content);
        JPanel title = new JPanel(new BorderLayout(8, 0));
        title.add(new JLabel("Outfit name"), BorderLayout.WEST);
        title.add(name, BorderLayout.CENTER);
        content.add(title, BorderLayout.NORTH);

        JPanel columns = new JPanel(new GridLayout(1, 4, 8, 0));
        OutfitOutputData state = model.getState();
        for (String category : List.of("HAT", "TOP", "BOTTOM", "SHOES")) {
            JPanel list = new JPanel();
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            for (ClothingData item : state.clothing) {
                if (!item.category.equals(category)) continue;
                JCheckBox choice = new JCheckBox("Select");
                clothingChoices.put(item.id, choice);
                JPanel card = new JPanel(new BorderLayout());
                card.add(new ClothingCardPanel(item), BorderLayout.CENTER);
                card.add(choice, BorderLayout.SOUTH);
                list.add(card);
            }
            if (list.getComponentCount() == 0) list.add(new JLabel("No clothing yet"));
            JPanel column = new JPanel(new BorderLayout());
            column.setBorder(BorderFactory.createTitledBorder(category));
            column.add(new JScrollPane(list), BorderLayout.CENTER);
            columns.add(column);
        }
        content.add(columns, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        tags.setBorder(BorderFactory.createTitledBorder("Tags (optional)"));
        JScrollPane tagScroll = new JScrollPane(tags);
        tagScroll.setPreferredSize(new Dimension(700, 85));
        footer.add(tagScroll);
        footer.add(status);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(newTag); buttons.add(cancel); buttons.add(save);
        footer.add(buttons);
        content.add(footer, BorderLayout.SOUTH);
        refreshTags(state.tags);

        newTag.addActionListener(e -> {
            String value = JOptionPane.showInputDialog(this, "New tag name:");
            if (value != null) controller.createTag(value);
        });
        save.addActionListener(e -> controller.createOutfit(name.getText(),
                selected(clothingChoices), selected(tagChoices)));
        cancel.addActionListener(e -> dispose());
        listener = event -> {
            if (event.getPropertyName().equals("busy")) {
                boolean enabled = !((Boolean) event.getNewValue());
                save.setEnabled(enabled); newTag.setEnabled(enabled); cancel.setEnabled(enabled);
                setDefaultCloseOperation(enabled ? DISPOSE_ON_CLOSE : DO_NOTHING_ON_CLOSE);
            } else if (event.getPropertyName().equals("state")) {
                OutfitOutputData next = model.getState();
                if (next.outfitCreated) dispose();
                else { refreshTags(next.tags); status.setText(next.message); }
            }
        };
        model.addListener(listener);
        setSize(830, 580);
        setLocationRelativeTo(owner);
    }
    private List<String> selected(Map<String, JCheckBox> choices) {
        List<String> result = new ArrayList<>();
        choices.forEach((id, box) -> { if (box.isSelected()) result.add(id); });
        return result;
    }
    private void refreshTags(List<TagData> available) {
        List<String> previouslySelected = selected(tagChoices);
        tags.removeAll(); tagChoices.clear();
        for (TagData tag : available) {
            JCheckBox choice = new JCheckBox(tag.name, previouslySelected.contains(tag.id));
            tagChoices.put(tag.id, choice); tags.add(choice);
        }
        if (available.isEmpty()) tags.add(new JLabel("Create a tag to label your outfit."));
        tags.revalidate(); tags.repaint();
    }
    @Override public void dispose() {
        if (listener != null) model.removeListener(listener);
        super.dispose();
    }
}
