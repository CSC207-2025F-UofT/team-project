package service;

import javax.swing.*;

public class NotificationService {
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Failure",
                JOptionPane.ERROR_MESSAGE
        );
    }
}