package com.studyarc.use_case.track_plan.TestOnUI;

import com.studyarc.view.MilestoneTasksView;
import com.studyarc.view.TrackPlansView;
import com.studyarc.view.ViewingResearchPapersView;

import javax.swing.*;
import java.awt.*;

public class Builder {

    final CardLayout cardLayout = new CardLayout();


    final JPanel sitwchPanel = new JPanel();// The parent Panel for switching between Loginview and Loggedin View
    TestLoginView loginView;
    LoggedInView loggedInView;


    public Builder() {
        this.sitwchPanel.setLayout(cardLayout);
    }

    public Builder addMainpanel() {
        this.loggedInView = new LoggedInView(sitwchPanel, cardLayout);
        this.sitwchPanel.add(loggedInView, "loggedin");
        return this;
    }

    public Builder addLoginview() {
        this.loginView = new TestLoginView(sitwchPanel, cardLayout);
        this.sitwchPanel.add(loginView, "login");
        return this;
    }

    public Builder addMilestoneview() {
        loggedInView.contentPanel.add(new MilestoneTasksView(), "miles");
        return this;
    }

    public Builder addTracksPlanview() {
        loggedInView.contentPanel.add(new TrackPlansView(), "track");
        return this;
    }

    public Builder addPaperView() {
        loggedInView.contentPanel.add(new ViewingResearchPapersView(), "paper");
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Code Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(sitwchPanel);
        return application;
    }

    public static void main(String[] args) {
        JFrame app = new Builder()
                .addLoginview()
                .addMainpanel()
                .addMilestoneview()
                .addTracksPlanview()
                .addPaperView()
                .build();
        app.pack();
        app.setLocationRelativeTo(null);
        app.setSize(800, 550);
        app.setVisible(true);
    }
}
