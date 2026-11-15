package view;

import interface_adapter.study_set.StudySetState;
import interface_adapter.study_set.StudySetViewModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View of the user's study sets.
 */
public class StudySetView extends JPanel implements PropertyChangeListener {
  public static final String VIEW_NAME = StudySetViewModel.VIEW_NAME;

  private final StudySetViewModel viewModel;
  private final JLabel nameLabel = new JLabel(" ");
  private final JLabel countLabel = new JLabel(" ");

  /**
   * Builds the view.
   *
   * @param viewModel the view model to listen to
   * @param onBack    run when the user clicks Back
   */
  public StudySetView(StudySetViewModel viewModel, Runnable onBack) {
    this.viewModel = viewModel;
    viewModel.addPropertyChangeListener(this);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));

    final JButton back = new JButton("Back");
    back.addActionListener(e -> onBack.run());

    add(nameLabel);
    add(countLabel);
    add(back);
  }

  /**
   * Shows the open study set's name and number of flashcards.
   *
   * @param evt the change event from the view model
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    final StudySetState state = viewModel.getState();
    nameLabel.setText(state.getName());
    countLabel.setText(state.getCardCount() == 1
        ? "1 flashcard" : state.getCardCount() + " flashcards");
  }
}