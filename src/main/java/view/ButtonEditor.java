package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private int row; // <-- 1. ADD THIS FIELD TO STORE THE ROW

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row; // <-- 2. STORE THE ROW WHEN THE EDITOR IS CREATED
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // --- THIS IS WHERE THE BUTTON CLICK LOGIC GOES ---
            // 3. USE THE STORED 'row' FIELD INSTEAD
            JOptionPane.showMessageDialog(button, "Details for row #" + this.row + " (Not Implemented)");
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
