package com.studyarc.view;

import com.studyarc.entity.StudyPlan;
import com.studyarc.interface_adapter.track_plan.TrackPlanState;
import com.studyarc.interface_adapter.track_plan.TrackPlanViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class TrackPlansView extends JPanel implements PropertyChangeListener{
    final String viewname = "track plan";
    final BorderLayout borderLayout = new BorderLayout();

    final JPanel trackPlansPanel;
    final JPanel TitlePanel;
    final JLabel title = new JLabel("MY PLANS");
    private final TrackPlanViewModel trackPlanViewModel;
    public TrackPlansView(TrackPlanViewModel trackPlanViewModel) {
        this.trackPlanViewModel = trackPlanViewModel;
        this.trackPlanViewModel.addPropertyChangeListener(this);

        trackPlansPanel = new JPanel();
        TitlePanel = new JPanel();
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        TitlePanel.add(title);
        trackPlansPanel.setLayout(new BoxLayout(trackPlansPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < 4; i++) {
            trackPlansPanel.add(new JLabel("Plan " + (i + 1)));
        }

        this.setLayout(borderLayout);
        this.add(TitlePanel, BorderLayout.NORTH);
        this.add(trackPlansPanel, BorderLayout.CENTER);


    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // get the current plans in the TrackPlanState and show them in the view accordingly.
        TrackPlanState currentstate = (TrackPlanState) evt.getNewValue();
        ArrayList<StudyPlan> current_Plans = currentstate.getStudyPlans();
        if (current_Plans.isEmpty()){
            //notify user you have no plan
        }
        else{
            this.showPlansinView(current_Plans);
        }
    }

    private void showPlansinView(ArrayList<StudyPlan> plans){

    }

    public String getViewname() {
        return viewname;
    }
}
