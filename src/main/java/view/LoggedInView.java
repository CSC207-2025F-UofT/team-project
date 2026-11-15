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
    private final JButton[][] squares = new JButton[GRID_SIZE][GRID_SIZE];
    private final JButton galleryButton = new JButton("Gallery");
    private final JLabel numberPlants = new JLabel("Plants in Gallery: 0", SwingConstants.CENTER);

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

        final JPanel galleryInfo = new JPanel(new GridLayout(2, 1));
        galleryButton.addActionListener(evt -> galleryButtonClicked());
        galleryInfo.add(galleryButton);
        galleryInfo.add(numberPlants);

        setLayout(new BorderLayout());
        add(grid, BorderLayout.CENTER);
        add(galleryInfo, BorderLayout.EAST);
    }

    /**
     * Called when a square is clicked.
     * @param row the row of the clicked square
     * @param col the column of the clicked square
     */
    private void squareClicked(int row, int col) {
        // TODO: CODE PLANT ACTIONS
    }

    private void galleryButtonClicked(){
        // TODO: CODE GALLERY
    }

    @Override
    public void stateChanged() {
        final LoggedInState state = loggedInViewModel.getState();

        final Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).setTitle("Logged in as: " + state.getUsername());
        }
    }

    // TODO: CODE PLANT COUNT
    // changeCountLabel.setText("Plants in Gallery: " + stat.getChangeCount());
}
