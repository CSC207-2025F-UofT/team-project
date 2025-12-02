package view.pets;

import entity.Pet;
import interface_adapter.ViewPets.ViewPetsController;
import view.common.RightContentTemplate;
import interface_adapter.FilterPets.PetListViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class PetListView2 extends RightContentTemplate implements PropertyChangeListener { 
	
    private final PetListViewModel viewModel;
    private final ViewPetsController controller;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> petJList = new JList<>(listModel);
    

    public PetListView2(PetListViewModel viewModel, ViewPetsController controller) {
        super("Available Pets");

        this.viewModel = viewModel;
        this.controller = controller;

//        setTitle("Availiable Pets");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(500, 500);
//        setLocationRelativeTo(null);
        

        JList<String> list = new JList<>(listModel);
        list.setFont(new Font("Arial", Font.PLAIN, 16));
        list.setFixedCellHeight(32);

        bodyPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        refreshList();
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
    
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
        refreshList();	
	}

}
