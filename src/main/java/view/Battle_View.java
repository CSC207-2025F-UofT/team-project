package view;

import entity.Monster;
import entity.User;
import interface_adapter.Battle.Battle_Controller;
import interface_adapter.Battle.Battle_State;
import interface_adapter.Battle.Battle_ViewModel;
import interface_adapter.InventoryUseItem.InventoryUseItem_Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Battle Use Case.
 * Displays the battle interface and handles user interactions.
 */
public class Battle_View extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Battle";
    private final Battle_ViewModel viewModel;
    private Battle_Controller battleController;
    private InventoryUseItem_Controller inventoryController;

    // UI Components
    private final JLabel titleLabel;
    private final JLabel userHpLabel;
    private final JLabel monsterNameLabel;
    private final JLabel monsterHpLabel;
    private final JTextArea battleMessageArea;
    private final JButton attackButton;
    private final JComboBox<String> inventoryDropdown = new JComboBox<>();
    private final JButton useItemButton = new JButton("Use Item");
    private final JTextArea inventoryDetailsArea = new JTextArea(5,20);


    public Battle_View(Battle_ViewModel battleViewModel) {
        this.viewModel = battleViewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Initialize UI components
        titleLabel = new JLabel("Battle", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        userHpLabel = new JLabel("HP: N/A");
        monsterNameLabel = new JLabel("Monster: N/A");
        monsterHpLabel = new JLabel("HP: N/A");

        battleMessageArea = new JTextArea(5, 30);
        battleMessageArea.setEditable(false);
        battleMessageArea.setLineWrap(true);
        battleMessageArea.setWrapStyleWord(true);
        battleMessageArea.setText("Battle is ready to begin...");

        inventoryDetailsArea.setLineWrap(true);
        inventoryDetailsArea.setEditable(false);
        inventoryDetailsArea.setWrapStyleWord(true);
        JScrollPane inventoryDisplayScrollPane = new JScrollPane(inventoryDetailsArea);

        useItemButton.setEnabled(false);

        attackButton = new JButton("Attack");

        // Add action listeners
        attackButton.addActionListener(this);
        inventoryDropdown.addActionListener(e-> {
            String selectedItemName = (String) inventoryDropdown.getSelectedItem();
            if (selectedItemName != null) {  //display details of item
                inventoryDetailsArea.setText(selectedItemName); } else {
                inventoryDetailsArea.setText("");
                useItemButton.setEnabled(false); } } );

        useItemButton.addActionListener(e-> {
            String selectedItemName = (String) inventoryDropdown.getSelectedItem();
            if (selectedItemName != null && inventoryController != null) {
                inventoryController.useItem(selectedItemName);
                inventoryDropdown.removeItem(selectedItemName);
                useItemButton.setEnabled(false);}
        else {useItemButton.setEnabled(true);}} );

        // Layout setup
        setupLayout();
    }

    /**
     * Sets up the layout of the view.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Center panel with battle info
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // User info panel
        JPanel userPanel = new JPanel(new GridLayout(2, 1));
        userPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        userPanel.add(userHpLabel);

        //inventory subpanel
        JPanel inventorySubPanel=  new JPanel(new BorderLayout());
        JLabel inventoryLabel = new JLabel("Inventory");
        inventorySubPanel.add(inventoryLabel, BorderLayout.NORTH);
        inventorySubPanel.add(inventoryDropdown, BorderLayout.CENTER);
        inventorySubPanel.add(useItemButton, BorderLayout.SOUTH);
        userPanel.add(inventorySubPanel);

        // Monster info panel
        JPanel monsterPanel = new JPanel(new GridLayout(2, 1));
        monsterPanel.setBorder(BorderFactory.createTitledBorder("Monster"));
        monsterPanel.add(monsterNameLabel);
        monsterPanel.add(monsterHpLabel);

        // Battle message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        JScrollPane scrollPane = new JScrollPane(battleMessageArea);
        messagePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(userPanel);
        centerPanel.add(monsterPanel);
        centerPanel.add(messagePanel);

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(attackButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the controller for this view.
     */
    public void setBattleController(Battle_Controller controller) {
        this.battleController = controller;
    }

    /**
     * Sets the controller for inventory
     */
    public void setInventoryController(InventoryUseItem_Controller controller) {
        this.inventoryController = controller;
    }
    /**
     * Returns the view name.
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == attackButton) {
            handleAttackButton();
        }
    }

    /**
     * Handles the attack button click.
     */
    private void handleAttackButton() {
        Battle_State state = viewModel.getState();

        if (state.isBattleEnded()) {
            JOptionPane.showMessageDialog(this,
                    "Battle has already ended!",
                    "Battle Over",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (battleController != null && state.getUser() != null && state.getMonster() != null) {
            // Disable attack button
            attackButton.setEnabled(false);
            User user = state.getUser();
            Monster monster = state.getMonster();
            battleController.switchToQuizView(user, monster);
            // Buttons will be re-enabled in propertyChange if battle continues
        } else {
            JOptionPane.showMessageDialog(this,
                    "Battle is not properly initialized!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            Battle_State state = viewModel.getState();
            updateDisplay(state);
        }
    }

    /**
     * Updates all display elements based on the current state.
     */
    private void updateDisplay(Battle_State state) {
        // Update user info
        if (state.getUser() != null) {
            userHpLabel.setText(String.format("HP: %.1f", state.getUserHp()));
        }

        // Update monster info
        if (state.getMonster() != null) {
            monsterNameLabel.setText("Monster: " + state.getMonsterName());
            monsterHpLabel.setText(String.format("HP: %.1f", state.getMonsterHP()));
        }

        // Update battle message
        if (!state.getBattleMessage().isEmpty()) {
            battleMessageArea.append("\n" + state.getBattleMessage());

            // Auto-scroll to bottom
            battleMessageArea.setCaretPosition(battleMessageArea.getDocument().getLength());
        }

        // Handle battle end
        if (state.isBattleEnded()) {
            attackButton.setEnabled(false);
            // Show result dialog
            String title = state.isUserWon() ? "Victory!" : "Defeat!";
            int messageType = state.isUserWon() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;

            JOptionPane.showMessageDialog(this,
                    state.getBattleMessage(),
                    title,
                    messageType);
        } else {
            // Battle continues - re-enable buttons
            attackButton.setEnabled(true);
        }
    }
}



