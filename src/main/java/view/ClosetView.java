/**
 * The Swing screen for showing the closet filtered by category, plus counts and stats.
 * The closet screen: category tabs or a dropdown (All, Tops, Bottoms, Shoes),
 * a grid of item pictures, counts beside each category, and an "Add Item" button.
 * Only shows things and passes clicks to the Controller; no logic here.
 *TODO: for Rie
 */

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import interface_adapter.view_closet.ViewClosetController;

public class ClosetView extends JPanel {

    private final JButton allButton = new JButton("All");
    private final JButton topsButton = new JButton("Tops");
    private final JButton bottomsButton = new JButton("Bottoms");
    private final JButton shoesButton = new JButton("Shoes");
    private final JButton addItemButton = new JButton("Add Item");

    private final JPanel itemsPanel = new JPanel();

    public ClosetView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Heading and category buttons.
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Closet");
        headerPanel.add(title, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new FlowLayout());
        categoryPanel.add(allButton);
        categoryPanel.add(topsButton);
        categoryPanel.add(bottomsButton);
        categoryPanel.add(shoesButton);

        headerPanel.add(categoryPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Scrollable clothing grid.
        itemsPanel.setLayout(new GridLayout(0, 3, 10, 10));

        itemsPanel.add(createItemCard("White T-shirt", "Top", "White"));
        itemsPanel.add(createItemCard("Blue Jeans", "Bottom", "Blue"));
        itemsPanel.add(createItemCard("Sneakers", "Shoes", "Black"));

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 350));
        add(scrollPane, BorderLayout.CENTER);

        // Add Item button.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addItemButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    private JPanel createItemCard(String name, String category, String colour) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel imagePlaceholder =
                new JLabel("Image goes here", SwingConstants.CENTER);
        card.add(imagePlaceholder, BorderLayout.CENTER);

        JPanel details = new JPanel(new GridLayout(0, 1));
        details.add(new JLabel(name));
        details.add(new JLabel(category));
        details.add(new JLabel(colour));

        card.add(details, BorderLayout.SOUTH);

        return card;
    }

    private ViewClosetController viewClosetController;

    public void setViewClosetController(ViewClosetController controller) {
        this.viewClosetController = controller;
    }
}