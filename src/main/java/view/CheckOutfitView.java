package view;

import interface_adapter.checkOutfit.CheckOutfitController;
import interface_adapter.checkOutfit.CheckOutfitViewModel;
import interface_adapter.checkOutfit.CheckOutfitState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sun.scenario.effect.impl.prism.PrEffectHelper.render;

public class CheckOutfitView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Checkout Fit View";
    private final CheckOutfitViewModel checkoutFitViewModel;

    private CheckOutfitController checkOutfitController;

    // Top info labels (optional, but nice)
    private final JLabel cityName = new JLabel();
    private final JLabel weather = new JLabel();
    private final JLabel temperature = new JLabel();
    private final JLabel feelsLike = new JLabel();
    private final JLabel humidity = new JLabel();

    // Buttons (actions NOT implemented per your request)
    private final JButton goBackButton;
    private final Map<String, JButton> outfitButtons = new LinkedHashMap<>();

    public CheckOutfitView(CheckOutfitViewModel checkoutFitViewModel) {
        this.checkoutFitViewModel = checkoutFitViewModel;
        this.checkoutFitViewModel.addPropertyChangeListener(this);

        // ---- Title ----
        final JLabel title = new JLabel("Check Outfit Fit");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        // ---- Info labels ----
        cityName.setAlignmentX(Component.CENTER_ALIGNMENT);
        weather.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperature.setAlignmentX(Component.CENTER_ALIGNMENT);
        feelsLike.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidity.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(cityName);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(weather);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(temperature);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(feelsLike);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(humidity);
        infoPanel.add(Box.createVerticalStrut(10));

        // ---- 3x3 outfits grid ----
        final JPanel grid = new JPanel(new GridLayout(3, 3, 12, 12));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        final String[][] outfits = {
                {"HOODIE", "Hoodie"},
                {"COAT", "Coat"},
                {"PUFFER", "Puffer Jacket"},
                {"TRENCH", "Trench Coat"},
                {"SWEATER", "Sweater"},
                {"T_SHIRT", "T-Shirt"},
                {"JEANS", "Jeans"},
                {"SHORTS", "Shorts"},
                {"DRESS", "Dress"},
        };

        for (String[] outfit : outfits) {
            String id = outfit[0];
            String name = outfit[1];

            JPanel card = buildOutfitCard(id, name);
            grid.add(card);
        }

        // ---- Bottom row ----
        goBackButton = new JButton("Go Back");

        final JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        bottomRow.add(goBackButton);

        goBackButton.addActionListener(this);

        // ---- Overall layout ----
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(4));
        this.add(title);
        this.add(Box.createVerticalStrut(4));
        this.add(infoPanel);
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroll);
        this.add(bottomRow);
        this.add(Box.createVerticalStrut(6));
        this.add(bottomRow);
        this.add(Box.createVerticalGlue());

    }

    private JPanel buildOutfitCard(String outfitId, String outfitName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        card.setBackground(Color.WHITE);
        card.setOpaque(true);

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(140, 140));
        imageLabel.setMinimumSize(new Dimension(140, 140));
        imageLabel.setMaximumSize(new Dimension(140, 140));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);


        // Try load image from resources: /outfits/HOODIE.png
        ImageIcon icon = loadOutfitIcon("/outfits/" + outfitId + ".png", 140, 140);
        if (icon != null) {
            imageLabel.setIcon(icon);
            imageLabel.setText("");
            imageLabel.setBorder(null);
        } else {
            imageLabel.setPreferredSize(new Dimension(140, 140));
            imageLabel.setMinimumSize(new Dimension(140, 140));
            imageLabel.setMaximumSize(new Dimension(140, 140));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setBorder(BorderFactory.createDashedBorder(new Color(210, 210, 210)));
            imageLabel.setText("No Image");
        }

        JButton button = new JButton(outfitName);
        button.setActionCommand(outfitId);
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        outfitButtons.put(outfitId, button);

        card.add(imageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(button);

        return card;
    }

    private ImageIcon loadOutfitIcon(String resourcePath, int w, int h) {
        try {
            System.out.println("URL=" + getClass().getResource("/outfits/HOODIE.png"));

            var url = getClass().getResource(resourcePath);
            if (url == null) return null;

            Image img = ImageIO.read(url);
            if (img == null) return null;

            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand(); // "HOODIE", "COAT", ...
        if (outfitButtons.containsKey(cmd)) {
            CheckOutfitState s = checkoutFitViewModel.getState();
            checkOutfitController.execute(cmd, s.getWeather(), s.getTemperature());
        } else if (e.getSource() == goBackButton) {
            checkOutfitController.goBack();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        CheckOutfitState state = checkoutFitViewModel.getState();

        cityName.setText("City Name: " + state.getCityName());
        weather.setText("Weather: " + state.getWeather());
        temperature.setText("Temperature: " + state.getTemperature());
        feelsLike.setText("Feels Like: " + state.getFeelsLike());
        humidity.setText("Humidity: " + state.getHumidity());

        if (state.getPopUpMessage() != null && !state.getPopUpMessage().isBlank()) {
            JOptionPane.showMessageDialog(this, state.getPopUpMessage());
        }

        revalidate();
        repaint();
    }

    public void setController(CheckOutfitController controller) {
        this.checkOutfitController = controller;
    }
}
