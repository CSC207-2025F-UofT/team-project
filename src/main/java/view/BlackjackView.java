package view;


import javax.swing.*;
import java.awt.*;
import interface_adapter.*;
import entity.*;
import use_case.*;


public class BlackjackView extends JFrame {


    private final BlackjackController controller;
    private final BlackjackViewModel viewModel;


    private JLabel walletLabel;
    private JTextField walletField;
    private JTextField betField;


    private JLabel[][] cardSlots = new JLabel[2][5];
// row 0 = dealer, row 1 = player


    public BlackjackView() {
// === Build Clean Architecture Stack ===
        viewModel = new BlackjackViewModel();
        BlackjackPresenter presenter = new BlackjackPresenter(viewModel);
        BlackjackGame game = new BlackjackGame(100);
        BlackjackInteractor interactor = new BlackjackInteractor(game, presenter);
        controller = new BlackjackController(interactor);


        createAndShowGUI();
    }


    private void createAndShowGUI() {
        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLayout(new BorderLayout(10, 10));


// ================= LEFT PANEL =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        walletLabel = new JLabel("Wallet:");
        walletField = new JTextField("$100.00");
        walletField.setEditable(false);


        JLabel betLabel = new JLabel("Bet amount:");
        betField = new JTextField("10");
        JButton betSubmit = new JButton("Submit Bet");
        betSubmit.addActionListener(e -> {
            double bet = Double.parseDouble(betField.getText());
            controller.onDeal(bet);
        });


        JPanel betPanel = new JPanel(new BorderLayout(5, 5));
        betPanel.add(betField, BorderLayout.CENTER);
        betPanel.add(betSubmit, BorderLayout.EAST);


        JLabel winningsLabel = new JLabel("Winnings:");
        JTextField winningsField = new JTextField("$0.00");
        winningsField.setEditable(false);


        JButton cashoutButton = new JButton("Cashout");
        cashoutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Cashout not implemented"));


        leftPanel.add(walletLabel);
        leftPanel.add(walletField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(betLabel);
        leftPanel.add(betPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(winningsLabel);
        leftPanel.add(winningsField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(cashoutButton);

// ================= CENTER (TABLE) =================
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Blackjack Table"));
        tablePanel.setBackground(new Color(0, 100, 0));


        JPanel dealerPanel = new JPanel(new FlowLayout());
        dealerPanel.setOpaque(false);
        dealerPanel.add(new JLabel("Dealer's Hand:"));


        for (int i = 0; i < 5; i++) {
            JLabel card = createCardSlot();
            cardSlots[0][i] = card;
            dealerPanel.add(card);
        }


        JPanel playerPanel = new JPanel(new FlowLayout());
        playerPanel.setOpaque(false);
        playerPanel.add(new JLabel("Your Hand:"));


        for (int i = 0; i < 5; i++) {
            JLabel card = createCardSlot();
            cardSlots[1][i] = card;
            playerPanel.add(card);
        }


        JPanel actionPanel = new JPanel();
        JButton deal = new JButton("Deal");
        JButton hit = new JButton("Hit");
        JButton stand = new JButton("Stand");


        deal.addActionListener(e -> controller.onDeal(Double.parseDouble(betField.getText())));
        hit.addActionListener(e -> controller.onHit());
        stand.addActionListener(e -> controller.onStand());


        actionPanel.add(deal);
        actionPanel.add(hit);
        actionPanel.add(stand);


        tablePanel.add(dealerPanel, BorderLayout.NORTH);
        tablePanel.add(playerPanel, BorderLayout.CENTER);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);


        add(leftPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);


        setVisible(true);
    }


    private JLabel createCardSlot() {
        JLabel card = new JLabel("🂠", SwingConstants.CENTER);
        card.setPreferredSize(new Dimension(80, 120));
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        card.setForeground(Color.WHITE);
        card.setFont(new Font("SansSerif", Font.BOLD, 24));
        return card;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackView::new);
    }

}