package com.studyarc.view;

import com.studyarc.entity.Milestone;
import com.studyarc.entity.StudyPlan;
import com.studyarc.entity.Task;
import com.studyarc.interface_adapter.track_plan.TrackPlanState;
import com.studyarc.interface_adapter.track_plan.TrackPlanViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TrackPlansView extends JPanel implements PropertyChangeListener {
    final String viewname = "track plan";
    final BorderLayout borderLayout = new BorderLayout();

    final JPanel trackPlansPanel;
    final JPanel TitlePanel;
    final JLabel title = new JLabel("MY PLANS");
    private final TrackPlanViewModel trackPlanViewModel;


    public TrackPlansView(TrackPlanViewModel trackPlanViewModel) {
        this.trackPlanViewModel = trackPlanViewModel;
        this.trackPlanViewModel.addPropertyChangeListener(this);
        this.setLayout(borderLayout);

        TitlePanel = new JPanel();
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        TitlePanel.add(title);
        trackPlansPanel = new JPanel();

        trackPlansPanel.setLayout(new BoxLayout(trackPlansPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(this.trackPlansPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        for (int i = 0; i < 10; i++) {
            trackPlansPanel.add(new JLabel("Plan " + (i + 1)));
        }

        this.add(TitlePanel, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // get the current plans in the TrackPlanState and show them in the view accordingly.
        TrackPlanState currentstate = (TrackPlanState) evt.getNewValue();
        ArrayList<StudyPlan> current_Plans = currentstate.getStudyPlans();
        ArrayList<StudyPlan> Test_Plans = this.generateTestPlans();
//        if (current_Plans.isEmpty()) {
//            System.out.println("YOU HAVE NO PLANS！");
//            //notify user you have no plan
//        } else {
//            this.showPlansinView(current_Plans);
//        }
        this.showPlansinView(Test_Plans);
    }

    private void showPlansinView(ArrayList<StudyPlan> plans) {
        System.out.println("chatgpt is the best!!!");
        trackPlansPanel.removeAll();

        if (plans == null || plans.isEmpty()) {
            trackPlansPanel.add(new JLabel("You have no plans yet."));
        } else {
            int index = 0;
            for (StudyPlan plan : plans) {
                JPanel planPanel = createPlanPanel(plan);
                trackPlansPanel.add(planPanel);
                trackPlansPanel.add(Box.createVerticalStrut(15)); // space between plans
                index++;
            }
        }

        trackPlansPanel.revalidate();
        trackPlansPanel.repaint();

    }

    private JPanel createPlanPanel(StudyPlan plan) {
        JPanel planPanel = new JPanel();
        planPanel.setLayout(new BorderLayout());
        planPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Head Part of each plan
        JPanel headPanel = new JPanel();
        headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.X_AXIS));
        JLabel planLabel = new JLabel("Plan : ");
        JTextField planTitleTextField = new JTextField();
        planTitleTextField.setText(plan.getTitle());
        planLabel.setFont(planLabel.getFont().deriveFont(Font.BOLD, 16f));

        JButton deleteButton = new JButton("❌");
        JButton saveButton = new JButton("Save");

        headPanel.add(planLabel);
        headPanel.add(planTitleTextField);
        headPanel.add(deleteButton);
        headPanel.add(saveButton);
//        headPanel.add(planLabel, BorderLayout.WEST);
//        headPanel.add(deleteButton, BorderLayout.EAST);
//        headPanel.add(planTitleTextField, BorderLayout.NORTH);

        // Milestones of each plan
        JPanel milestonesPanel = new JPanel();
        milestonesPanel.setLayout(new BoxLayout(milestonesPanel, BoxLayout.Y_AXIS));

        ArrayList<Milestone> milestones = plan.getMilestones();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        for (int i = 0; i < milestones.size(); i++) {
            Milestone m = milestones.get(i);

            JPanel milestonePanel = new JPanel();
            milestonePanel.setLayout(new BoxLayout(milestonePanel, BoxLayout.Y_AXIS));

            // MilestoneHeadPanel setup
            JPanel milestoneHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton upButton = new JButton("Hide");
            JButton downButton = new JButton("Show");
            JLabel milestoneLabel = new JLabel("milestone " + (i + 1) + " : " + m.getTitle());

            milestoneHeader.add(upButton);
            milestoneHeader.add(downButton);
            milestoneHeader.add(milestoneLabel);


            // SubTask Panel for each Milestone
            JPanel tasksPanel = new JPanel();
            tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));

            ArrayList<Task> tasks = m.getTasks();
            for (int j = 0; j < tasks.size(); j++) {
                Task t = tasks.get(j);

                JPanel taskRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

                JLabel taskLabel = new JLabel("Task " + (j + 1) + ": " + t.getName() + "    ");

//              JTextField taskNameField = new JTextField(15);
//              taskNameField.setText(t.getName());

                Date d = t.getDuedate();

//              JTextField dueField = new JTextField(10);

//              if (d != null) {
//                  dueField.setText(format.format(d));
//              }
                JLabel dueLabel = new JLabel("Due: " + format.format(d) + "   ");

                JCheckBox doneCheckBox = new JCheckBox("Done");
                doneCheckBox.setSelected(t.isCompleted());

                taskRow.add(taskLabel);
//                taskRow.add(taskNameField);
                taskRow.add(dueLabel);
//                taskRow.add(dueField);
                taskRow.add(doneCheckBox);

                tasksPanel.add(taskRow);
            }

            upButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tasksPanel.setVisible(false);              // hide tasks
                    milestonePanel.revalidate();
                    milestonePanel.repaint();
                }
            });

            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //show the task panel
                    tasksPanel.setVisible(true);
                    milestonePanel.revalidate();
                    milestonePanel.repaint();
                }
            });
            // tasks are originally invisible
            tasksPanel.setVisible(false);

            milestonePanel.add(milestoneHeader);
            milestonePanel.add(tasksPanel);

            milestonesPanel.add(milestonePanel);
            milestonesPanel.add(Box.createVerticalStrut(10));
        }

        planPanel.add(headPanel, BorderLayout.NORTH);
        planPanel.add(milestonesPanel, BorderLayout.CENTER);
        return planPanel;
    }

    public String getViewname() {
        return viewname;
    }

    private ArrayList<StudyPlan> generateTestPlans() {
        ArrayList<StudyPlan> plans = new ArrayList<>();

        // Plan 1
        StudyPlan plan1 = new StudyPlan("Plan 1", new ArrayList<>());

        Milestone p1m1 = new Milestone("Milestone 1");
        p1m1.getTasks().add(new Task("Do step1", new Date()));
        p1m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p1m2 = new Milestone("Milesone 2");
        p1m2.getTasks().add(new Task("Do step1", new Date()));
        p1m2.getTasks().add(new Task("Do step2", new Date()));

        plan1.getMilestones().add(p1m1);
        plan1.getMilestones().add(p1m2);

        // Plan 2
        StudyPlan plan2 = new StudyPlan("Plan 2", new ArrayList<>());

        Milestone p2m1 = new Milestone("Milestone 1");
        p2m1.getTasks().add(new Task("Do step1", new Date()));
        p2m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p2m2 = new Milestone("Milestone 2");
        p2m2.getTasks().add(new Task("Do step1", new Date()));
        p2m2.getTasks().add(new Task("Do step2", new Date()));

        plan2.getMilestones().add(p2m1);
        plan2.getMilestones().add(p2m2);

        // Plan 3
        StudyPlan plan3 = new StudyPlan("Plan 3", new ArrayList<>());

        Milestone p3m1 = new Milestone("Milestone 1");
        p3m1.getTasks().add(new Task("Do step1", new Date()));
        p3m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p3m2 = new Milestone("Milestone 2");
        p3m2.getTasks().add(new Task("Do step1", new Date()));
        p3m2.getTasks().add(new Task("Do step2", new Date()));

        plan3.getMilestones().add(p3m1);
        plan3.getMilestones().add(p3m2);


        //add all 3 plans to plans
        plans.add(plan1);
        plans.add(plan2);
        plans.add(plan3);
        plans.add(plan3);
        plans.add(plan3);
        plans.add(plan3);

        return plans;

    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//
//
//
//            // ===== hook up ViewModel, View, and State =====
//            TrackPlanViewModel viewModel = new TrackPlanViewModel();
//            TrackPlansView trackPlansView = new TrackPlansView(viewModel);
//
//            ArrayList<StudyPlan> plans = trackPlansView.generateTestPlans();
//
//            TrackPlanState state = new TrackPlanState();
//            state.setStudyPlans(plans);
//            viewModel.setState(state);
//            viewModel.firePropertyChange();
//            // this should call firePropertyChanged()
//            // and make TrackPlansView render the plans
//
//            // ===== simple frame to display the TrackPlansView =====
//            JFrame frame = new JFrame("Code Example");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(960, 700);
//            frame.setLayout(new BorderLayout());
//
//            // left sidebar like your screenshot (just to mimic layout)
//            JPanel leftSidebar = new JPanel();
//            leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
//            leftSidebar.add(new JButton("New Plans"));
//            leftSidebar.add(Box.createVerticalStrut(10));
//            leftSidebar.add(new JButton("Papers"));
//            leftSidebar.add(Box.createVerticalStrut(10));
//            leftSidebar.add(new JButton("Jobs"));
//            leftSidebar.add(Box.createVerticalStrut(10));
//            leftSidebar.add(new JButton("My Plans"));
//
//            frame.add(leftSidebar, BorderLayout.WEST);
//            frame.add(trackPlansView, BorderLayout.CENTER);
//
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }

}
