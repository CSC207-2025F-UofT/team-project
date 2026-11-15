package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInViewModelListener;

import javax.swing.*;
import java.awt.*;

public class LoggedInView extends JPanel implements LoggedInViewModelListener {
    private static final int GRID_SIZE = 3;
    private static final int SQUARE_SIZE = 80;

    private final LoggedInViewModel loggedInViewModel;
    private final JLabel username = new JLabel();
    private final JButton[][] squares = new JButton[GRID_SIZE][GRID_SIZE];

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addListener(this);

        final JPanel grid = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int row = 0; row < GRID_SIZE; row++){
            for (int col = 0; col < GRID_SIZE; col++) {
                final int r = row;
                final int c = col;
                final JButton square = new JButton();
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.addActionListener(evt -> squareClicked(r,c));
                squares[row][col] = square;
                grid.add(square);
            }
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(grid);
    }

    /**
     * Called when a square is clicked.
     * @param row the row of the clicked square
     * @param col the column of the clicked square
     */
    private void squareClicked(int row, int col) {
        // TODO: CODE PLANT ACTIONS
    }

    @Override
    public void stateChanged() {
        final LoggedInState state = loggedInViewModel.getState();

        final Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).setTitle("Logged in as: " + state.getUsername());
        }
    }
}
