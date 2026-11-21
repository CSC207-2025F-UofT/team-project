package view;

import entity.Job;
import interface_adapter.job_grouping.CreateJobGroupController;
import interface_adapter.job_grouping.CreateJobGroupState;
import interface_adapter.job_grouping.CreateJobGroupViewModel;
import interface_adapter.job_grouping.CreateJobGroupViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The View for creating a job group.
 */
public class CreateJobGroupView extends JPanel
        implements CreateJobGroupViewModelListener {

    private final CreateJobGroupViewModel viewModel;

    private final JTextField groupNameInputField = new JTextField(15);
    private final JLabel messageField = new JLabel();

    private final JList<String> jobList;
    private final ArrayList<Job> availableJobs;

    private final JButton createGroup;
    private CreateJobGroupController controller;

    /**
     * Creates the Create Job Group View.
     * @param viewModel the view model
     * @param availableJobs the jobs available for selection
     */
    public CreateJobGroupView(CreateJobGroupViewModel viewModel,
                              ArrayList<Job> availableJobs) {
        this.viewModel = viewModel;
        this.availableJobs = new ArrayList<>(availableJobs);
        this.viewModel.addListener(this);

        final JLabel title = new JLabel("Create Job Group");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel groupNameInfo = new LabelTextPanel(
                new JLabel("Group Name"), groupNameInputField);

        DefaultListModel<String> jobListModel = new DefaultListModel<>();

        for (Job job : availableJobs) {
            jobListModel.addElement(job.getTitle() + " - " + job.getCompany());
        }

        jobList = new JList<>(jobListModel);
        jobList.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        final JScrollPane jobScrollPane = new JScrollPane(jobList);

        createGroup = new JButton("Create Group");

        createGroup.addActionListener(evt -> {
            ArrayList<Job> selectedJobs = new ArrayList<>();

            for (int index : jobList.getSelectedIndices()) {
                selectedJobs.add(this.availableJobs.get(index));
            }

            final CreateJobGroupState currentState = viewModel.getState();

            if (controller != null) {
                controller.execute(
                        currentState.getUsername(),
                        groupNameInputField.getText(),
                        selectedJobs);
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(groupNameInfo);
        this.add(new JLabel("Select Jobs"));
        this.add(jobScrollPane);
        this.add(messageField);
        this.add(createGroup);
    }

    @Override
    public void stateChanged() {
        final CreateJobGroupState state = viewModel.getState();

        if (state.isSuccess()) {
            messageField.setText(
                    "Successfully created group: " + state.getGroupName());
        }
        else {
            messageField.setText(state.getErrorMessage());
        }
    }

    public void setController(CreateJobGroupController controller) {
        this.controller = controller;
    }
}
