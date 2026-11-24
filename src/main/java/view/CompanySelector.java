package view;

import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Selects the configured boards, one employer, or a public board supplied for this search. */
final class CompanySelector extends JPanel {
  private final JComboBox<String> choices = new JComboBox<>();
  private final List<String> tokens;
  private final JLabel customLabel = new JLabel("Board token or URL");
  private final JTextField custom = new JTextField(28);

  CompanySelector(Map<String, String> companies, Runnable selectionChanged, Runnable search) {
    super(new FlowLayout(FlowLayout.LEADING, 8, 0));
    tokens = List.copyOf(companies.keySet());
    choices.addItem("All configured companies");
    for (String token : tokens) {
      choices.addItem(companies.get(token) + " (" + token + ")");
    }
    choices.addItem("Other Greenhouse board");
    JLabel label = new JLabel("Company");
    label.setLabelFor(choices);
    add(label);
    add(choices);
    customLabel.setLabelFor(custom);
    add(customLabel);
    add(custom);
    customLabel.setVisible(false);
    custom.setVisible(false);
    choices.addActionListener(
        event -> {
          boolean other = choices.getSelectedIndex() == tokens.size() + 1;
          customLabel.setVisible(other);
          custom.setVisible(other);
          if (other) {
            custom.requestFocusInWindow();
          }
          selectionChanged.run();
        });
    custom.addActionListener(event -> search.run());
  }

  String selectedBoard() {
    int index = choices.getSelectedIndex();
    if (index == 0) {
      return null;
    }
    return index <= tokens.size() ? tokens.get(index - 1) : custom.getText();
  }

  void setSearchEnabled(boolean enabled) {
    choices.setEnabled(enabled);
    custom.setEnabled(enabled);
  }
}
