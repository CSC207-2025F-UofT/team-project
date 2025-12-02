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

public class PetListView extends RightContentTemplate implements PropertyChangeListener { 
	
    private final PetListViewModel viewModel;
    private final JPanel listPanel = new JPanel();
    
    public PetListView(PetListViewModel viewModel, ViewPetsController controller) {
        super("Available Pets");

        this.viewModel = viewModel;
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(listPanel, BorderLayout.CENTER);

        refreshList();
    }

    public void refreshList() {

        listPanel.removeAll();

        List<Pet> pets = viewModel.getPets();

        if (pets == null || pets.isEmpty()) {
            JLabel empty = new JLabel("No pets available.");
            empty.setFont(new Font("Arial", Font.PLAIN, 16));
            empty.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(empty);
        } else {
            for (Pet pet : pets) {
                listPanel.add(createPetCard(pet));
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createPetCard(Pet pet) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel img = new JLabel("No Image", SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(140, 110));
        img.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        info.add(new JLabel("Name: " + pet.getName()));
        info.add(new JLabel("Species: " + pet.getSpecies()));
        info.add(new JLabel("Age: " + pet.getAge()));
        info.add(new JLabel("Size: " + pet.getSize()));

        card.add(img, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshList();
    }
}