package view;

import interface_adapter.ViewManagerModel;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class GameView extends JPanel {
    private final CardLayout layout = new CardLayout();

    public GameView(TitleView title, ResidenceView residence, ViewManagerModel navigation) {
        setLayout(layout);
        addPage("title", title);
        addPage("street", new StreetView(navigation::setActiveView));
        addPage("registrar", new BuildingView("Registrar's Office",
                "Choose your major at Sidney Smith.", () -> navigation.setActiveView("street")));
        addPage("lecture", new BuildingView("Lecture Hall",
                "Complete class challenges at Bahen Centre.", () -> navigation.setActiveView("street")));
        addPage("exam", new BuildingView("Exam Centre",
                "Take exams for your courses.", () -> navigation.setActiveView("street")));
        addPage("residence", residence);
        navigation.addPropertyChangeListener(event -> layout.show(this, navigation.getActiveView()));
    }

    private void addPage(String name, JPanel page) {
        page.setName(name);
        add(page, name);
    }
}
