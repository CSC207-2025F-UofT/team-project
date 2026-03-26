package view;

import interface_adapter.weatherReportPage.WeatherReportPageController;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import api.geocodingapi.CoordinatesFetcher;
import api.openWeatherApi.WeatherDataFetcher;
import model.Location;
import model.WeatherData;
import service.ExportService;
import service.NotificationService;
import service.ShareService;
import service.WeatherDataService;
import exception.StorageException;
import exception.DownloadPermissionException;
import exception.ShareAppNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


public class WeatherReportView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Weather Report View";
    private final WeatherReportPageViewModel weatherReportViewModel;
    private WeatherReportPageController weatherReportController;

    // Service
    private final ExportService exportService;
    private final NotificationService notificationService;
    private final ShareService shareService;
    private final WeatherDataService weatherDataService;

    // JLabels
    private final JLabel cityName;
    private final JLabel weather;
    private final JLabel temperature;
    private final JLabel feelsLike;
    private final JLabel humidity;

    // Buttons
    private final JButton backToHomeButton;
    private final JButton backToSearchButton;
    private final JButton addToFavouritesButton;
    private final JButton removeFromFavouritesButton;
    private final JButton exportPdfButton;
    private final JButton exportExcelButton;
    private final JButton shareEmailButton;
    private final JButton shareFacebookButton;
    private final JButton checkOutfitButton;
    private final JButton backToFavouritesButton;
    private final JButton toWeatherForecast;



    public WeatherReportView(WeatherReportPageViewModel weatherReportViewModel) {
        this.weatherReportViewModel = weatherReportViewModel;
        this.weatherReportViewModel.addPropertyChangeListener(this);

        // Initialize Service
        this.exportService = new ExportService(false, false);
        this.notificationService = new NotificationService();
        this.shareService = new ShareService(false);
        this.weatherDataService = new WeatherDataService(false);

        // ---- Title ----
        final JLabel title = new JLabel("Weather Report");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        // ---- Labels ----
        cityName = new JLabel();
        cityName.setAlignmentX(Component.CENTER_ALIGNMENT);
        weather = new JLabel();
        weather.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperature = new JLabel();
        temperature.setAlignmentX(Component.CENTER_ALIGNMENT);
        feelsLike = new JLabel();
        feelsLike.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidity = new JLabel();
        humidity.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---- Buttons ----
        backToHomeButton = new JButton(WeatherReportPageViewModel.TO_HOME_LABEL);
        backToSearchButton = new JButton(WeatherReportPageViewModel.TO_SEARCH_LABEL);
        addToFavouritesButton = new JButton(WeatherReportPageViewModel.FAVOURITE_LABEL);
        checkOutfitButton = new JButton(WeatherReportPageViewModel.CHECK_OUTFIT_LABEL);
        removeFromFavouritesButton = new JButton(WeatherReportPageViewModel.UNFAVOURITE_LABEL);
        backToFavouritesButton = new JButton(WeatherReportPageViewModel.GO_FAVOURITES);
        toWeatherForecast = new JButton(WeatherReportPageViewModel.TO_HOURLY_FORECAST);

        exportPdfButton = new JButton("Export as PDF");
        exportExcelButton = new JButton("Export as Excel");
        shareEmailButton = new JButton("Share via Email");
        shareFacebookButton = new JButton("Share to Facebook");


        //Main Panel and Alignment
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(cityName);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(weather);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(temperature);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(feelsLike);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(humidity);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel buttonsPanel2 = new JPanel();
        buttonsPanel2.add(toWeatherForecast);
        buttonsPanel2.add(checkOutfitButton);
        buttonsPanel2.add(addToFavouritesButton);
        buttonsPanel2.add(removeFromFavouritesButton);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(backToSearchButton);
        buttonsPanel.add(backToHomeButton);
        buttonsPanel.add(backToFavouritesButton);

        JPanel exportButtonsPanel = new JPanel();
        exportButtonsPanel.add(exportPdfButton);
        exportButtonsPanel.add(exportExcelButton);
        exportButtonsPanel.add(shareEmailButton);
        exportButtonsPanel.add(shareFacebookButton);

        // -----Layout-----
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(mainPanel);
        this.add(buttonsPanel2);
        this.add(buttonsPanel);
        this.add(exportButtonsPanel);
        this.add(Box.createVerticalGlue());


        backToHomeButton.addActionListener(e ->
                weatherReportController.switchToLoggedInHomePageView()
        );


        backToSearchButton.addActionListener(e ->
                weatherReportController.switchToLoggedInSearchView()
        );

        backToFavouritesButton.addActionListener( e -> {
            weatherReportController.switchToFavouritesPageView();
        });

        toWeatherForecast.addActionListener( e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            try {
                weatherReportController.switchToHourlyForecast(state.getCityName());
            } catch (CoordinatesFetcher.CityNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        checkOutfitButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            weatherReportController.switchToCheckOutfitView(state);
        });


        addToFavouritesButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            try {
                weatherReportController.addToFavourites(state);
            } catch (CoordinatesFetcher.CityNotFoundException | WeatherDataFetcher.CityNotFoundException ex) {
                notificationService.showError("Failed to add to favourites: " + ex.getMessage());
            } catch (Exception ex) {
                notificationService.showError("Add to favourites failed: " + ex.getMessage());
            }
        });


        removeFromFavouritesButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            try {
                weatherReportController.removeFromFavourites();
            } catch (Exception ex) {
                notificationService.showError("Remove from favourites failed: " + ex.getMessage());
            }
        });

        // export PDF
        exportPdfButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            WeatherData weatherData = buildWeatherDataFromState(state);
            if (weatherData == null) return;

            try {
                exportService.exportAsPdf(weatherData);
                notificationService.showSuccess("PDF exported successfully!");
            } catch (StorageException | DownloadPermissionException ex) {
                notificationService.showError(ex.getMessage());
            } catch (RuntimeException ex) {
                notificationService.showError("PDF export failed: " + ex.getMessage());
            }
        });

        // export Excel
        exportExcelButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            WeatherData weatherData = buildWeatherDataFromState(state);
            if (weatherData == null) return;

            try {
                exportService.exportAsExcel(weatherData);
                notificationService.showSuccess("Excel exported successfully!");
            } catch (StorageException | DownloadPermissionException ex) {
                notificationService.showError(ex.getMessage());
            } catch (RuntimeException ex) {
                notificationService.showError("Excel export failed: " + ex.getMessage());
            }
        });

        // share via Email
        shareEmailButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            WeatherData weatherData = buildWeatherDataFromState(state);
            if (weatherData == null) return;

            try {
                shareService.shareByEmail(weatherData);
                notificationService.showSuccess("Shared via Email!");
            } catch (ShareAppNotFoundException ex) {
                notificationService.showError(ex.getMessage());
            }
        });

        // share to Facebook
        shareFacebookButton.addActionListener(e -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            WeatherData weatherData = buildWeatherDataFromState(state);
            if (weatherData == null) return;

            try {
                shareService.shareToFacebook(weatherData);
                notificationService.showSuccess("Shared to Facebook!");
            } catch (ShareAppNotFoundException ex) {
                notificationService.showError(ex.getMessage());
            }
        });


        weatherReportViewModel.addPropertyChangeListener(evt -> {
            WeatherReportPageState state = weatherReportViewModel.getState();
            cityName.setText("City Name: " + state.getCityName());
            weather.setText("Weather: " + state.getWeather());
            temperature.setText("Temperature: " + state.getTemperature());
            feelsLike.setText("Feels Like: " + state.getFeelsLike());
            humidity.setText("Humidity: " + state.getHumidity());
            if (state.getPopUpMessage() != null && !state.getPopUpMessage().isEmpty()) {
                JOptionPane.showMessageDialog(null, state.getPopUpMessage());
                weatherReportController.resetPopUpMessage();
            }
        });
    }


    private WeatherData buildWeatherDataFromState(WeatherReportPageState state) {
        if (state.getCityName() == null || state.getCityName().isEmpty()) {
            notificationService.showError("No weather data available!");
            return null;
        }

        Location location = new Location(
                "loc_" + state.getCityName(),
                state.getCityName()
        );

        String summary = String.format(
                "City Name: %s\nWeather: %s\nTemperature: %s\nFeels Like: %s\nHumidity: %s",
                state.getCityName(),
                state.getWeather(),
                state.getTemperature(),
                state.getFeelsLike(),
                state.getHumidity()
        );

        return new WeatherData(location, summary);
    }


    public void setWeatherReportController(WeatherReportPageController weatherReportController) {
        this.weatherReportController = weatherReportController;
    }

    public String getViewName() {return viewName; }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}

}

