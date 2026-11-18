package view.dashboard;

import interface_adapter.dashboard.DashboardItemState;
import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.List;

/**
 * Render analytics table given a table state.
 */
public class TableRenderer implements DashboardRenderer<DashboardStateCatalog.TableState> {
    @Override
    public JComponent render(DashboardStateCatalog.TableState tableState) {
        String[] columns = tableState.columns().toArray(new String[0]);
        Object[][] data = tableState.rows().stream()
                .map(List::toArray)
                .toArray(Object[][]::new);

        // Create a model and override isCellEditable to make it read-only
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return new JTable(model);
    }
}
