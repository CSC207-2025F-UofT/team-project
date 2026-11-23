package view;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class GameView extends JPanel {
    private final CardLayout layout = new CardLayout();

    public GameView() {
        setLayout(layout);
        addPage("title", new TitleView(() -> showPage("street")));
        addPage("street", new StreetView(this::showPage));
        addPage("registrar", new BuildingView("Registrar's Office",
                "Choose your major at Sidney Smith.", () -> showPage("street")));
        addPage("lecture", new BuildingView("Lecture Hall",
                "Complete class challenges at Bahen Centre.", () -> showPage("street")));
        addPage("exam", new BuildingView("Exam Centre",
                "Take exams for your courses.", () -> showPage("street")));
        addPage("residence", new BuildingView("Innis Residence",
                "Rest and save your progress here.", () -> showPage("street")));
        showPage("title");
    }

    private void addPage(String name, JPanel page) {
        page.setName(name);
        add(page, name);
    }

    private void showPage(String name) {
        layout.show(this, name);
    }
}
