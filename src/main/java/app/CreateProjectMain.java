package app;

import data_access.InMemoryProjectDataAccess;
import interface_adapter.create_project.CreateProjectController;
import interface_adapter.create_project.CreateProjectPresenter;
import interface_adapter.create_project.CreateProjectViewModel;
import use_case.create_project.CreateProjectInteractor;
import view.CreateProjectView;

import javax.swing.*;

public class CreateProjectMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InMemoryProjectDataAccess dataAccess =
                    new InMemoryProjectDataAccess();

            CreateProjectViewModel viewModel =
                    new CreateProjectViewModel();

            CreateProjectPresenter presenter =
                    new CreateProjectPresenter(viewModel);

            CreateProjectInteractor interactor =
                    new CreateProjectInteractor(
                            dataAccess, presenter
                    );

            CreateProjectController controller =
                    new CreateProjectController(interactor);

            CreateProjectView view =
                    new CreateProjectView(controller, viewModel);

            JFrame frame = new JFrame("Create Project");
            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );
            frame.setContentPane(view);
            frame.setSize(600, 450);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}