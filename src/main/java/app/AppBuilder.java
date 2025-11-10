package app;

import data_access.DemoCourseAccess;
import data_access.GeminiApiDataAccess;
import entities.Course;
import entities.PDFFile;
import interface_adapters.LoadingViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.evaluate_test.*;
import interface_adapters.mock_test.*;
import usecases.evaluate_test.EvaluateTestInteractor;
import usecases.mock_test_generation.MockTestGenerationInteractor;
import views.*;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

public class AppBuilder {
    // --- Shared Components held by the Builder ---
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // --- Data Access Objects ---
    private DemoCourseAccess courseDAO = new DemoCourseAccess();
    private final GeminiApiDataAccess geminiDAO = new GeminiApiDataAccess();

    // --- ViewModels and Views (stored for wiring) ---
    private MockTestViewModel mockTestViewModel;
    private EvaluateTestViewModel evaluateTestViewModel;
    private LoadingViewModel loadingViewModel;
    private WriteTestView writeTestView;
    private EvaluateTestView evaluateTestView;
    private DemoView demoView;

    public AppBuilder() {
        PDFFile dummyPdf = new PDFFile("test.pdf");
        Course dummyCourse = new Course("CS207");
        dummyCourse.addFile(dummyPdf);
        courseDAO.save(dummyCourse);
        System.out.println("Dummy course 'Software Design' created with ID: " + dummyCourse.getCourseId());
    }

    public AppBuilder addDemoView() {
        // The DemoView doesn't have its own ViewModel but needs the MockTestViewModel for the Presenter to target
        mockTestViewModel = new MockTestViewModel();
        demoView = new DemoView(); // Assumes a simple constructor
        cardPanel.add(demoView, "demo view");
        return this;
    }


    public AppBuilder addWriteTestView() {
        writeTestView = new WriteTestView(mockTestViewModel); // The view for taking the test
        cardPanel.add(writeTestView, mockTestViewModel.getViewName());
        return this;
    }

    public AppBuilder addEvaluateTestView() {
        evaluateTestViewModel = new EvaluateTestViewModel();
        evaluateTestView = new EvaluateTestView(evaluateTestViewModel);
        cardPanel.add(evaluateTestView, evaluateTestViewModel.getViewName());
        return this;
    }

    public AppBuilder addLoadingView() {
        loadingViewModel = new LoadingViewModel();
        LoadingView loadingView = new LoadingView(loadingViewModel);
        cardPanel.add(loadingView, loadingViewModel.getViewName());
        return this;
    }

    public AppBuilder addMockTestGenerationUseCase() {
        MockTestPresenter presenter = new MockTestPresenter(mockTestViewModel, viewManagerModel, loadingViewModel);
        MockTestGenerationInteractor interactor = new MockTestGenerationInteractor(courseDAO, geminiDAO, presenter);
        MockTestController controller = new MockTestController(interactor);
        demoView.setMockTestController(controller);
        return this;
    }

    public AppBuilder addEvaluateTestUseCase() {
        // The Presenter for the evaluation results view
        EvaluateTestPresenter evalPresenter = new EvaluateTestPresenter(evaluateTestViewModel, loadingViewModel, viewManagerModel);

        // The Interactor for the evaluation use case. It correctly uses the DAOs.
        EvaluateTestInteractor evalInteractor = new EvaluateTestInteractor(courseDAO, geminiDAO, evalPresenter);

        // The Controller that the WriteTestView will use to trigger the evaluation.
        EvaluateTestController evalController = new EvaluateTestController(evalInteractor);

        // The Presenter for the WriteTestView's navigation (next/prev question).
        MockTestPresenter mockTestPresenter = new MockTestPresenter(mockTestViewModel, viewManagerModel, loadingViewModel);

        // Inject both the controller (for submitting) and the presenter (for navigation) into the WriteTestView.
        writeTestView.setController(evalController);
        writeTestView.setPresenter(mockTestPresenter);

        // Inject the presenter into the EvaluateTestView
        evaluateTestView.setPresenter(evalPresenter);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("StudyFlow AI Assistant");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        // Set the initial view
        viewManagerModel.setState("demo view");
        viewManagerModel.firePropertyChange();

        return application;
    }
}