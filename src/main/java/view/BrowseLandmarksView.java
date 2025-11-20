// src/java/view/BrowseLandmarksView.java
package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.browselandmarks.BrowseLandmarksController;
import interface_adapter.browselandmarks.BrowseLandmarksState;
import interface_adapter.browselandmarks.BrowseLandmarksViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BrowseLandmarksView extends JPanel implements PropertyChangeListener {
    private final String viewName = "browse landmarks";
    private final BrowseLandmarksViewModel viewModel;
    private final BrowseLandmarksController controller;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> landmarkList = new JList<>(listModel);
    private final StaticMapPanel mapPanel = new StaticMapPanel();

    public BrowseLandmarksView(BrowseLandmarksViewModel viewModel,
                               BrowseLandmarksController controller,
                               ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.controller = controller;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        landmarkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(landmarkList);
        scrollPane.setPreferredSize(new Dimension(250, 800));

        add(scrollPane, BorderLayout.WEST);
        add(mapPanel, BorderLayout.CENTER);

        // list click: print name
        landmarkList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String name = landmarkList.getSelectedValue();
                if (name != null) {
                    System.out.println("List clicked: " + name);
                }
            }
        });

        // map click: print name
        mapPanel.setMarkerClickListener(name -> {
            System.out.println("Map clicked: " + name);
            // Optionally also select in list:
            landmarkList.setSelectedValue(name, true);
        });

        // trigger initial load
        controller.loadLandmarks();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        BrowseLandmarksState state = (BrowseLandmarksState) evt.getNewValue();

        // update list
        listModel.clear();
        for (BrowseLandmarksState.LandmarkVM vm : state.getLandmarks()) {
            listModel.addElement(vm.name);
        }

        // update map
        mapPanel.setLandmarks(state.getLandmarks());
    }

    public String getViewName() {
        return viewName;
    }

}
