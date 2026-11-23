package main.view.swing_assets;

import main.view.ViewUtils;

import javax.swing.*;

public class CardLabel extends JLabel {
    private String status;
    private String fillerFilePath;
    private String realFilePath;
    public double x;
    public double y;
    public double width;

    public CardLabel(String realFilePath, String status, double xPct, double yPct, double widthPct) {
        this.realFilePath = realFilePath;
        this.status = status;
        this.x = xPct;
        this.y = yPct;
        this.width = widthPct;

        updateStatus(status);
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        switch (status) {
            case "hidden":
                fillerFilePath = "/cards/card_back.png";
                break;
            case "folded":
                fillerFilePath = "/cards/folded_card.png";
                break;
            case "":
                fillerFilePath = "/cards/null_card.png";
                break;
            case "revealed":
                fillerFilePath = realFilePath;
                break;
        }
        JLabel temp = ViewUtils.createImage(fillerFilePath, x, y, width);
        this.setIcon(temp.getIcon());
        this.setBounds(temp.getBounds());
        revalidate();
        repaint();
    }

    public void setRealFilePath(String realFilePath) {
        this.realFilePath = realFilePath;
    }
}
