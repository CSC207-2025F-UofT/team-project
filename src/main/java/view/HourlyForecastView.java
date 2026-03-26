package view;

import entity.hourly_forecast_report.HourlyForecastReport;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.hourly_forecast.HourlyForecastController;
import interface_adapter.hourly_forecast.HourlyForecastState;
import interface_adapter.hourly_forecast.HourlyForecastViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class HourlyForecastView extends JPanel implements PropertyChangeListener {
    private final String viewName = "Hourly Forecast View";

    HourlyForecastViewModel hourlyForecastViewModel;
    private HourlyForecastController hourlyForecastController = null;

    JLabel title;
    JPanel contentPanel;
    JButton goBack;
    JButton goHome;

    public HourlyForecastView(HourlyForecastViewModel hourlyForecastViewModel) {
        this.hourlyForecastViewModel = hourlyForecastViewModel;
        hourlyForecastViewModel.addPropertyChangeListener(this);

        //** Title**//
        title = new JLabel(HourlyForecastViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        // ** FORECAST PANEL ** //
        contentPanel = new JPanel();

        //** BUTTONS **//
        final JPanel buttons = new JPanel();
        goBack = new JButton(HourlyForecastViewModel.GO_BACK_LABEL);
        goHome = new JButton(HourlyForecastViewModel.GO_HOME_LABEL);
        buttons.add(goBack);
        buttons.add(goHome);

        //** View **//
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));
        this.add(contentPanel);// spacing under title
        this.add(Box.createVerticalStrut(20)); // spacing under title
        this.add(buttons);

        goHome.addActionListener(e -> {
            hourlyForecastController.SwitchToLoggedInHomePage();
        });

        goBack.addActionListener(e -> {
            hourlyForecastController.switchToWeatherReportView();
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        HourlyForecastState state = hourlyForecastViewModel.getState();
        title.setText(LoggedInHomePageViewModel.WELCOME_LABEL + " for " + state.getCityName());
        HourlyForecastReport report = state.getReport();
        contentPanel.removeAll();
        // Create the grid panel for the forecast
        JPanel gridPanel = new JPanel(new GridLayout(4, 6, 60, 5));

        List<String> dates = report.getDateTimes();
        List<String> weather = report.getWeather();
        List<String> temperature = report.getTemperature();

        // Row 1: Date/Time
        for (String date : dates) {
            JLabel label = new JLabel(date, SwingConstants.CENTER);
            gridPanel.add(label);
        }

        // Row 2: Weather icons (replace with actual ImageIcons if you have them)
        for (String w : weather) {
            JLabel label = new JLabel(getWeatherIcon(w));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            gridPanel.add(label);
        }

        // Row 3: Weather description (text under icon)
        for (String w : weather) {
            JLabel label = new JLabel(w, SwingConstants.CENTER);
            gridPanel.add(label);
        }

        // Row 3: Temperature
        for (String temp : temperature) {
            JLabel label = new JLabel(temp + "°C", SwingConstants.CENTER);
            gridPanel.add(label);
        }

        // Add the new grid to contentPanel
        contentPanel.add(gridPanel);
        contentPanel.revalidate(); // Refresh layout
        contentPanel.repaint();
    }

    private Icon getWeatherIcon(String weather) {
        try {
            String fileName;

            // Map weather description to file name
            switch (weather.toLowerCase()) {
                case "snow":
                    fileName = "snowy.png";
                    break;
                case "clear":
                    fileName = "sunny.png";
                    break;
                case "clouds":
                    fileName = "cloudy.png";
                    break;
                case "rain":
                    fileName = "rainy.png";
                    break;
                default:
                    fileName = "sunny.png"; // fallback
            }

            // Load from resources
            var url = getClass().getResource("/forecast_pictures/" + fileName);
            if (url == null) return null;

            Image img = ImageIO.read(url);
            if (img == null) return null;

            // Scale the image
            Image scaled = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getViewName() {
        return viewName;
    }

    public void setHourlyForecastController(HourlyForecastController hourlyForecastController) {
        this.hourlyForecastController = hourlyForecastController;
    }
}
