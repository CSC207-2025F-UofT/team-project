package view;

import interface_adapter.save_event_to_list.SaveEventToListController;
import interface_adapter.save_event_to_list.SaveEventToListViewModel;
import use_case.save_event_to_list.SaveEventToListInputBoundary;

import javax.swing.*;
import java.awt.*;

// stuck, current issue:
// in createUI, for loop of creating checkBox has problem, since I have not coded viewModel to get name of list

public class SaveEventToListView extends JPanel{
    private final SaveEventToListController controller;
    private final SaveEventToListViewModel viewModel;

    // UI generate (based on number of eventList)
    public void createUI(int n){
        // n is number of existing eventList
        JFrame frame = new JFrame("Save Event to List View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        // basic parameter of frame layout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;

        JLabel viewQuestion = new JLabel("Which list(s) you want to save your event in?");

        frame.add(viewQuestion, gbc);

        if(n == 0){
            // to be added, situation that
            return;
        }else{
            JCheckBox[] eventListCheckBoxes = new JCheckBox[n];
            for(int i = 0; i < n; i++){
                gbc.gridx = 0; gbc.gridy = 1 + i;
                frame.add(eventListCheckBoxes[i], gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = 1 + n;
        gbc.anchor = GridBagConstraints.EAST;

        JButton confirmButton = new JButton("Confirm Saving");

        frame.add(confirmButton, gbc);

        frame.setVisible(true);

    }


    public SaveEventToListView(SaveEventToListController controller, SaveEventToListViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
    }


    public static void main(String[] args) {
        // method for debug, not for start, should be deleted after design

    }
}
