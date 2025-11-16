package view;

import data_access.InMemoryCalendarRepository;
import interface_adapter.calendar.AddEventPresenter;
import interface_adapter.calendar.CalendarController;
import interface_adapter.calendar.CalendarViewModel;
import use_case.calendar.AddEventInteractor;
import use_case.calendar.CalendarRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LockIn!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLayout(new BorderLayout(10, 10));

            Color bgBlack = Color.decode("#000000");
            Color panelDark = Color.decode("#020F28");
            Color headerColor = Color.decode("#020F28");
            Color accentHover = Color.decode("#0047A3");
            Color textLight = Color.decode("#E6E6E6");
            Color buttonBase = panelDark;

            JLabel header = new JLabel("LockIn!", SwingConstants.CENTER);
            header.setFont(new Font("Georgia", Font.BOLD, 32));
            header.setOpaque(true);
            header.setBackground(headerColor);
            header.setForeground(textLight);
            header.setPreferredSize(new Dimension(0, 80));
            frame.add(header, BorderLayout.NORTH);

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new GridLayout(6, 1, 0, 0));
            sidebar.setBackground(bgBlack);
            sidebar.setPreferredSize(new Dimension(220, 0));

            CardLayout cardLayout = new CardLayout();
            JPanel content = new JPanel(cardLayout);

            JPanel mainPanel = new JPanel(new GridLayout(2, 1, 0, 0));
            mainPanel.setBackground(bgBlack);

            JPanel section2 = new JPanel();
            section2.setBackground(panelDark);
            JLabel label2 = new JLabel("Due Soon:");
            label2.setFont(new Font("Georgia", Font.PLAIN, 20));
            label2.setForeground(textLight);

            section2.setLayout(new BoxLayout(section2, BoxLayout.X_AXIS));
            section2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            section2.add(label2);
            section2.add(Box.createRigidArea(new Dimension(15, 0)));

            JPanel infoContainerPanel = new JPanel();
            infoContainerPanel.setLayout(new GridLayout(1, 3, 15, 0));
            infoContainerPanel.setOpaque(false);

            for (int i = 0; i < 3; i++) {
                JPanel infoPanel = new JPanel();
                infoPanel.setBackground(Color.BLACK);
                infoPanel.setLayout(new BorderLayout());
                infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JLabel infoLabel = new JLabel("Info " + (i+1), SwingConstants.CENTER);
                infoLabel.setForeground(Color.WHITE);
                infoLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
                infoPanel.add(infoLabel, BorderLayout.CENTER);

                infoContainerPanel.add(infoPanel);
            }
            section2.add(infoContainerPanel);

            JPanel section3 = new JPanel();
            section3.setBackground(panelDark);
            JLabel label3 = new JLabel("Section 3");
            label3.setFont(new Font("Georgia", Font.PLAIN, 20));
            label3.setForeground(textLight);
            section3.add(label3);

            JPanel wrapper2 = new JPanel(new BorderLayout());
            wrapper2.setBackground(bgBlack);
            wrapper2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper2.add(section2, BorderLayout.CENTER);

            JPanel wrapper3 = new JPanel(new BorderLayout());
            wrapper3.setBackground(bgBlack);
            wrapper3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper3.add(section3, BorderLayout.CENTER);

            mainPanel.add(wrapper2);
            mainPanel.add(wrapper3);

            content.add(mainPanel, "home");

            CalendarRepository calendarRepository = new InMemoryCalendarRepository();
            CalendarViewModel calendarViewModel = new CalendarViewModel(calendarRepository);
            AddEventPresenter addEventPresenter = new AddEventPresenter(calendarViewModel);
            AddEventInteractor addEventInteractor = new AddEventInteractor(calendarRepository, addEventPresenter);
            CalendarController calendarController = new CalendarController(addEventInteractor);

            CalendarPanel calendarPanel = new CalendarPanel(calendarController, calendarViewModel);
            content.add(calendarPanel, "calendar");

            String[] buttons = {"Home", "Grades", "Calendar", "Task Manager", "Courses", "Logout"};
            for (String text : buttons) {
                JButton btn = new JButton(text);
                btn.setFont(new Font("Georgia", Font.PLAIN, 18));
                btn.setForeground(textLight);
                btn.setBackground(buttonBase);
                btn.setOpaque(true);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                btn.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { btn.setBackground(accentHover); }
                    @Override public void mouseExited(MouseEvent e) { btn.setBackground(buttonBase); }
                });
                btn.addActionListener(ev -> {
                    if ("Calendar".equals(text)) {
                        cardLayout.show(content, "calendar");
                    } else if ("Home".equals(text)) {
                        cardLayout.show(content, "home");
                    }
                });

                JPanel buttonWrapper = new JPanel(new BorderLayout());
                buttonWrapper.setOpaque(false);
                buttonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                buttonWrapper.add(btn, BorderLayout.CENTER);

                sidebar.add(buttonWrapper);
            }

            frame.add(sidebar, BorderLayout.WEST);
            frame.add(content, BorderLayout.CENTER);
            cardLayout.show(content, "home");

            frame.getContentPane().setBackground(bgBlack);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
