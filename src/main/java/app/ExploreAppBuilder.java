package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.explore.ExploreViewModel;
import interface_adapter.explore.MoveController;
import interface_adapter.explore.MovePresenter;
import interface_adapter.explore.PickUpItemController;
import interface_adapter.explore.PickUpItemPresenter;
import use_case.explore.ExploreDataAccessInterface;
import use_case.explore.move.MoveInputBoundary;
import use_case.explore.move.MoveInteractor;
import use_case.explore.pick_up_item.PickUpItemInputBoundary;
import use_case.explore.pick_up_item.PickUpItemInteractor;
import view.ExploreView;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;

/**
 * Wires up the Explore use case (Use Case 2). Copy the addExploreView() and
 * addExploreUseCases() methods into the team's AppBuilder when merging.
 *
 * <p>This is the only class that knows about every layer. It creates the
 * objects and plugs them together, so no other class has to "new" its own
 * dependencies (dependency injection). It uses the Builder pattern: each add...()
 * method returns "this" so the calls can be chained.</p>
 */
public class ExploreAppBuilder {
    /** Holds every screen; the CardLayout shows one at a time. */
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    /** Shared by all presenters to switch screens. */
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    /** The game state shared by both Explore interactors. */
    private final ExploreDataAccessInterface dataAccess;

    // Created by the add...() methods below.
    private ExploreViewModel exploreViewModel;
    private ExploreView exploreView;
    private MoveController moveController;

    /**
     * Creates the builder.
     *
     * @param dataAccess the data source for the Explore use cases
     */
    public ExploreAppBuilder(ExploreDataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
        cardPanel.setLayout(cardLayout);
        // The ViewManager registers itself as a listener, so we don't need to keep a reference.
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    /**
     * Creates the Explore screen and its view model.
     *
     * @return this builder, for chaining
     */
    public ExploreAppBuilder addExploreView() {
        exploreViewModel = new ExploreViewModel();
        exploreView = new ExploreView(exploreViewModel);
        // Register the screen under its name, so the ViewManager can switch to it.
        cardPanel.add(exploreView, exploreView.getViewName());
        return this;
    }

    /**
     * Adds a placeholder screen until the combat view (Use Case 3) is merged in.
     *
     * @return this builder, for chaining
     */
    public ExploreAppBuilder addPlaceholderCombatView() {
        JPanel combat = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><center>Combat (Use Case 3) starts here.<br>"
                + "This is a placeholder for the combat view.</center></html>",
                SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        combat.add(label, BorderLayout.CENTER);
        // Use the same name MovePresenter switches to.
        cardPanel.add(combat, MovePresenter.COMBAT_VIEW_NAME);
        return this;
    }

    /**
     * Creates the presenters, interactors and controllers for both Explore actions,
     * and gives the controllers to the view. Call addExploreView() first.
     *
     * @return this builder, for chaining
     */
    public ExploreAppBuilder addExploreUseCases() {
        // Move: presenter -> interactor -> controller -> view.
        MovePresenter movePresenter = new MovePresenter(exploreViewModel, viewManagerModel);
        MoveInputBoundary moveInteractor = new MoveInteractor(dataAccess, movePresenter);
        moveController = new MoveController(moveInteractor);
        exploreView.setMoveController(moveController);

        // Pick up item: same chain.
        PickUpItemPresenter pickUpPresenter = new PickUpItemPresenter(exploreViewModel);
        PickUpItemInputBoundary pickUpInteractor = new PickUpItemInteractor(dataAccess, pickUpPresenter);
        exploreView.setPickUpItemController(new PickUpItemController(pickUpInteractor));
        return this;
    }

    /**
     * Creates the window. Call this last.
     *
     * @return the finished window (not visible yet)
     */
    public JFrame build() {
        JFrame frame = new JFrame("D&D Dungeon Crawler - Explore");
        // Closing the window ends the program.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cardPanel);

        // Show the starting room (this also makes the Explore screen the visible card).
        moveController.showCurrentRoom();
        // Size the window to fit its contents, then centre it on the screen.
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
