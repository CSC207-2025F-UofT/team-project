package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.SwitchViewController;
import interface_adapter.save_preferences.PreferencesState;
import interface_adapter.save_preferences.PreferencesViewModel;
import interface_adapter.save_preferences.PreferencesViewModelListener;
import interface_adapter.save_preferences.SavePreferencesController;

/**
 * The My profile page: the adopter says what their home is like, once, and every cat is checked against it.
 */
public class PreferencesView extends JPanel implements PreferencesViewModelListener {
    private static final int GAP = 12;

    private final PreferencesViewModel viewModel;
    private final SavePreferencesController controller;
    private final Supplier<String> adopterId;
    private final JCheckBox childrenBox = new JCheckBox("There are children at home");
    private final JCheckBox otherPetsBox = new JCheckBox("There are other pets at home");
    private final JCheckBox medicalBox = new JCheckBox("I can care for a cat that needs daily medication");
    private final JCheckBox kittenBox = new JCheckBox("I can take on a kitten (under 1 year old)");
    private final JCheckBox pregnantBox = new JCheckBox("I can take on a pregnant cat");
    private final JLabel messageLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();

    /**
     * Creates the page.
     *
     * @param viewModel the model whose state this page draws
     * @param controller saves and loads the answers
     * @param adopterId gives the id of the logged-in adopter, empty if nobody is logged in
     * @param switchViewController used by the Back button
     * @param backViewName the screen the Back button returns to
     */
    public PreferencesView(PreferencesViewModel viewModel, SavePreferencesController controller,
                           Supplier<String> adopterId, SwitchViewController switchViewController,
                           String backViewName) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        this.controller = controller;
        this.adopterId = adopterId;
        viewModel.addListener(this);
        getAccessibleContext().setAccessibleName("My profile");

        final JButton back = new JButton("Back to gallery");
        back.setMnemonic(KeyEvent.VK_B);
        back.getAccessibleContext().setAccessibleName("Back to gallery");
        back.addActionListener(event -> switchViewController.switchTo(backViewName));
        final JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, 0, GAP));
        top.add(back, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        final JLabel title = new JLabel("My profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        final JLabel intro = new JLabel("Tell us about your home. We use it to show how well each cat fits you.");
        content.add(title);
        content.add(intro);
        content.add(Box.createVerticalStrut(GAP));
        addBox(content, childrenBox, KeyEvent.VK_C);
        addBox(content, otherPetsBox, KeyEvent.VK_O);
        addBox(content, medicalBox, KeyEvent.VK_M);
        addBox(content, kittenBox, KeyEvent.VK_K);
        addBox(content, pregnantBox, KeyEvent.VK_P);
        content.add(Box.createVerticalStrut(GAP));
        final JButton save = new JButton("Save profile");
        save.setMnemonic(KeyEvent.VK_S);
        save.getAccessibleContext().setAccessibleName("Save profile");
        save.addActionListener(event -> save());
        messageLabel.getAccessibleContext().setAccessibleName("Save result");
        errorLabel.getAccessibleContext().setAccessibleName("Error");
        content.add(save);
        content.add(messageLabel);
        content.add(errorLabel);
        for (java.awt.Component component : content.getComponents()) {
            ((JComponent) component).setAlignmentX(LEFT_ALIGNMENT);
        }
        add(content, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                load();
            }
        });
    }

    private void addBox(JPanel content, JCheckBox box, int mnemonic) {
        box.setMnemonic(mnemonic);
        box.getAccessibleContext().setAccessibleName(box.getText());
        content.add(box);
    }

    /**
     * Fills the page with what the logged-in adopter saved before.
     */
    public void load() {
        controller.load(adopterId.get());
    }

    private void save() {
        controller.save(adopterId.get(), childrenBox.isSelected(), otherPetsBox.isSelected(),
                medicalBox.isSelected(), kittenBox.isSelected(), pregnantBox.isSelected());
    }

    @Override
    public void stateChanged() {
        final PreferencesState state = viewModel.getState();
        childrenBox.setSelected(state.hasChildren());
        otherPetsBox.setSelected(state.hasOtherPets());
        medicalBox.setSelected(state.canHandleMedicalNeeds());
        kittenBox.setSelected(state.canHandleKitten());
        pregnantBox.setSelected(state.canHandlePregnantAnimal());
        messageLabel.setText(state.getMessage());
        errorLabel.setText(state.getError());
    }
}
