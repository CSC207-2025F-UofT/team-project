package org.accessgraph.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.function.BiPredicate;

final class LoginPanel extends JPanel {
    private final JTextField username = new JTextField(19);
    private final JPasswordField password = new JPasswordField(19);
    private final JLabel feedback = Ui.label(" ", 14, false);
    private final BiPredicate<String, char[]> signIn;

    LoginPanel(String demoUsername, String demoPassword, BiPredicate<String, char[]> signIn) {
        super(new GridLayout(1, 2));
        this.signIn = signIn;
        add(brand());

        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Ui.PAPER);
        right.setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints row = new GridBagConstraints();
        row.gridx = 0;
        row.gridy = 0;
        row.weightx = 1;
        row.fill = GridBagConstraints.HORIZONTAL;
        row.anchor = GridBagConstraints.WEST;

        addRow(form, row, Ui.label("Sign in", 36, true), 0, 24);
        addField(form, row, "Username", username);
        addField(form, row, "Password", password);
        JButton submit = Ui.button("Sign in", true);
        submit.addActionListener(event -> submit());
        password.addActionListener(event -> submit());
        username.addActionListener(event -> password.requestFocusInWindow());
        addRow(form, row, submit, 4, 0);
        feedback.setForeground(Ui.ERROR);
        feedback.getAccessibleContext().setAccessibleName("Sign-in feedback");
        addRow(form, row, feedback, 7, 16);
        JLabel demo = Ui.label("Demo account", 13, true);
        demo.setForeground(Ui.MUTED);
        addRow(form, row, demo, 0, 3);
        JLabel credentials = Ui.label(demoUsername + " / " + demoPassword, 15, false);
        credentials.getAccessibleContext().setAccessibleName(
                "Demo username " + demoUsername + ". Demo password " + demoPassword);
        addRow(form, row, credentials, 0, 0);
        right.add(form, new GridBagConstraints());
        add(right);
    }

    void focusUsername() { username.requestFocusInWindow(); }

    private void submit() {
        char[] secret = password.getPassword();
        try {
            if (signIn.test(username.getText(), secret)) {
                feedback.setText(" ");
                username.setText("");
            } else {
                feedback.setText("Check your username and password.");
                password.requestFocusInWindow();
            }
        } finally {
            Arrays.fill(secret, '\0');
            password.setText("");
        }
    }

    private static void addField(JPanel form, GridBagConstraints row, String name, JTextField field) {
        JLabel label = Ui.label(name, 14, true);
        label.setLabelFor(field);
        Ui.styleField(field);
        addRow(form, row, label, 0, 5);
        addRow(form, row, field, 0, 14);
    }

    private static void addRow(JPanel form, GridBagConstraints row, JComponent item, int top, int bottom) {
        row.insets = new Insets(top, 0, bottom, 0);
        form.add(item, row);
        row.gridy++;
    }

    private static JPanel brand() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Ui.PINE);
        JPanel title = new JPanel(new GridLayout(2, 1, 0, 3));
        title.setOpaque(false);
        title.setBorder(BorderFactory.createEmptyBorder(54, 42, 4, 28));
        JLabel name = Ui.label("AccessGraph", 39, true);
        name.setForeground(Color.WHITE);
        JLabel subtitle = Ui.label("Toronto transit map", 18, false);
        subtitle.setForeground(Ui.MINT);
        title.add(name);
        title.add(subtitle);
        panel.add(title, BorderLayout.NORTH);
        panel.add(new TransitLines(), BorderLayout.CENTER);
        return panel;
    }

    private static final class TransitLines extends JComponent {
        TransitLines() { setPreferredSize(new Dimension(300, 250)); }

        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.scale(getWidth() / 460.0, getHeight() / 430.0);
                drawLine(g, new Color(0x57948A), new int[][]{{-18, 260}, {120, 260}, {216, 164}, {490, 164}});
                drawLine(g, new Color(0x9ACABE), new int[][]{{90, -25}, {90, 106}, {168, 184}, {168, 266}, {350, 448}});
                drawLine(g, new Color(0xDAAE60), new int[][]{{320, -20}, {320, 178}, {222, 276}, {222, 449}});
            } finally {
                g.dispose();
            }
        }

        private static void drawLine(Graphics2D g, Color line, int[][] points) {
            Path2D path = new Path2D.Double();
            path.moveTo(points[0][0], points[0][1]);
            for (int index = 1; index < points.length; index++) { path.lineTo(points[index][0], points[index][1]); }
            g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(line);
            g.draw(path);
            for (int index = 1; index < points.length - 1; index++) {
                int x = points[index][0];
                int y = points[index][1];
                g.setColor(Ui.PINE);
                g.fillOval(x - 11, y - 11, 22, 22);
                g.setColor(line);
                g.fillOval(x - 8, y - 8, 16, 16);
                g.setColor(Ui.PINE);
                g.fillOval(x - 4, y - 4, 8, 8);
            }
        }
    }
}
