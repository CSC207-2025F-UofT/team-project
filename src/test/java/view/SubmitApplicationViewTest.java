package view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interface_adapter.submit_application.SubmitApplicationState;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import org.junit.jupiter.api.Test;

class SubmitApplicationViewTest {

    private final SubmitApplicationViewModel viewModel = new SubmitApplicationViewModel();
    private final SubmitApplicationView view = new SubmitApplicationView(viewModel);

    @Test
    void anotherAdopterDoesNotSeeTheFirstAdoptersDraft() {
        open("adopterA", "p1");
        contactField().setText("a@example.com");

        open("adopterB", "p1");

        assertEquals("", contactField().getText());
    }

    @Test
    void anotherPetStartsAnEmptyForm() {
        open("adopterA", "p1");
        contactField().setText("a@example.com");

        open("adopterA", "p2");

        assertEquals("", contactField().getText());
    }

    @Test
    void aFailedCheckKeepsTheAnswers() {
        open("adopterA", "p1");
        contactField().setText("a@example.com");

        viewModel.getState().setError("Please fill in household members.");
        viewModel.notifyStateChanged();

        assertEquals("a@example.com", contactField().getText());
    }

    @Test
    void aSuccessfulSubmissionClearsTheForm() {
        open("adopterA", "p1");
        contactField().setText("a@example.com");

        viewModel.getState().setSuccessMessage("Application submitted.");
        viewModel.notifyStateChanged();

        assertEquals("", contactField().getText());
    }

    private void open(String adopterId, String petId) {
        final SubmitApplicationState state = viewModel.getState();
        state.setAdopterId(adopterId);
        state.setPetId(petId);
        state.setPetName("Miso");
        viewModel.notifyStateChanged();
    }

    /** The contact field is the third single-line text field on the form. */
    private JTextField contactField() {
        final List<JTextField> fields = new ArrayList<>();
        collect(view, fields);
        return fields.get(2);
    }

    private static void collect(Container container, List<JTextField> fields) {
        for (Component child : container.getComponents()) {
            if (child instanceof JTextField) {
                fields.add((JTextField) child);
            }
            if (child instanceof Container) {
                collect((Container) child, fields);
            }
        }
    }
}
