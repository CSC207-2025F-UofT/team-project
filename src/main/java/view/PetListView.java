package view;

import entity.Pet;
import interface_adapter.controllers4ViewFilterPets.ViewPetsController;
import interface_adapter.ViewModel4ViewFilterPets.PetListViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PetListView extends JFrame {
    private final PetListViewModel viewModel;
    private final ViewPetsController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> petJList = new JList<>(listModel);

    public PetListView(PetListViewModel viewModel, ViewPetsController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        setTitle("Availiable Pets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        add(new JScrollPane(petJList), BorderLayout.CENTER);

        controller.onViewPets();
        refreshList();
        setVisible(true);
    }
    private void refreshList() {
        listModel.clear();
        List<Pet> pets = viewModel.getPets();
        for (Pet pet : pets) {
            String line = pet.getName() + " (" + pet.getAge() + " " +
                    pet.getSpecies() + ", " + pet.getSize() + ")";
            listModel.addElement(line);
        }
    }
}
