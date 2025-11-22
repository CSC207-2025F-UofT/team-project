package com.studyarc.view;

import com.studyarc.entity.Milestone;
import com.studyarc.entity.StudyPlan;
import com.studyarc.entity.Task;
import com.studyarc.interface_adapter.delete_plan.DeletePlanController;
import com.studyarc.interface_adapter.track_plan.TrackPlanState;
import com.studyarc.interface_adapter.track_plan.TrackPlanViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class TrackPlansView extends JPanel implements PropertyChangeListener, ActionListener, DocumentListener {
    private static TrackPlansView instance;

    final String viewname = "track plan";
    final BorderLayout borderLayout = new BorderLayout();
    final String[] TaskStatus = {"Not Started", "In Progress", "Completed"};

    final JPanel trackPlansPanel;
    final JPanel titlePanel;
    final JLabel title = new JLabel("MY PLANS");
    private final JButton saveButton = new JButton("Save");
    private final JButton newPlan = new JButton("🌟Create A New Plan🌟");
    private final TrackPlanViewModel trackPlanViewModel;
    private DeletePlanController deletePlanController = null;

    private HashMap<JButton, StudyPlan> buttonToPlanMap;
    private HashMap<JTextField, StudyPlan> titleToPlanMap;

    public static TrackPlansView getInstance(TrackPlanViewModel trackPlanViewModel) {
        if (instance == null) {
            instance = new TrackPlansView(trackPlanViewModel);
        }
        return instance;
    }

    private TrackPlansView(TrackPlanViewModel trackPlanViewModel) {
        this.buttonToPlanMap = new HashMap<>();
        this.titleToPlanMap = new HashMap<>();

        this.trackPlanViewModel = trackPlanViewModel;
        this.trackPlanViewModel.addPropertyChangeListener(this);
        this.setLayout(borderLayout);
        titlePanel = SetTitlePanel();
        trackPlansPanel = new JPanel();

        trackPlansPanel.setLayout(new BoxLayout(trackPlansPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(this.trackPlansPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);

    }

    @NotNull
    private JPanel SetTitlePanel() {
        final JPanel titlePanel;
        titlePanel = new JPanel();
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        saveButton.addActionListener(this);
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(saveButton, BorderLayout.EAST);
        return titlePanel;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // get the current plans in the TrackPlanState and show them in the view accordingly.
        TrackPlanState currentstate = (TrackPlanState) evt.getNewValue();
        ArrayList<StudyPlan> current_Plans = currentstate.getStudyPlans();
        if (current_Plans.isEmpty()) {
            this.showRedirectButton();
        } else {
            this.showPlansinView(current_Plans);
        }
    }

    private void showRedirectButton() {
        trackPlansPanel.removeAll();
        JLabel message = new JLabel("You have no Plans! Go Create New Plans!Go Create New Plans!Go Create New Plans!");
        message.setFont(message.getFont().deriveFont(Font.BOLD, 16f));
        newPlan.addActionListener(this);
        trackPlansPanel.add(message);
        trackPlansPanel.add(newPlan);
        trackPlansPanel.repaint();
        trackPlansPanel.revalidate();
    }

    private void showPlansinView(ArrayList<StudyPlan> plans) {
        this.buttonToPlanMap = new HashMap<>();
        this.titleToPlanMap = new HashMap<>();
        this.trackPlansPanel.removeAll();
        for (StudyPlan plan : plans) {
                JPanel planPanel = createPlanPanel(plan);
                trackPlansPanel.add(planPanel);
                trackPlansPanel.add(Box.createVerticalStrut(15));
        }
        trackPlansPanel.repaint();
        trackPlansPanel.revalidate();
    }

    private JPanel createPlanPanel(StudyPlan plan) {
        JPanel planPanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension size = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, size.height);
            }
        };
        planPanel.setLayout(new BorderLayout());
        planPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Head Part of each plan
        JPanel headPanel = new JPanel();
        headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.X_AXIS));
        JLabel planLabel = new JLabel("Plan : ");

        // Text Field for Plan Title
        JTextField planTitleTextField = new JTextField();
        planTitleTextField.getDocument().addDocumentListener(this);
        planTitleTextField.setText(plan.getTitle());
        planLabel.setFont(planLabel.getFont().deriveFont(Font.BOLD, 16f));
        this.titleToPlanMap.put(planTitleTextField, plan);//Add the title textfield in the map for each plan.

        // Delete Button for each plan
        JButton deleteButton = new JButton("Delete " + "❌");
        deleteButton.addActionListener(this);
        this.buttonToPlanMap.put(deleteButton, plan);//Add the delete button in the map for each plan.

        headPanel.add(planLabel);
        headPanel.add(planTitleTextField);
        headPanel.add(deleteButton);


        // Milestones of each plan
        JPanel milestonesPanel = new JPanel();
        milestonesPanel.setLayout(new BoxLayout(milestonesPanel, BoxLayout.Y_AXIS));

        ArrayList<Milestone> milestones = plan.getMilestones();

        for (int i = 0; i < milestones.size(); i++) {
            Milestone m = milestones.get(i);

            JPanel milestonePanel = new JPanel();
            milestonePanel.setLayout(new BoxLayout(milestonePanel, BoxLayout.Y_AXIS));

            // MilestoneHeadPanel setup
            JPanel milestoneHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton upButton = new JButton("▲");
            JButton downButton = new JButton("▼");
            JLabel milestoneLabel = new JLabel("milestone " + (i + 1) + " : " + m.getTitle());

            // Check if all subtasks are completed
            JLabel milestoneCompleted = new JLabel(" ✅ ");
            milestoneCompleted.setVisible(true);
            for (Task subtask : m.getSubtasks()) {
                if (subtask.getStatus().equals("Not Started") | subtask.getStatus().equals("In Progress")) {
                    milestoneCompleted.setVisible(false);
                    break;
                }
            }


            milestoneHeader.add(upButton);
            milestoneHeader.add(downButton);
            milestoneHeader.add(milestoneLabel);
            milestoneHeader.add(milestoneCompleted);

            // SubTask Panel for each Milestone
            JPanel tasksPanel = new JPanel();
            tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
            //If the milestone is completed, hide it. Show it if it's not
            tasksPanel.setVisible(!milestoneCompleted.isVisible());

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

            ArrayList<Task> tasks = m.getSubtasks();
            for (int j = 0; j < tasks.size(); j++) {
                Task t = tasks.get(j);

                JPanel taskRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel taskLabel = new JLabel("Task " + (j + 1) + ": " + t.getName() + "    ");
                String d = t.getDuedate();
                JLabel dueLabel = new JLabel("Due: " + d + "   ");

                //Later on could change String color based on the status.
                JLabel statusLabel = new JLabel("Status: " + t.getStatus());

                taskRow.add(taskLabel);
                taskRow.add(dueLabel);
                taskRow.add(statusLabel);
                tasksPanel.add(taskRow);
            }


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


    public void setDeletePlanController(DeletePlanController deletePlanController) {
        this.deletePlanController = deletePlanController;
    }

    // Delete button triggers here
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (this.buttonToPlanMap.containsKey(button)) {
            StudyPlan plan = this.buttonToPlanMap.get(button);
            this.deletePlanController.execute(plan);
        }else if(e.getSource() == newPlan){
            System.out.println("Go create New Plans!");
        }
        else {
            handleSavingEvent(e);
        }
    }

    private void handleSavingEvent(ActionEvent e) {
        ArrayList<StudyPlan> currentPlansInView = trackPlanViewModel.getState().getStudyPlans();

        Set<String> planTitles = new HashSet<>();
        for (StudyPlan plan : currentPlansInView) {
            if (plan.getTitle().strip().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Plan Title! Not allowed!😡😡");
                System.out.println("Empty Plan Title! Not allowed!");
                return;
            }
            planTitles.add(plan.getTitle());
        }
        if (planTitles.size() == currentPlansInView.size()) {
            System.out.println("All Plans have different names, good to save!");
            JOptionPane.showMessageDialog(this, "Save Successful!");
        } else {
            JOptionPane.showMessageDialog(this, "StudyPlans CAN NOT have the same title😡😡");
            System.out.println("There are some repetitive names in Plans, fail to save");
        }
    }


    // DocumentListener for the plan title input field
    @Override
    public void insertUpdate(DocumentEvent e) {
        handleTypingEvent(e);

    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handleTypingEvent(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handleTypingEvent(e);
    }

    private void handleTypingEvent(DocumentEvent e) {
        Document doc = e.getDocument();
        Set<JTextField> textFieldSet = titleToPlanMap.keySet();
        for (JTextField textField : textFieldSet) {
            if (textField.getDocument() == doc) {
                titleToPlanMap.get(textField).setTitle(textField.getText());
                System.out.println("Find the field of the Plan: " + titleToPlanMap.get(textField).getTitle());
                System.out.println(titleToPlanMap.get(textField).getTitle());
                break;
            }
        }
    }
}
