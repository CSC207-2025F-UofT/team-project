package service;

import exception.ShareAppNotFoundException;
import model.WeatherData;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ShareService {
    private final boolean simulateNoShareApp;
    private final NotificationService notificationService;


    public ShareService(boolean simulateNoShareApp) {
        this.simulateNoShareApp = simulateNoShareApp;
        this.notificationService = new NotificationService();
    }


    public void shareByEmail(WeatherData data) throws ShareAppNotFoundException {
        if (simulateNoShareApp) {
            throw new ShareAppNotFoundException("No available email app detected. Please install one and try again.");
        }

        try {
            String subject = URLEncoder.encode(
                    "Weather Report: " + data.getLocation().getName(),
                    StandardCharsets.UTF_8
            );
            String body = URLEncoder.encode(
                    "Here's the latest weather data:\n\n" + data.getSummary(),
                    StandardCharsets.UTF_8
            );
            String mailtoUri = "mailto:?subject=" + subject + "&body=" + body;

            Desktop.getDesktop().mail(URI.create(mailtoUri));
            notificationService.showSuccess("Email client opened with weather data!");
        } catch (UnsupportedOperationException e) {
            throw new ShareAppNotFoundException("Your system doesn't support opening email apps.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open email app: " + e.getMessage(), e);
        }
    }


    public void shareToFacebook(WeatherData data) throws ShareAppNotFoundException {
        if (simulateNoShareApp) {
            throw new ShareAppNotFoundException("No Facebook app detected. Please install one and try again.");
        }

        try {
            String shareContent = URLEncoder.encode(
                    "Check out the weather for " + data.getLocation().getName() + ":\n" + data.getSummary(),
                    StandardCharsets.UTF_8
            );
            String facebookShareUri = "https://www.facebook.com/sharer/sharer.php?quote=" + shareContent;

            Desktop.getDesktop().browse(URI.create(facebookShareUri));
            notificationService.showSuccess("Facebook share page opened in browser!");
        } catch (UnsupportedOperationException e) {
            throw new ShareAppNotFoundException("Your system doesn't support opening browsers.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to open Facebook: " + e.getMessage(), e);
        }
    }
}