package view;

import interface_adapter.watchlist.WatchListController;
import interface_adapter.watchlist.WatchListViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WatchlistView extends JPanel implements PropertyChangeListener {

    private final WatchListController controller;
    private final WatchListViewModel viewModel;
    private final String username;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> stockList = new JList<>(listModel);
    private final JTextField stockWatcherField = new JTextField(8);
    private final JTextArea changesArea = new JTextArea(6, 25);
    private final JLabel messageLabel = new JLabel(" ");

    private final JButton backButton = new JButton("Back");
    private final JButton explorerButton = new JButton("Open Stock Explorer");

    public WatchlistView(WatchListController controller, WatchListViewModel viewModel, String userName){
        this.controller = controller;
        this.viewModel = viewModel;
        this.username = userName;

        viewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout(5, 5));

        // add stock
        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Add");
        topPanel.add(new JLabel("Ticker"));
        topPanel.add(stockWatcherField);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        //List of stockwatchers(tickers) w/ latest changes
        changesArea.setEditable(false);
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        centerPanel.add(new JScrollPane(stockList));
        centerPanel.add(new JScrollPane(changesArea));
        add(centerPanel, BorderLayout.CENTER);

        //buttons and message at the bottom
        JPanel bottomPanek = new JPanel(new GridLayout(2, 1));
        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remove Selected");
        JButton refreshButton = new JButton("Refresh prices");
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(explorerButton);
        buttonPanel.add(backButton);
        bottomPanek.add(buttonPanel);
        bottomPanek.add(messageLabel);
        add(bottomPanek, BorderLayout.SOUTH);

        //Navigation buttons
        explorerButton.setVisible(false);
        backButton.setVisible(false);

        addButton.addActionListener(event -> {
            String stockWatcher = stockWatcherField.getText();
            runInBackground(() -> {
                controller.addStock(userName, stockWatcher);
                SwingUtilities.invokeLater(() -> {
                    if(viewModel.getErrorMessage() == null){
                        stockWatcherField.setText("");
                    }
                });
            });
        });

        removeButton.addActionListener(event -> {
            String selected = stockList.getSelectedValue();
            if(selected == null){
                messageLabel.setText("No stock selected");
            }
            else{
                runInBackground(() -> controller.removeStock(userName, selected));
            }
        });

        refreshButton.addActionListener(event -> getChanges());
        runInBackground(() -> controller.showWatchList(username));
    }

    private void runInBackground(Runnable task) {
        messageLabel.setText("Loading");
        new Thread(task).start();
    }

    private void getChanges(){
        runInBackground(() -> controller.getChanges(username));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event){
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for(String stockWatcher : viewModel.getStockWatchers()){
                listModel.addElement(stockWatcher);
            }

            changesArea.setText(viewModel.getChangesText());

            if(viewModel.getErrorMessage() == null){
                messageLabel.setText(" ");
            }
            else{
                messageLabel.setText(viewModel.getErrorMessage());
            }
        });
    }

    public void setOpenStockExplorerAction(Runnable action){
        explorerButton.addActionListener(event -> action.run());
        explorerButton.setVisible(true);
    }

    public void setGoBackAction(Runnable action){
        backButton.addActionListener(event -> action.run());
        backButton.setVisible(true);
    }
}
