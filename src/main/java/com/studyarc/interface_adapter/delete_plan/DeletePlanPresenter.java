package com.studyarc.interface_adapter.delete_plan;

import com.studyarc.entity.StudyPlan;
import com.studyarc.interface_adapter.ViewManagerModel;
import com.studyarc.interface_adapter.track_plan.TrackPlanState;
import com.studyarc.interface_adapter.track_plan.TrackPlanViewModel;
import com.studyarc.use_case.delete_plan.DeletePlanOutputBoundary;
import com.studyarc.use_case.delete_plan.DeletePlanOutputData;
import com.studyarc.use_case.track_plan.TrackPlanOutputData;

import java.util.ArrayList;

public class DeletePlanPresenter implements DeletePlanOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final TrackPlanViewModel trackPlanViewModel;

    public DeletePlanPresenter(TrackPlanViewModel trackPlanViewModel,
                               ViewManagerModel viewManagerModel) {
        this.trackPlanViewModel = trackPlanViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void ShowPlans(DeletePlanOutputData outputData) {

        StudyPlan deletedPlan = outputData.getPlan();
        ArrayList<StudyPlan> plans = trackPlanViewModel.getState().getStudyPlans();

        System.out.println("Before delete: " + plans.size());
        System.out.println("Contains delted Plan?" + plans.contains(deletedPlan));

        plans.remove(deletedPlan);

        System.out.println("After delete: " + plans.size());

        trackPlanViewModel.firePropertyChange();


    }
}
